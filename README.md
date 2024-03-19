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

/Users/lod2lis/Desktop/Holisticon/nim.game.server/postman

## Swagger API Documentation

Explore the API endpoints accessing recourses/apiDocs/docs.json:

[ApiDocs File](src/main/resources/apiDocs/docs.json)

## Postman Collection

To get started, follow these simple steps:

1. Install Postman: If you haven't already, download and install Postman.

2. Import Collection: Download the collection file (./postman/nim-game-server.postman_collection.json) from this repository and import it into your Postman app.

3. Explore Endpoints: Start exploring CRUD endpoints available in the collection. Each request is thoroughly documented with descriptions, headers, and example payloads in openAPI.
The collection is organized into different requests for functionality. Here's provided CRUD operations:

- createGame
- restartGame
- fetchGame
- playerMove

[NimGameServer Collection](./postman/nim-game-server.postman_collection.json)

## Code Coverage Evidences (run tests with coverage)
![image](https://github.com/davidlourenco6/nim.game.server/assets/57639139/0efd0580-62b6-4acf-b04a-bc4e100de92b)


## Final Notes
All the optional goals were implemented:

a) Implement a version of the game server where the computer utilizes a winning-oriented strategy instead of randomly drawing matches.

b) Implement a parameterized version of the game where the following game settings can be altered.
   1. The number of matches at game start (e.g., 13 in the default game)
   2. The number of matches that can be taken each turn (e.g., 1, 2, or 3 in the default game)


c) Implement a persistent version of the game, such as by using a database or in-memory cache.

## Future Work (Improvements)

1. Create a HumanPlayer VS HumanPlayer mode
2. Create a frontEnd application to integrate with

---
