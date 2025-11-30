# MyAnimeChart

MyAnimeChart is a microservice-based application for collecting, analyzing, and visualizing anime data.
It is built with Spring Boot (multi-module) and React, designed for scalability and clean separation of concerns.

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
