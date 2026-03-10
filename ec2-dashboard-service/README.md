# The EC2 Dashboard API Project

## Prerequisites

- Java 17+
- Maven 3.9+
- AWS account with read-only EC2 access

## Installation

Build the project with:
- `mvn clean package`

Run it locally with:
- `mvn spring-boot:run`

## Security note

Do not send AWS credentials over plain HTTP. If you deploy this service outside local development, terminate TLS and expose only HTTPS endpoints for any credential-bearing flow.
