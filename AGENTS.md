# AGENTS.md

## Repo Overview

This repository is a Java showcase with multiple independent Maven projects in one Git repository. Treat each project directory as its own build target and avoid introducing cross-project dependencies.

## Working Rules

- Use Java 17-compatible changes by default.
- Prefer small, compatible dependency upgrades over framework rewrites.
- Keep AWS credentialed tests out of default CI unless credentials are explicitly configured.
- Do not commit `.idea`, `target`, `*.iml`, or other generated files.
- When changing one showcase project, verify that shared repo files such as the root `README.md`, `.github/workflows/ci.yml`, and `CONTRIBUTING.md` still make sense.

## Build Commands

Use project-local Maven commands instead of assuming a parent build:

```bash
mvn -B -f carpetslisingtask/pom.xml test
mvn -B -f prefixsearcher/pom.xml test
mvn -B -f p2ploancalculator/pom.xml test
mvn -B -f orderboard-app/pom.xml test
mvn -B -f ec2-dashboard-service/pom.xml test
mvn -B -f carpet-slicing/pom.xml test
```

## Review Focus

- Dependency compatibility for older Spring Boot projects.
- Test stability when updating Mockito, JUnit, or Kotlin.
- CI safety for projects that rely on external systems.
