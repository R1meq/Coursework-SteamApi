# SteamApi
The SteamApi is a Java application that  allows you to receive and transfer information about PC games, publisher, user browser requirements, user registration, game start, game status tracking
# Functions

- Create, update and delete games, users, publishers, game sessions  and browser requirements
- Read from CSV files
- Write to CSV files
- start and finish game session 

# Technologies Used
- Java
- Spring Framework
- Maven
# Installation and Usage
To run this project locally, follow these steps:

- Clone the repository: git clone https://github.com/R1meq/SteamApi-coursework
- Open project in your IDE
- Build project with command : mvn clean install
- Run the application with command : mvn spring-boot: run
- The application will start running on ```http://localhost:8080```

# Usage
- ```Use GET/games|publishers|users|brwsrReqs|gameSessions```: to retrieve a list of all entities
- ```Use GET /games|publishers|users|brwsrReqs|gameSessions/{id}```: to retrieve a entity by ID
- ```Use POST /games|publishers|users|brwsrReqs|gameSessions```: to create entity (the body requires)
- ```Use PUT /games|publishers|users|brwsrReqs|gameSessions/{id}```: to update an existing entity by ID (the body requires)
- ```Use DELETE /games|publishers|users|brwsrReqs|gameSessions/{id}```: to delete a existing entity by ID
- ```Use GET /gameSessions/startGame/{id}```: to start game session
- ```Use GET /gameSessions/endGame/{id}```:to finish game session
# DataStorage
The application stores data in CSV files located in the ```src/main/resources/games|publishers|users|brwsrReqs|gameSessions```
