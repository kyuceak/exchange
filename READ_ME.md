# Exchange Backend (Modular Monolith)

A Spring Boot 3.5 + Java 17 backend for an exchange-style system, built as a **modular monolith** with clear module
boundaries and event-driven integration between modules.

## Current Status

This project is in active development.
Implemented (partially or fully):

- Auth
- Customer
- Ledger
- Wallet
- Payment (in progress)
- Planned:
    - Order
    - Market data

## Tech Stack

- Java 17
- Spring Boot 3.5.6
- Spring Modulith 1.4.4
- Spring Data JPA (PostgreSQL)
- Spring Security + JWT
- Apache Kafka
- Redis
- Maven

## Architecture

The codebase follows module-oriented structure under:
`src/main/java/com/kutay/exchange/modules/*`
Typical internal layering per module:

- `api` -> module facade / public contract
- `application` -> use-case orchestration
- `domain` -> entities and business rules
- `infrastructure` -> persistence, messaging, external adapters
- `web` -> controllers + transport DTOs
  Cross-module communication is primarily done through facades and asynchronous events.

## Modules

### Auth

- Registration flow
- User credential handling
- JWT token utilities and filters
- Security integration via `UserDetailsService`

### Customer

- Customer profile creation and lookup
- Facade exposed for other modules (e.g. Auth, Payment)

### Ledger (source of truth)

- Transaction recording and balancing model
- Account creation and ledger entry persistence
- Outbox/event publishing pipeline

### Wallet (read/projection model)

- Wallet creation
- Asset balance projection
- Kafka consumer for ledger events
- Redis-backed caching for query side

### Payment (in progress)

- Payment domain models (bank transfer, crypto/card models)
- Strategy-based execution components
- Webhook handling for fiat deposits/status
- Kafka/outbox plumbing for payment events

## Prerequisites

- Java 17+
- Maven (or use `./mvnw`)
- Docker + Docker Compose
- PostgreSQL running locally
- Kafka + Redis (via compose)

## Configuration

Application config file:
`src/main/resources/application.properties`
Environment variables expected (via `.env`):

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `REDIS_PASSWORD`
  Example:

```env
DB_URL=jdbc:postgresql://localhost:5432/exchange
DB_USERNAME=admin
DB_PASSWORD=1234
JWT_SECRET=replace-with-strong-secret
REDIS_PASSWORD=replace-with-redis-password
```
