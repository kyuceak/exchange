# 2. System Design — Exchange Trading App

## 2.1. System Overview

The system is a modular monolith consisting of six main modules:

- User
- Customer
- Wallet
- Trading
- Order
- Market

Each module follows a layered architecture (`domain`, `application`, `infrastructure`, `api`) and communicates through
in-memory domain events.

---

## 2.2. Context Diagram

**Purpose:** Show the system in its environment.

Actors:

- **Trader (User)** — interacts via REST API or frontend UI
- **Admin** — monitors user activity
- **External Market API (mock)** — provides price data
- **Database (PostgreSQL)** — stores persistent data

## 2.3. Container Diagram

Shows major “containers” or systems inside the app.

| Container                   | Technology       | Purpose                    |
|-----------------------------|------------------|----------------------------|
| **Backend Application**     | Spring Boot      | Business logic and APIs    |
| **Database**                | PostgreSQL       | Persistent storage         |
| **Message System (Future)** | Kafka / RabbitMQ | Event-driven communication |
| **Frontend (Future)**       | React            | User interface for trading |

---

## 2.4. Component Diagram (Modules)

**Modules:**

- **User:** Authentication, registration, JWT issuance.
- **Customer:** Customer profiles linked to users.
- **Wallet:** Wallet creation, balance tracking.
- **Order:** Order lifecycle (create, cancel).
- **Trading:** Executes orders based on prices.
- **Market:** Provides market data.

**Relationships:**

- `User` → `Customer` (user owns a customer profile)
- `User` → `Wallet` (creates wallet on registration)
- `Wallet` → `Trading` (funds verification)
- `Trading` → `Order` (execution and state updates)
