# Ledger Module Architecture

## Main Objective

- The source of truth for all money movement in the exchange.
- Ledger module tracks the money movements in the exchange with double-entry bookkeeping.
  Double-entry bookkeeping is a method of tracking the business transactions in two different places
  to reflect a debit and credits.
- Every debit and credit must be balanced --> total debits = total credits.

## Core entities

### Account

- Account chart of accounts entry. Each has a unique code (e.g LIABILITY:BTC:wallet-uuid).
- Account has two scopes which are USER (wallet balances) and SYSTEM (exchange treasury,fees).

### Entry

- An individual DEBIT or CREDIT operation. Has fields like amount, direction, layer.

### Transaction

- A group of entries. Has `referenceId` for idempotency and to reference related operation.

## System bootstrap

- On, startup `LedgerBootstrap` creates system accounts such as crypto holdings, trading fees etc.

## Event-driven Messaging

- Used Kafka for event-driven messaging. Ledger module is the source of truth, so whenever user wallet balance changes
  it notifies the wallet module by publishing an LedgerEntryRecorded event.
- Later, the wallet module consumes these events updates user wallet balances accordingly.

### The Flow

POST request

- → RecordTransactionRequest validates debits = credits
- → TransactionFactory.createOrFindIfExists(referenceId)  ← idempotency
- → Batch-fetch accounts, validate they exist
- → Create Entry objects (debit user, credit system)
- → saveAll() entries
- → LedgerEventPublisher: for USER accounts only, save
  OutboxEvent

### Outbox Pattern (Reliable Even Publishing)

- When publishing events we have a problem called the Dual-write Problem. you can't atomically write to database and
  publish the event.
- Solution: Write the event to DB in the same transaction as the entry, then publish to Kafka seperately with polling.
- Future: Polling mechanism might be replaced with CDC.

  ````                   Same DB Transaction
                    ┌──────────────────────┐
  Entry saved ──────┤  OutboxEvent saved   │
                    └──────────────────────┘
                             │
                OutboxRelayService (every 5s)
                             │
                      ┌──────▼──────┐
                      │    Kafka    │
                      └──────┬──────┘
                             │
                whenComplete() callback
                             │
                      OutboxResultBuffer
                             │
                OutboxFlushScheduler (every 5s)
                             │
                DB batch update (SENT/FAILED)
  ```

- For acknowledge mechanism, `kafkaTemplate.send()` inside `OutboxRelayService` returns a completable future.
  if kafka acknowledges we put it to SuccessQueue inside `OutboxResultBuffer`. if it doesnt we put it to FailureQueue.
  Then we based on the queues if its in SuccessQeueue we mark them as `SENT` otherwise if its in FailureQueue its marked
  as `FAILED`.
  If its failed and we didnt get any acknowledgement we retry it so that we can publish the event in a reliable way.
- **Event Lifecycle**: `PENDING` -> `SENDING` -> `SENT` (or `FAILED` -> retry)





