# Wallet / Ledger / Payment Architecture

# 1. Goal

This document describes my though process for architecturing for handling fiat deposits and withdrawals.(Crypto deposit
will be implemented in future).

- **Ledger acts as the source of truth.**
- **Wallet balances as a Projection.**
- Event-driven communication between modules.

## 2. Principles

### 2.1 Ledger is the source of truth

- All money movement is recorded as immutable **ledger entries**. Wallet balances are derived from **ledger**.

### 2.2 Wallet balance is a projection

- `wallet_assets` is a fast read projection which is updated by ledger events.

### 2.3 Asynchronous communication

- Modules communicate via domain events. It may lag behind ledger shortly. The reason is that no module should depend on
  another module's availability for finishing the core business write.

# 3. Modules and responsibilities.

### 3.1 Payment module (will be implemented in future)

Responsibilities:

- receive web hooks from banks
- receive blockchain confirmations(crypto)
- validate payment authenticity.(mock)
- Track deposit status(pending -> confirmed -> status). (mock)
- Emit "PaymentSettled" event when confirmed. (via outbox)

### 3.2 Ledger module

Responsibilities:

- writes immutable ledger records.
- its the source of truth.
- enforce idempotency.
- applies accounting invariants.
- Emits "LedgerEntryRecorded" record. (via outbox)

DOES NOT:

- maintain user-facing balances as source of truth.

### 3.3 Wallet Module

Responsibilities:

Split into multiple roles:

#### 1. WalletQuerySerice

- Reads Projection ("wallet_assets")
- Returns wallet and balance DTOs.

#### 2. WalletReservationService (for trading/withdrawal flows)

- manages temporal locks so that we can safely run trading/withdraw operations.
- reserve (lock)
- unreserve (unlock)
- commit reserved (debit locked)
- purely wallet-owned operational actions

#### 3. WalletProjectionUpdater

- Consumes "LedgerEntryRecorded" and updates balance accordingly.
- Emits "WalletBalanceUpdated" (optional)

## 4. Events

### 4.1 Event catalog (overview)

| Event                           | Producer | Consumers                          | Meaning                                       | Idempotency key                 | 
|---------------------------------|----------|------------------------------------|-----------------------------------------------|---------------------------------|
| PaymentSettled                  | Payment  | Ledger                             | Provider confirmed funds are settled/credited | referenceId / providerTxnId     | 
| LedgerEntryRecorded             | Ledger   | WalletProjectionUpdater (+ others) | Ledger entry committed (money moved)          | ledgerEntryId / referenceId     | 
| WalletBalanceUpdated (optional) | Wallet   | UI cache / notifications           | Projection updated                            | walletId + lastProjectedEntryId |

> Note: `WalletBalanceUpdated` is optional. Many systems skip it and let clients query balances.

## 5. Fiat deposit/withdraw flow (chronological)

### 5.1 Deposit

1) **Provider → Payment**: Webhook arrives (may be duplicated)
2) **Payment**:
    - verify signature
    - insert or update payment by `providerTxnId`
    - transition payment to SETTLED
    - emit **PaymentSettled(referenceId, userId, amount, currency, ...)**
3) **Ledger consumes PaymentSettled**:
    - if `referenceId` already exists → ignore (idempotent)
    - insert ledger entry (commit)
    - emit **LedgerEntryRecorded(ledgerEntryId, referenceId, walletId, amount, ...)**
4) **WalletProjectionUpdater consumes LedgerEntryRecorded**:
    - if ledgerEntryId already applied → ignore (idempotent)
    - update `wallet_assets` (available += amount)
    - optionally emit WalletBalanceUpdated

### 5.2  Withdraw

### Consistency semantics

- Ledger is strongly consistent (transaction commit = truth).
- Wallet projection is eventually consistent relative to ledger.

## 6. Data model (minimum)

### 6.1 Payments

- payments(id, providerTxnId UNIQUE, userId, amount, currency, status, createdAt, settledAt, ...)

### 6.2 Ledger

- ledger_entries(id, referenceId UNIQUE, walletId, amount, asset, type, createdAt, ...)

### 6.3 Wallet projection

- wallet_assets(walletId, asset, available, locked, total, lastProjectedLedgerEntryId, updatedAt, ...)

---

## 7. Failure handling & retries

### 7.1 At-least-once delivery

All events are treated as at-least-once. Consumers must be idempotent.

### 7.2 Outbox

Events are written to an outbox table in the same DB transaction as the business write, then published asynchronously.

### 7.3 Consumer retries

- Retry with backoff
- Dead-letter queue/topic for poison messages
- Alert on consumer lag

---

## 8. Observability

- Correlation: `referenceId` carried across Payment → Ledger → Wallet
- Metrics:
    - consumer lag
    - outbox publish latency
    - projection update latency
- Logs:
    - include referenceId, walletId, ledgerEntryId

---

## 9. Open questions / future work

- Chargeback / reversal modeling (compensating ledger entries)
- Partitioning strategy if Kafka is used (walletId recommended)
- Read-your-writes UX strategy (pending delta vs wait for projection)