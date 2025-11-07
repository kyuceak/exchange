# 1. Requirements — Exchange Trading Application

## 1.1. Purpose

The Exchange Trading Application allows users to register, manage wallets, place buy/sell orders, and execute trades
based on real-time market data. The system mimics a modular enterprise backend.

---

## 1.2. Functional Requirements

| ID   | Requirement                                                 | Module                | Priority |
|------|-------------------------------------------------------------|-----------------------|----------|
| FR-1 | User can register and log in using JWT-based authentication | User                  | High     |
| FR-2 | Customer details are stored and linked with user accounts   | Customer              | Medium   |
| FR-3 | Wallet is automatically created when a user registers       | Wallet                | High     |
| FR-4 | Users can deposit, withdraw, and transfer funds             | Wallet                | High     |
| FR-5 | Users can place buy/sell orders                             | Order                 | High     |
| FR-6 | Orders are matched and executed based on market price       | Trading               | High     |
| FR-7 | Market module provides real-time prices                     | Market                | Medium   |
| FR-8 | System sends notifications after successful trades          | Notification (future) | Low      |

---

## 1.3. Non-Functional Requirements

| Category            | Description                                                     |
|---------------------|-----------------------------------------------------------------|
| **Performance**     | API should respond within <300ms for typical operations.        |
| **Scalability**     | Modular architecture allows independent growth of modules.      |
| **Security**        | JWT Authentication, role-based access, HTTPS-only endpoints.    |
| **Maintainability** | Clean Architecture and Spring Modulith enforce boundaries.      |
| **Availability**    | System should recover from restarts with data integrity intact. |
| **Tech Stack**      | Java 17+, Spring Boot 3.x, PostgreSQL, Docker.                  |

---

## 1.4. Assumptions

- Market data will be mocked.
- Users can have only one wallet for now.
- Trading is limited to internal tokens (no real exchange connection yet).

---

## 1.5. Future Enhancements

- Add WebSocket support for live market updates.
- Integrate Kafka for event streaming between modules.
- Add notification and analytics modules.