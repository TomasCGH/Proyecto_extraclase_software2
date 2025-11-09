# Copilot Instructions for VictusLittleBackend

## Project Overview
VictusLittleBackend is a Java backend application structured as a Maven project. The codebase is organized by domain-driven design principles, with clear separation between controllers, DAOs, entities, and configuration. The main entry point is `VictusRApplication`.

## Architecture & Key Components
- **Controllers**: Handle HTTP requests. Located in `src/main/java/co/edu/uco/easy/victusresidencias/victus_api/controller/`.
- **DAOs**: Data access logic, including CRUD operations and enums/impl subfolders. See `src/main/java/co/edu/uco/easy/victusresidencias/victus_api/dao/`.
- **Entities**: Represent database models. See `src/main/java/co/edu/uco/easy/victusresidencias/victus_api/entity/`.
- **Config**: Application-wide configuration (e.g., CORS) in `config/`.
- **Crosscutting**: Shared helpers and exception handling.

## Build & Test Workflow
- **Build**: Use Maven wrapper scripts: `./mvnw clean install` (Linux/macOS) or `mvnw.cmd clean install` (Windows PowerShell).
- **Run**: Main class is `VictusRApplication`. Use `mvnw spring-boot:run` or run the built JAR from `target/`.
- **Tests**: Test classes are in `src/test/java/co/edu/uco/easy/victusresidencias/victus_api/`. Run with `mvnw test`.

## Conventions & Patterns
- **Package Structure**: Follows `co.edu.uco.easy.victusresidencias.victus_api` for all source files.
- **Entity/DAO Naming**: Entities end with `Entity`, DAOs end with `DAO`.
- **Controller Naming**: Controllers end with `Controlador` (Spanish convention).
- **Configuration**: Properties in `application.properties` (multiple locations: `src/main/resources/`, `bin/`, `target/classes/`).
- **Environment Variables**: Example file at `environment-variables.example`.

## Integration Points
- **Spring Boot**: Application is built on Spring Boot (see `pom.xml`).
- **External Dependencies**: Managed via Maven in `pom.xml`.
- **Docker**: Containerization supported via `Dockerfile`.

## Examples
- To add a new entity, create `XEntity.java` in `entity/`, corresponding DAO in `dao/`, and update controller logic in `controller/`.
- To configure CORS, edit `CorsConfig.java` in `config/`.

## Key Files & Directories
- `pom.xml`: Maven dependencies and build config
- `Dockerfile`: Containerization setup
- `src/main/java/co/edu/uco/easy/victusresidencias/victus_api/`: Main source code
- `src/test/java/co/edu/uco/easy/victusresidencias/victus_api/`: Tests
- `application.properties`: Configuration

---
For unclear or missing conventions, please request clarification or provide feedback to improve these instructions.
