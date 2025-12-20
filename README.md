# MyAnimeChart

MyAnimeChart is a modular application for collecting, analyzing, and visualizing anime data.
It is built with Spring Boot (multi-module) and React, following a clean separation of concerns.

Note: While it incorporates microservice-inspired patterns like API Gateway and service discovery, the core application is currently a modular monolith..

<img width="1888" height="934" alt="image" src="https://github.com/user-attachments/assets/0d11c074-3434-4507-ad5d-d1ae8a052d0f" />

## Project Structure

```text
myanimechart
├── myanimechart-application    # Main Spring Boot entry point
├── myanimechart-core           # Domain models and shared logic
├── myanimechart-discovery      # Service registry (Eureka)
├── myanimechart-gateway        # API Gateway (Spring Cloud Gateway)
├── myanimechart-service        # Business logic and REST APIs
├── myanimechart-website        # React frontend (Yarn)
└── Mal4J                       # MyAnimeList Java API (submodule)
```

```text

                           ┌───────────────────────────────┐
                           │      User / Web Browser       │
                           └───────────────┬───────────────┘
                                           │
                                     (HTTP / REST)
                                           │
                           ┌───────────────▼───────────────┐
                           │     myanimechart-website      │
                           │        (React Frontend)       │
                           └───────────────┬───────────────┘
                                           │
                                     API Requests
                                           │
                           ┌───────────────▼───────────────┐
                           │      myanimechart-gateway     │
                           │   (Spring Cloud API Gateway)  │
                           └───────────────┬───────────────┘
                                           │
                              +------------+-------------------+
                              |                                |
                      Service Discovery                Routed API Calls
                              |                                |
               ┌──────────────▼───────────────┐                |
               │    myanimechart-discovery    │                |
               │       (Eureka Server)        │                |
               └──────────────────────────────┘                |
                                                               │
                                ┌──────────────────────────────▼────────────────────────────────┐
                                │                  myanimechart-application                     │
                                │             (Main Spring Boot / Orchestration)                │
                                │                                                               │
                                │   ┌────────────────────────────┐                              │
                                │   │    myanimechart-service    │                              │
                                │   │   (Business Logic / APIs)  │                              │
                                │   └───────────────┬────────────┘                              │
                                │                   │──────────────────────────────+            │
                                │             uses  │                        uses  │            │
                                │                   ▼                              ▼            │
                                │   ┌────────────────────────────┐  ┌─────────────────────────┐ │
                                │   │      myanimechart-core     │  │        Mal4J            │ │
                                │   │ (Shared Domain / Utilities)│  │(MyAnimeList API Wrapper)│ │
                                │   └────────────────────────────┘  └─────────────────────────┘ │
                                │                                                               │
                                └───────────────────────────────────────────────────────────────┘
```
