# Boot-Catalog (Backend)

REST API for **Backlog Killer**, a personal video game catalog application. Users can register games, associate them with platforms, manage cover/screenshot images, and track playing status (backlog, in progress, finished).

This is a learning-oriented project built progressively through documented phases.

## Tech Stack

- Spring Boot 3.5.7
- Java 25 (Temurin via SDKMAN)
- PostgreSQL 16
- Flyway (database migrations)
- Spring Security + JWT (jjwt 0.12.6)
- Bucket4j (rate limiting)
- Caffeine (in-memory cache)
- Spring Batch (CSV/JSON export jobs)

## Requirements

- Java 25 (via SDKMAN — auto-activated with `.sdkmanrc`)
- PostgreSQL 16+ running on `localhost:5432`

## Running

```bash
./mvnw spring-boot:run
```

Runs at `http://localhost:8080` with the `dev` profile by default.

## Main Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | /api/media | No | List catalog (paginated, filterable by `title`, `platformId`, `status`) |
| GET | /api/media/{id} | No | Get single media item |
| POST | /api/media | JWT | Create media item |
| PUT | /api/media/{id} | JWT | Update media item |
| DELETE | /api/media/{id} | JWT | Delete media item |
| GET | /api/media/export?format=csv\|json | JWT | Export catalog (Spring Batch) |
| POST | /api/media/{id}/images | JWT | Upload image (JPEG, PNG, WebP) |
| GET | /api/platforms | No | List platforms (cached) |
| POST | /api/auth/login | No | Login |
| POST | /api/auth/register | No | Register |

## Development Status

| Phase | Description | Status |
|-------|-------------|--------|
| 1 | Foundations (CRUD, entities, DTOs, validation, global exception handling) | Done |
| 2 | Production-ready (Profiles, Flyway, pagination, auditing, HikariCP tuning) | Done |
| 3 | Security (Spring Security + JWT, registration, MIME validation, rate limiting) | Done |
| 6 | Advanced (PUT endpoint, Specifications, Caffeine cache, Spring Batch export) | In progress |
| 7 | Observability and testing | Pending |
| 8 | Dockerization | Pending |
| 9 | Cloud deployment | Pending |

*(Phases 4–5 were frontend-only and are tracked in the frontend project.)*

## Out of Scope (and Why)

The following topics are **intentionally excluded** from this project. They are valuable in a production/enterprise context but fall outside the learning scope of a solo hobby project:

- **API versioning (`/api/v1/...`).** Adds routing complexity with no practical payoff when there's a single client under development. Trivial to introduce later if the API stabilizes and a public consumer appears.
- **OpenAPI / Swagger documentation.** The API is small and its client is this project's own frontend. The trade-off (extra dependencies, annotation noise, generated YAML drift) isn't worth it at this scale.
- **Advanced APM (Datadog, New Relic, Grafana stack).** Spring Boot Actuator is enough for the traffic volume expected. Integrating a full observability stack is a topic better learned in a team/production setting with real traffic patterns.
- **Error tracking service (Sentry, GlitchTip).** Global exception handling already returns structured errors. In-prod error capture is more relevant when multiple engineers operate a live service.
- **Distributed tracing (OpenTelemetry).** The system is a monolith with a single database — there's no cross-service hop to trace.
- **Load / stress testing (k6, JMeter, Gatling).** Tuning for throughput requires a defined SLO and representative traffic. Without those, numbers are meaningless.
- **Database backup / disaster recovery strategy.** Cloud-hosted managed Postgres (e.g. Neon) handles snapshots transparently in the free tier. Custom backup pipelines belong in environments with compliance requirements.
- **Multi-tenancy, roles / authorities beyond `enabled`.** The domain has a single role (regular user). Adding RBAC would be code for the sake of code.

These omissions are documented deliberately — if this project were extended to a real product, they would become part of a mature roadmap, but teaching the basics clearly takes priority over breadth.