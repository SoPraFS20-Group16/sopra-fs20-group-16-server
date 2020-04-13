package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameLinkDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
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
                    ErrorMsg.TOKEN_INVALID,
                    ErrorMsg.NOT_LOGGED_IN);
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
                    ErrorMsg.TOKEN_INVALID,
                    ErrorMsg.NOT_LOGGED_IN);
        }

        // convert API game to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        //findUser that posted the game
        User tmp = new User();
        tmp.setToken(token);
        User postUser = userService.findUser(tmp);

        //Set creator id
        gameInput.setCreatorId(postUser.getId());

        // create game
        Game createdGame = gameService.createGame(gameInput);

        //if created game is null there was a conflict
        if (createdGame == null) {
            throw new RestException(HttpStatus.CONFLICT,
                    ErrorMsg.GAME_CREATION_CONFLICT,
                    ErrorMsg.ALREADY_GAME_WITH_NAME);
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
                    ErrorMsg.TOKEN_INVALID,
                    ErrorMsg.NOT_LOGGED_IN);
        }

        //find game
        Game gameInput = new Game();
        gameInput.setId(gameId);

        Game foundGame = gameService.findGame(gameInput);

        //if game is null then there exists no game with that id
        if (foundGame == null) {
            throw new RestException(HttpStatus.NOT_FOUND, ErrorMsg.NO_GAME_WITH_ID,
                    ErrorMsg.GAME_NOT_EXISTS);
        }

        //check if the user is also a player
        User tempUser = new User();
        tempUser.setToken(token);
        User requestingUser = userService.findUser(tempUser);

        //If user is not a player return  403 forbidden
        if (!gameService.userCanAccessGame(requestingUser, foundGame)) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.USER_NOT_PLAYER_IN_GAME,
                    ErrorMsg.GAME_ACCESS_DENIED);
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
                    ErrorMsg.TOKEN_INVALID,
                    ErrorMsg.NOT_LOGGED_IN);
        }

        //If game does not exists return 404
        Game gameInput = new Game();
        gameInput.setId(gameId);

        Game foundGame = gameService.findGame(gameInput);

        //if game is null then there exists no game with that id
        if (foundGame == null) {
            throw new RestException(HttpStatus.NOT_FOUND, ErrorMsg.NO_GAME_WITH_ID,
                    ErrorMsg.GAME_NOT_EXISTS);
        }


        //Find move
        Move requestedMove = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(move);
        Move foundMove = moveService.findMove(requestedMove);

        //If move does not exist return 403
        if (foundMove == null) {
            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.NO_MOVE_WITH_ID,
                    ErrorMsg.MOVE_INVALID);
        }

        //Check if move and game and user build a valid set of instructions

        //Find the user that made the request
        User userFromToken = new User();
        userFromToken.setToken(token);
        User requestingUser = userService.findUser(userFromToken);

        //If user is not a player of the game return  403 forbidden
        if (!gameService.userCanAccessGame(requestingUser, foundGame)) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.USER_NOT_PLAYER_IN_GAME,
                    ErrorMsg.GAME_ACCESS_DENIED);
        }

        //The move is not part of the game that was posted to
        if (!foundMove.getGameId().equals(foundGame.getId())) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.PATHVARIABLE_NOT_MATCH_ID,
                    ErrorMsg.MOVE_INVALID);
        }

        //If the users Id does not match the moves PlayerId return 403
        if (!requestingUser.getId().equals(foundMove.getUserId())) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.USER_NOT_MATCH_PLAYER_ID,
                    ErrorMsg.NOT_ALLOWED_TO_MAKE_MOVE);
        }

        //If everything is correct perform the move
        moveService.performMove(foundMove);

        //Make recalculations
        moveService.makeRecalculations(foundGame);
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
