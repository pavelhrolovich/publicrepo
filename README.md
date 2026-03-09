# Public Java Showcase

This repository collects a set of small Java and Kotlin showcase projects ranging from algorithm exercises to Spring Boot APIs. The projects are intentionally independent, so each one keeps its own `pom.xml` and can be built on its own.

## Projects

| Project | Stack | Notes |
| --- | --- | --- |
| `carpet-slicing` | Java 17, Maven | Small scratch project kept for completeness in the showcase repo. |
| `carpetslisingtask` | Kotlin 2.3, JUnit 4, Maven | Finds the largest carpet slice without a hole. |
| `prefixsearcher` | Kotlin 2.3, JUnit 4, Maven | Prefix-tree implementation for fast prefix lookups. |
| `p2ploancalculator` | Java 17, Guice, Lombok, Maven | CLI loan quote calculator backed by lender CSV data. |
| `orderboard-app` | Spring Boot 2.7, Maven | REST API for a live order board exercise. |
| `ec2-dashboard-service` | Spring Boot 2.7, AWS SDK v1, Maven | EC2 dashboard API with caching and Swagger UI support. |

## Build

Use Java 17 and Maven 3.9+.

```bash
mvn -B -f carpetslisingtask/pom.xml test
mvn -B -f prefixsearcher/pom.xml test
mvn -B -f p2ploancalculator/pom.xml test
mvn -B -f orderboard-app/pom.xml test
mvn -B -f ec2-dashboard-service/pom.xml test
mvn -B -f carpet-slicing/pom.xml test
```

## CI

GitHub Actions runs the projects independently through a matrix workflow in [`./.github/workflows/ci.yml`](./.github/workflows/ci.yml).

The EC2 dashboard project excludes credentialed end-to-end tests from default CI runs. Those tests still need AWS-specific credentials and should be run intentionally in a configured environment.

## Repository Files

- [`./CONTRIBUTING.md`](./CONTRIBUTING.md): contribution and pull request guidelines.
- [`./AGENTS.md`](./AGENTS.md): repository-specific instructions for Codex and other coding agents.
- [`./.codex/config.toml`](./.codex/config.toml): local Codex defaults for working in this repo.

## Notes

The Spring projects were upgraded to the latest practical versions that fit their pre-Jakarta codebase without a full framework rewrite. If you want them moved to Spring Boot 3/4 later, that should be handled as a separate migration.
