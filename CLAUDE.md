# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build (fast, tests skipped)
./mvnw -q -DskipTests package

# Run all tests
./mvnw -q test

# Run single test class
./mvnw -q -Dtest=SomeTest test

# Run single test method
./mvnw -q -Dtest=SomeTest#testMethod test

# Run the application
./mvnw spring-boot:run

# Start Kafka (required for event features)
docker-compose -f docker/docker-compose.yml up -d
```

## Architecture Overview

This is a **modular monolith cryptocurrency exchange** using Spring Boot 3.5, Spring Modulith 1.4, and Java 17.

### Module Structure

Each module under `com.kutay.exchange.modules.<module>/` follows this layered architecture:

```
api/            → Facade interfaces (public contract for inter-module communication)
application/    → CQRS: commands/ and queries/ services
domain/         → Business logic, entities, domain services
infrastructure/ → Persistence, messaging (Kafka), outbox pattern
web/            → REST controllers and DTOs
```

### Core Modules

| Module | Purpose |
|--------|---------|
| **ledger** | Double-entry bookkeeping (source of truth for all balances) |
| **wallet** | User wallets, cached balance projections, deposits/withdrawals |
| **customer** | Customer profiles and identity |
| **auth** | JWT authentication, user credentials, roles |
| **order** | Order lifecycle (planned) |
| **trading** | Order matching engine (planned) |
| **market** | Market data, price feeds (planned) |

### Inter-Module Communication

- Modules expose **Facades** (e.g., `LedgerFacade`, `WalletFacade`) as public APIs
- Other modules must use facades, never implementation classes directly
- **Outbox Pattern**: Domain events are stored in `outbox_events` table and relayed to Kafka asynchronously
- **Eventual Consistency**: Ledger is authoritative; Wallet projections are eventually consistent via Kafka events

### Key Domain Concepts

**Ledger (Source of Truth)**:
- `Transaction` → groups related entries with referenceId for idempotency
- `Entry` → individual debit/credit with direction, layer (AVAILABLE/LOCKED), and account reference
- `Account` → chart of accounts with generated codes (e.g., `LIABILITY:USDT:wallet-uuid`)

**Wallet (Read Model)**:
- `Wallet` → container for WalletAssets, linked to Customer
- `WalletAsset` → cached balance per asset (availableBalance, lockedBalance)
- Balance changes flow: Ledger Entry → Kafka Event → Wallet Projection Update

## Code Conventions

### Annotations
- Use Lombok: `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@Slf4j`, `@Builder`
- Spring stereotypes: `@Service`, `@Repository`, `@Component`, `@Entity`

### Financial Calculations
- **Always use BigDecimal** (precision=19, scale=8) for monetary values
- Never use float/double for money

### Persistence
- UUID for distributed entities (Wallet, Transaction, Entry, Account)
- `@Version` for optimistic locking on concurrent updates
- Unique constraints for idempotency (`Transaction.referenceId`, `Account.code`)

### Error Handling
- Define domain-specific exceptions
- Use `ProblemDetail` for API error responses via `@RestControllerAdvice`
- Never expose internal implementation details in errors

### Events
- `LedgerEventType`: LEDGER_ENTRY_CREATED, LEDGER_ENTRY_SETTLED
- Topics configured in `application.properties` via `event-topics.*`

## Configuration

Environment variables (loaded from `.env`):
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` → PostgreSQL connection
- `JWT_SECRET` → JWT signing key

Kafka: `localhost:9092` (start with docker-compose)

## Critical Rules

- **Idempotency**: All ledger transactions must have unique `referenceId`
- **Never allow negative balances**: Validate before debiting
- **Ledger is truth**: Wallet balances are projections, not authoritative
- **Transactions require entries**: Always create balanced debit/credit entries
