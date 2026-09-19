# Grainger Pricing Platform

Local demo of the [Lead Software Engineer, Pricing](https://jobs.grainger.com/job/CHICAGO-Lead-Software-Engineer-IL-60654-4203/1412460300/) stack: React/Next.js, Java/Spring Boot, Kafka, PostgreSQL, Docker, Kubernetes manifests, and CI.

The Pricing team derives a **market-relevant price** for each MRO SKU from product cost, customer segment, and competitor ticks.

## Quick start

```bash
docker compose -f infra/docker-compose.yml up --build
```

- Web: http://localhost:3000
- API: http://localhost:8080/api/dashboard
- Engine health: http://localhost:8081/actuator/health

The competitor simulator publishes a price tick every 8 seconds. Open **Dashboard** and **Approvals** to watch Kafka-driven recommendations appear.

## Architecture

```
Next.js  -->  pricing-api (Spring Boot)  -->  PostgreSQL
                         ^
                         |
competitor-simulator --Kafka--> price-engine --+--> PostgreSQL
                                               +--> analytics.price_facts
                                               +--> price-recommendations topic
```

`analytics.price_facts` is the local stand-in for a Snowflake warehouse. In production this consumer would land facts in Snowflake for the data team.

## Maps to the job posting

| Posting | In this repo |
| --- | --- |
| React, Next.js | `apps/web` |
| Java, Spring Boot | `services/pricing-api`, `services/price-engine`, `services/competitor-simulator` |
| REST APIs | `/api/products`, `/api/customers`, `/api/prices`, `/api/recommendations`, `/api/dashboard` |
| Kafka | topics `competitor-prices` and `price-recommendations` |
| PostgreSQL | OLTP catalog, rules, recommendations, applied prices |
| Snowflake | `analytics.price_facts` written by the engine |
| Docker | `infra/docker-compose.yml` and `infra/docker/*` |
| Kubernetes | `infra/k8s` Kustomize (Postgres/Kafka assumed in-cluster) |
| CI/CD | `.github/workflows/ci.yml` |
| Distributed pricing | engine consumes competitor events, API serves reads/approvals |

## Pricing rules

1. Floor = cost × (1 + category min margin)
2. Segment price = list × (1 − segment discount). `LIST` 0%, `CONTRACT` 8%, `STRATEGIC` 15%
3. If a competitor undercuts segment price by the category threshold, match that price, never below floor
4. If the change vs the current customer price is under 5%, auto-apply; otherwise queue `PENDING_APPROVAL`

## Local iteration (without rebuilding images)

```bash
docker compose -f infra/docker-compose.yml up postgres kafka
mvn -pl services/pricing-api -am spring-boot:run
mvn -pl services/price-engine -am spring-boot:run
mvn -pl services/competitor-simulator -am spring-boot:run
cd apps/web && npm install && npm run dev
```

To iterate on the host against Compose Kafka, start only `kafka` (and optionally publish Postgres yourself). Java services in Compose talk to Postgres on the internal Docker network.

## Layout

- `apps/web` — Next.js App Router UI
- `libs/pricing-core` — deterministic quote calculator
- `libs/pricing-events` — Kafka payloads
- `libs/pricing-domain` — JPA entities and repositories
- `services/pricing-api` — REST + Flyway
- `services/price-engine` — Kafka consumer and recommendation writer
- `services/competitor-simulator` — live competitor ticks
- `infra` — Compose, Dockerfiles, Kustomize
