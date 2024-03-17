# Nim Game Server - Backend

This repository contains the backend implementation of the Nim Game, built with Spring Boot, Kotlin, and Redis.

## Getting Started

1. Clone the repository:

    ```
    git clone https://github.com/davidlourenco6/nim.game.server
    cd nim.game.server
    ```

2. Set up Service Infrastructure:

   Use the following Docker Compose command to start the Redis database:

    ```
    docker-compose up -d
    ```

3. Build the project:

    ```
    ./gradlew build
    ```

4. Run tests (unit and int):

    ```
    ./gradlew test
    ```

5. Run the application:

   When Docker image is running, start Spring Boot application with the following command or using IDE:

    ```
    ./gradlew bootRun
    ```

## Swagger API Documentation

Explore the API endpoints using Swagger at the following URL (locally) or accessing recourses/apiDocs/docs.json:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

[ApiDocs File](src/main/resources/apiDocs/docs.json)

## Notes

---