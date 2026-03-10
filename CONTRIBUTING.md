# Contributing

Thanks for contributing to this showcase repository.

## Getting Started

1. Use Java 17 and Maven 3.9 or newer.
2. Pick the project directory you want to work on.
3. Run the project-specific Maven build before opening a pull request.

```bash
mvn -B -f <project>/pom.xml test
```

## Project Structure

Each showcase app is intentionally independent. Please avoid coupling projects together unless the change is purely documentation or CI-related.

## Pull Requests

Please keep pull requests focused and easy to review.

- Describe which project folders were changed.
- Mention any dependency upgrades explicitly.
- Call out behavior changes, especially in the Spring services.
- Include follow-up work if a larger migration is intentionally deferred.

## Style

- Prefer simple, readable fixes over broad rewrites.
- Keep dependencies current, but favor compatible upgrades when the project is older.
- Add or update tests when changing behavior.
- Do not commit IDE folders, build outputs, or generated artifacts.

## CI Expectations

GitHub Actions should stay green for every project matrix entry. If a change requires credentials or external services, guard it so the default CI path still passes.
