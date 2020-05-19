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
 information about spring boot, visit the [official Spring website](https://spring.io/projects/spring-boot). It is 
 responsible for the core game logic, providing the
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
- [MoveService](src/main/java/ch/uzh/ifi/seal/soprafs20/service/move/MoveService.java): The MoveService is responsible 
for the alterations that a given move requires. With the help of the
move specific handler it routes to the appropriate methods to be executed in the gameService and others. It finally
alerts the MoveCalculator to recalculate the next available moves. For this it again relies on the move specific
handlers. This is due to the nature of some moves that require specific follow up moves.
- [UserController](src/main/java/ch/uzh/ifi/seal/soprafs20/controller/UserController.java): The UserController is the 
entry point for any client. It handles user registration and
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

The game provides the possibility for a wide range of possible features that could be added.
In the following we will explore a few by example. The first two might serve as inspiration for the immediate future,
whereas the last one should convey our vision about the future of Toucan.

### The almighty bot

The bots in the game are at the moment rather limited in their game play strategies. This could
be improved in different ways:

- The bots will build roads and settlements whenever they can, and also see that they use the minimum amount of
road to build new settlements.
- The bots take advantage of their resources and trade to the resource least possessed as soon as a threshold is 
reached. This will ensure that the
bots have an optimal resource distribution and therefore can build roads and settlements quicker.
- The bots actively seek out tiles that have a high probability of distributing resources and will build in a way
they have an equal resource distribution.
    
### Trading 2.0

Trading resources in the game currently is only possible with the *National Bank of Toucan*. This can be improved in the 
following ways:

- Players can offer a resouce trades to all players. If a player chooses to accept the trade within a given time
then the trade is executed.
- Players can offer resource trades to specific players. The player can then accept or decline the offered trade
- Players that have a settlement at specific coordinates, for example Harbour coordinates at the edge of the board,
have acces to a more favorable trading ratio when trading with the *National Bank of Toucan*.
    
    
### The Joint Empire

Having a game board that is much bigger, potentially without borders,  would make it possible for players to play as a team.
They could form alliances against other groups and try to conquer the land collaboratively.

## Authors and acknowledgement

We firstly want to thank our Teaching Assistant Moritz Eck for his continued and unwavering support for the duration
of this project.
For the SoPra FS20 we split the Group into a Frontend and Backend team. This required an early on specification of the
API endpoints. We thank everyone from the Frontend team for their continued input in the API development process.


The SoPra FS20 Group 16 Backend Team


## License

Distributed under the Apache 2.0 License. See LICENSE for more information.
