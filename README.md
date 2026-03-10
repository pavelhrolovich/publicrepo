# Public Java Showcase

This repository is a collection of small independent Java and Kotlin projects. It includes algorithm exercises, CLI utilities, and Spring Boot APIs that I keep together as a public showcase rather than as a single multi-module build.

Each project has its own `pom.xml`, its own README or notes, and can be built separately.

## Projects

| Project | Stack | What it does |
| --- | --- | --- |
| `carpet-slicing` | Java 17, Maven | Minimal placeholder module kept in the showcase set. |
| `carpetslisingtask` | Kotlin, Maven, JUnit 4 | Solves the carpet slicing challenge by finding the largest slice without a hole. |
| `prefixsearcher` | Kotlin, Maven, JUnit 4 | Prefix-tree based search implementation for fast prefix lookups. |
| `p2ploancalculator` | Java 17, Maven, Guice, Lombok | Command-line loan quote calculator using lender data from CSV input. |
| `orderboard-app` | Java 17+, Spring Boot 4, Maven | REST API implementation of the Silver Bars live order board exercise. |
| `ec2-dashboard-service` | Java 17+, Spring Boot 4, Maven, AWS SDK v1, springdoc | EC2 dashboard API with paging, sorting, HTTP Basic auth, and test coverage. |

## Quick Start

Use Java 17 or newer and Maven 3.9+.

Run a project test suite directly:

```bash
mvn -B -f carpetslisingtask/pom.xml test
mvn -B -f prefixsearcher/pom.xml test
mvn -B -f p2ploancalculator/pom.xml test
mvn -B -f orderboard-app/pom.xml test
mvn -B -f ec2-dashboard-service/pom.xml test
mvn -B -f carpet-slicing/pom.xml test
```

Run all showcase modules locally:

```bash
for project in carpetslisingtask prefixsearcher p2ploancalculator orderboard-app ec2-dashboard-service carpet-slicing; do
  mvn -B -f "$project/pom.xml" test || exit 1
done
```

## CI

GitHub Actions runs the projects independently through the matrix workflow in `./.github/workflows/ci.yml`.

The `ec2-dashboard-service` module excludes credentialed end-to-end tests from normal CI runs. Those tests should only be run in an environment that has the required AWS credentials and configuration.

## Repo Notes

- This repo is intentionally not a Maven aggregator project.
- The Spring Boot modules are now on Spring Boot 4 and are currently tested in CI on Temurin 17.
- Some module-level docs use different file names such as `README`, `README.md`, or `README.txt`, depending on the original project.

## Repository Files

- `README.md`: top-level repo overview.
- `CONTRIBUTING.md`: contribution and pull request guidance.
- `AGENTS.md`: instructions for coding agents working in this repo.
- `.codex/config.toml`: Codex defaults for this workspace.
