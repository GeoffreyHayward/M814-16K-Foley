# Guidelines for Junie (and other developers)

All requirements are kept within this repository.

## Important for all changes 
- All functional requirements `REQ-ID: FR-` and are found `WaltonSustainabilityCentre-Requirements.txt`
- Each non-functional `REQ-ID: NFR-` and are found in the folder named `NonfunctionalRequirements`
- When implementing a functional requirements:
  - Include code changes
  - Include automated tests changes
  - Include descriptive help-pages, written in Markdown, in the `user-manual` folder. Also, when requirements change, keep the user manual in sync. 
  - Keep the developer guide, found in the `developer-guide` folder, in sync with all changes.
  - Feel free to add and edit requirements when you see gaps. You are also free to add comments to requirements in the corresponding `Comments:` section (Comment as `Junin AI` within the commenter name block).
  - Also look for and apply all relevant non-functional requirements.
    - And append, as a list each non-functional that was applied, to the written functional requirement.

## Important Architectural Principles

To maintain a clean, maintainable, and testable codebase, we follow a layered architecture:

- **Controller Layer** – Responsible for handling HTTP requests and responses. Delegates logic to services.
- **Service Layer** – Contains the core business logic and orchestrates repository operations.
- **Repository Layer** – Provides access to persistent storage (e.g., database) using Spring Data JPA.
- **DTOs** - Represent external-facing request/response structures.
- **Model** - Represent internal database models.
- **Exception** - Centralised handling of domain-specific and system errors.
- **Config** - Centralised non-security-related configuration of application components.
- **Util** - Contains utility classes and methods, such as formatting and encryption helpers. Utilities must not include business logic.
- **Security** - Centralised security-related configuration of application components.

Each layer has a specific responsibility and should not overstep its boundaries. Each layer should only interact with the one directly below it.

## Important UI guidelines

The UI is driven by server-server templates using Thymeleaf. Always follow these rules:
- Use Bootstrap
- Do not write JavaScript. When making the page dynamic, you can use:
  - HTMX
  - Bootstrap's JS driven components
  - Alpine.js
- Each new template should use a layout page, via the `thymeleaf-layout-dialect`.