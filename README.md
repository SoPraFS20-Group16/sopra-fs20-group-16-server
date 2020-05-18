# The Settlers of Toucan

#### SoPra FS20 Group 16 - Backend

## Introduction

Welcome! With "The Settlers of Toucan" we aim to create a fun strategy boardgame for everyone. This online
collaborative game allows you to play with friends, strangers and even bots. It is designed for up to four 
people in one game.

Collect resources and use them to build roads, settlements and cities. Expand your empire, use development 
cards and trading options in order to conquer the island of Toucan. Since every move affects every player, it will
 surely keep you on your toes. It gives you some time to establish your strategies - but watch out for the thief!

Based on simple concepts, this game does not fail to provide a diverting experience.

## Technolgies

This backend part of the project is build on the spring boot project-template provided for SoPra FS20. For more
 information about spring boot, visit www.springboot.io. It is responsible for the core game logic, providing the
  game state to the client via a REST API. The game state includes the board, the players as well as all possible
   moves that can be made. Moves can be made using the REST API.

## High-level components

- [GameController](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/GameController.java): The GameController provides
 the API endpoints that enable the game play. It delegates the 
execution tasks to the corresponding services.
- [GameService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/GameService.java): The GameService is responsible for 
creating, reading, updating and deleting the game instances.
- [MoveCalculator](src/main/java/ch/uzh/ifi/seal/soprafs20/service/move/calculator/MoveCalculator.java): The
 MoveCalculator is responsible to generate all legal alterations of a given game instance.
It delegates sub-tasks to its helpers in order to reduce the classes complexity. The generated moves are then again
exposed via the API endpoints defined in the GameController.
- [MoveService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/move/MoveService.java): The MoveService is responsible for the alterations that a given move requires. With the help of the
move specific handler it routes to the appropriate methods to be executed in the gameService and others. It finally
alerts the MoveCalculator to recalculate the next available moves. For this it again relies on the move specific
handlers. This is due to the nature of some moves that require specific follow up moves.
- [UserController](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/UserController.java): The UserController is the entry point for any client. It handles user registration and
authentication. It also provides a list uf all users and their location data, if they chose to share it.

## Launch & Deployment

To contribute to this project you can clone or fork this repository. If you want to add your changes or improvements
you can create a pull request. For more information visit the [Github Help Page](https://help.github.com/en/github).
If you choose to make a pull request, make sure the github actions task **Test Project** passes.

If your pull request is accepted, the project is automatically deployed to heroku. If you want to deploy your own
version check out this guide on 
[how to deploy spring boot applications to heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku).

### How to get started

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), 
[IntelliJ](https://www.jetbrains.com/idea/download/)) and make sure Java 13 is installed on your system.

1. File -> Open... -> sopra-fs20-group-16-server
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### Building with Gradle

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and
[Gradle](https://gradle.org/docs/).

#### Run

```bash
./gradlew bootRun
```

#### Test

```bash
./gradlew test
```

#### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`


## Roadmap

## Authors and acknowledgement

## License

# SoPra RESTful Service Template FS20

## Getting started with Spring Boot

-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: http://spring.io/guides/tutorials/bookmarks/

## Setup this Template with your IDE of choice

Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)) and make sure Java 13 is installed on your system.

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

## Building with Gradle

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing

### Postman

-   We highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.

## Debugging

If something is not working and/or you don't know what is going on. We highly recommend that you use a debugger and step
through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command),
do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug"Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing

Have a look here: https://www.baeldung.com/spring-boot-testing
