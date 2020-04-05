package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Move;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs.GameLinkDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;
    private final UserService userService;
    private final MoveService moveService;

    GameController(GameService gameService, UserService userService, MoveService moveService) {
        this.gameService = gameService;
        this.userService = userService;
        this.moveService = moveService;
    }


    /**
     * GET /games.
     *
     * returns for all games an Id and a link in an array
     *
     * Success: 200
     *
     * @return List of GameLinkDTOs
     */
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameLinkDTO> getGames(@RequestHeader(name = "Token") String token) {

        //Check token for validity
        if (tokenNotValid(token)) {
            throw new RestException(HttpStatus.UNAUTHORIZED,
                    "The token was not valid",
                    "You are not logged in!");
        }

        //Get list of games from game service
        List<Game> games = gameService.getGames();
        //Create new list for DTOs
        List<GameLinkDTO> gameLinks = new ArrayList<>();
        //Create DTOs for each game
        for (Game game : games) {
            gameLinks.add(DTOMapper.INSTANCE.convertGameToGameLinkDTO(game));
        }
        return gameLinks;
    }

    /**
     * POST /games
     * <p>
     * creates a new game instance
     * <p>
     * Success: 201 CREATED, Failure 503 SERVICE UNAVAILABLE, 401 UNAUTHORIZED
     *
     * @param gamePostDTO the gamePostDTO
     * @return the appropriate Response entity according to API specification
     */
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpHeaders> postGames(@RequestHeader(name = "Token") String token,
                                                 @RequestBody GamePostDTO gamePostDTO) {

        //Check token for validity
        if (tokenNotValid(token)) {
            throw new RestException(HttpStatus.UNAUTHORIZED,
                    "The token was not valid",
                    "You are not logged in!");
        }

        // convert API game to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // create game
        Game createdGame = gameService.createGame(gameInput);

        //if created game is null there was a conflict
        if (createdGame == null) {
            throw new RestException(HttpStatus.CONFLICT,
                    "The game creation ran into a conflict",
                    "There is already a game with this Name!");
        }

        // add game location to header
        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", String.format("/games/%d", createdGame.getId()));

        //Compose Response
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * GET /games/:gameId
     * <p>
     * returns the game that has the Id gameId
     * <p>
     * Success: 200 OK, Failure: 403 FORBIDDEN, 404 NOT FOUND, 401 UNAUTHORIZED
     * (If the game exists but the user lacks permission, then 403 is returned)
     *
     * @param gameId the games unique id
     * @return the game
     */
    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDTO getGameWithId(@RequestHeader(name = "Token") String token,
                                 @PathVariable Long gameId) {

        //Check token for validity
        if (tokenNotValid(token)) {
            throw new RestException(HttpStatus.UNAUTHORIZED,
                    "The token was not valid",
                    "You are not logged in!");
        }

        //find game
        Game gameInput = new Game();
        gameInput.setId(gameId);

        Game foundGame = gameService.findGame(gameInput);

        //if game is null then there exists no game with that id
        if (foundGame == null) {
            throw new RestException(HttpStatus.NOT_FOUND, "No game with that Id",
                    "This game does not exist!");
        }

        //check if the user is also a player
        User tempUser = new User();
        tempUser.setToken(token);
        User requestingUser = userService.findUser(tempUser);

        //If user is not a player return  403 forbidden
        if (!gameService.userCanAccessGame(requestingUser, foundGame)) {

            throw new RestException(HttpStatus.FORBIDDEN, "The user is not a player in the game",
                    "Game access denied!");
        }

        //If user has access return the GameDTO

        //TODO: Add the available moves for the player to the game
        return DTOMapper.INSTANCE.convertGameToGameDTO(foundGame);
    }

    @PostMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public void postToGameWithId(@RequestHeader(name = "Token") String token,
                                 @PathVariable Long gameId,
                                 @RequestBody MovePostDTO move) {

        //If user does not possess a valid token return 401
        if (tokenNotValid(token)) {
            throw new RestException(HttpStatus.UNAUTHORIZED,
                    "The token was not valid",
                    "You are not logged in!");
        }

        //If game does not exists return 404
        Game gameInput = new Game();
        gameInput.setId(gameId);

        Game foundGame = gameService.findGame(gameInput);

        //if game is null then there exists no game with that id
        if (foundGame == null) {
            throw new RestException(HttpStatus.NOT_FOUND, "No game with this Id",
                    "This game does not exist!");
        }


        //Find move
        Move requestedMove = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(move);
        Move foundMove = moveService.findMove(requestedMove);

        //If move does not exist return 403
        if (foundMove == null) {
            throw new RestException(HttpStatus.FORBIDDEN, "There is no move with that id",
                    "This is not a valid move!");
        }

        //Check if move and game and user build a valid set of instructions

        //Find the user that made the request
        User userFromToken = new User();
        userFromToken.setToken(token);
        User requestingUser = userService.findUser(userFromToken);

        //If user is not a player of the game return  403 forbidden
        if (!gameService.userCanAccessGame(requestingUser, foundGame)) {

            throw new RestException(HttpStatus.FORBIDDEN, "The user is not a player in the game",
                    "Game access denied!");
        }

        //The move is not part of the game that was posted to
        if (!foundMove.getGameId().equals(foundGame.getId())) {

            throw new RestException(HttpStatus.FORBIDDEN, "The gameId and the PathVariable do not match!",
                    "This is not a valid move!");
        }

        //If the users Id does not match the moves PlayerId return 403
        if (!requestingUser.getId().equals(foundMove.getPlayerId())) {

            throw new RestException(HttpStatus.FORBIDDEN, "Users id does not match moves playerId",
                    "You are not allowed to make this move!");
        }

        //If everything is correct perform the move
        moveService.performMove(foundMove);
    }

    /**
     * Checks the token for validity
     * This method helps guard the /games endpoints but does not
     * differentiate between users!
     *
     * @param token the token to be checked
     * @return returns true if the token is valid, else false
     */
    private boolean tokenNotValid(String token) {
        //Get the logged in user with the token
        User candidate = new User();
        candidate.setToken(token);
        User foundUser = userService.findUser(candidate);

        //If a user is found with that token, then the token is valid
        return foundUser == null;
    }
}
