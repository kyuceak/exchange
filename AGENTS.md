# AGENTS Guidelines

This document encodes conventions and operational rules for agentic edits within this repository. It harmonizes build, test, lint, and style expectations with the project’s Java/Spring tooling and the Copilot guidance found in the repo.

## Build, Lint, Test

- Build the project (fast, tests skipped):
  - `mvn -q -DskipTests package`
- Run all tests: `mvn -q test`
- Run a single test class: `mvn -q -Dtest=SomeTest test`  (replace `SomeTest`)
- Run a single test method: `mvn -q -Dtest=SomeTest#testMethod test`  (replace `SomeTest` and `testMethod`)
- Run only unit tests (convention: tests end with `Test`): `mvn -q -Dtest=*Test test`
- Run a specific module (if multi-module): `mvn -q -pl :exchange -am test`
- Validate code style with Spotless/Checkstyle if configured:
  - `mvn -q spotless:check`
  - `mvn -q checkstyle:check`
- Apply code style fixes (when supported):
  - `mvn -q spotless:apply`

> Note: If Spotless or Checkstyle plugins are not configured in pom.xml, these commands will fail. Consider adding a style-check plugin for consistent linting.

## Code Style Guidelines

### General Principles
- Write clean, predictable, and maintainable Java 17+ code.
- Prefer explicit, readable constructs over clever tricks.
- Minimize boilerplate with Lombok where it matches project conventions (e.g., `@RequiredArgsConstructor`, `@Slf4j`).
- Write small, focused methods; prefer composition over long monoliths.

### Packages & Naming
- Package names follow the existing convention: `com.kutay.exchange.modules.<moduleName>...`.
- Class names use UpperCamelCase; methods and variables use lowerCamelCase.
- DTOs often use Java records when appropriate (e.g., simple payloads), otherwise immutable POJOs.
- Abbreviations should be consistent; avoid mixed-case acronyms in identifiers.

### Imports & Formatting
- Use explicit imports; avoid wildcard imports.
- Static imports, when used, appear before normal imports and are clearly grouped.
- Organize imports in the following order:
  1) static imports
  2) java.*, javax.*
  3) third-party
  4) project-specific
- Alphabetize import groups and separate groups with a blank line.
- Indentation uses 4 spaces; line length should not exceed ~120 characters.
- Do not mix tabs and spaces.
- Use Lombok annotations (e.g., `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@Slf4j`) in a consistent, documented way.

### DTOs & Data Modeling
- Prefer Records for simple, immutable DTOs when the data shape is fixed and there is no behavior.
- For entities and domain models, use Lombok for boilerplate while maintaining clarity; avoid overuse of Lombok where it hampers readability.
- Validate inputs at boundaries; keep domain models focused on business rules.

### Error Handling & API Responses
- Define domain-specific exceptions for invalid states; expose meaningful HTTP status via `@ResponseStatus` or a central `RestControllerAdvice` with `ProblemDetail`.
- Centralize error formatting (title, detail, status) for consistency.
- Never leak internal implementation details in error messages.

### Logging & Observability
- Use `@Slf4j` on services/adapters; log at appropriate levels (debug for verbose trace, info for high-level events, warn/error for issues).
- Avoid logging sensitive data (passwords, secrets).
- Include correlation identifiers when crossing service boundaries for traceability.

### Testing & Quality
- Target at least 80% unit test coverage where feasible; include integration tests for REST endpoints.
- Use `@WebMvcTest` for controller tests; mock service layers with `@MockBean`.
- Write tests that cover edge cases and concurrent scenarios where applicable.
- Keep tests deterministic and independent.

### Security & Validation
- Enforce input validation at controllers; rely on bean validation annotations (`@NotNull`, `@Pattern`, etc.).
- Use Spring Security features (`@PreAuthorize`, JWT-based auth) consistently.
- Avoid exposing sensitive details in responses.

### Documentation & Maintenance
- Document public APIs with Javadoc and meaningful method names.
- Update AGENTS.md and Copilot guidance when conventions evolve.
- Prefer code comments that explain why, not what the code does. Let the code show the what.

## Cursor & Copilot Rules

### Cursor Rules
- No repository-wide Cursor rules detected (`.cursor/rules/` or `.cursorrules` absent).
- If rules are added later, align edits with them and ensure tool outputs comply.

### Copilot Rules (from repository)
- Follow the project Copilot Instructions in `.github/copilot-instructions.md`.
- Respect module structure, domain-driven design, and the CQRS/event-driven guidance in code samples.
- Generate boilerplate that matches existing style (Lombok usage, Spring stereotypes, etc.).
- Provide clear, self-contained examples for complex changes and avoid global, sweeping edits.

## Style References & Examples

- See existing interfaces and implementations for consistent patterns, e.g.:
  - `com.kutay.exchange.modules.auth.api.AuthFacade` and `AuthFacadeImpl`.
  - DTOs such as `RegisterRequest` using `record` for simple payloads.
- Follow the existing project’s use of `@Service`, `@RequiredArgsConstructor`, and `@Slf4j` where appropriate.

## Validation & Next Steps

- After applying changes, run `mvn -q test` to verify tests pass locally.
- If tests fail due to environment specifics, isolate and re-run targeted tests with `-Dtest` selectors.
- If you want, I can scaffold a Spotless/Checkstyle config and wire it into Maven for automated linting.
