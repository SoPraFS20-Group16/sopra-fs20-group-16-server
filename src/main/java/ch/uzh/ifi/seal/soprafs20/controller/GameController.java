package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSummary;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameLinkDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MovePutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.HistoryService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    private final Logger log = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;
    private final UserService userService;
    private final MoveService moveService;
    private final PlayerService playerService;
    private final HistoryService historyService;

    GameController(GameService gameService,
                   UserService userService,
                   MoveService moveService,
                   PlayerService playerService,
                   HistoryService historyService) {

        this.gameService = gameService;
        this.userService = userService;
        this.moveService = moveService;
        this.playerService = playerService;
        this.historyService = historyService;
    }


    /**
     * GET /games.
     * <p>
     * returns for all games an Id and a link in an array
     * <p>
     * Success: 200 OK, Failure 401 UNAUTHORIZED
     *
     * @return List of GameLinkDTOs
     */
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameLinkDTO> getGames(@RequestHeader(name = "Token") String token) {

        //Check token for validity
        GameControllerHelper.checkToken(userService, token);

        //Get list of games from game service
        List<Game> games = gameService.getGames();

        //Create new list for DTOs
        List<GameLinkDTO> gameLinks = new ArrayList<>();

        //Create DTOs for each game
        for (Game game : games) {
            gameLinks.add(DTOMapper.INSTANCE.convertGameToGameLinkDTO(game));
        }

        log.info("GET /games called");

        return gameLinks;
    }

    /**
     * POST /games
     *
     * creates a new game instance
     *
     * Success: 201 CREATED, Failure 503 SERVICE UNAVAILABLE, 401 UNAUTHORIZED
     *
     * @param gamePostDTO the gamePostDTO
     * @return the appropriate Response entity according to API specification
     */
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<GameLinkDTO> postGames(@RequestHeader(name = "Token") String token,
                                                 @RequestBody GamePostDTO gamePostDTO) {

        //Check token for validity
        GameControllerHelper.checkToken(userService, token);

        // convert API game to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        //findUser that posted the game
        User tmp = new User();
        tmp.setToken(token);
        User postUser = userService.findUser(tmp);

        //Check if the user is already a player in the game
        GameControllerHelper.checkIfUserIsInAnotherGameElseThrow403Forbidden(postUser, playerService);

        //Set creator id
        gameInput.setCreatorId(postUser.getId());

        // create game
        Game createdGame = gameService.createGame(gameInput);
        GameControllerHelper.checkConflict(createdGame);

        // add game location to header
        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", String.format("/games/%d", createdGame.getId()));

        //response body dtp
        GameLinkDTO responseBody = DTOMapper.INSTANCE.convertGameToGameLinkDTO(createdGame);

        log.info("POST /games called");

        //Compose Response
        return new ResponseEntity<>(responseBody, headers, HttpStatus.CREATED);
    }


    /**
     * GET /games/:gameId
     *
     * returns the game that has the Id gameId
     *
     * Success: 200 OK, Failure: 403 FORBIDDEN, 404 NOT FOUND, 401 UNAUTHORIZED
     * (If the game exists but the user lacks permission, then 403 is returned)
     *
     * @param gameId the games unique id
     * @return the game
     */
    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object getGameWithId(@RequestHeader(name = "Token") String token,
                                 @PathVariable Long gameId) {

        //Check token for validity
        GameControllerHelper.checkToken(userService, token);

        // check for gameSummary (e.g. game has been finished)
        GameSummary summary = gameService.findGameSummary(gameId);

        // return summary if provided
        if (summary != null) {
            return DTOMapper.INSTANCE.convertGameSummaryToGameSummaryDTO(summary);
        }

        //find game else throw 404
        Game foundGame = GameControllerHelper.checkIfGameExists(gameService, gameId);

        //check if the user is also a player
        User requestingUser = GameControllerHelper.checkIfUserIsPlayerElseThrow403(
                gameService, userService, token, foundGame);

        //If user has access create the GameDTO
        GameDTO gameDTO = DTOMapper.INSTANCE.convertGameToGameDTO(foundGame);

        //Add cards and moves to player
        GameControllerHelper.addCardsAndMoves(moveService, playerService, requestingUser, gameDTO);

        //Add the game History
        GameControllerHelper.addGameHistory(historyService, gameDTO);

        String message = String.format("GET /games/%d called", gameId);
        log.info(message);

        //Return gameDTO
        return gameDTO;
    }

    /**
     * PUT /games/:gameId
     * <p>
     * a requested move is checked for authentication and identification and gets
     * carried out eventually
     * <p>
     * Success: 202 ACCEPTED, Failure: 401 UNAUTHORIZED, 404 NOT FOUND, 403 FORBIDDEN
     * (if the game and the move both exist, but the player lacks permission, then
     * 403 is returned)
     *
     * @param token      unique authentication string for every user
     * @param gameId     unique game Id
     * @param movePutDTO the movePutDTO
     */
    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public void postToGameWithId(@RequestHeader(name = "Token") String token,
                                 @PathVariable Long gameId,
                                 @RequestBody MovePutDTO movePutDTO) {

        //If user does not possess a valid token return 401
        GameControllerHelper.checkToken(userService, token);

        //If game does not exists return 404
        Game foundGame = GameControllerHelper.checkIfGameExists(gameService, gameId);

        //Find move
        Long requestedMoveId = movePutDTO.getMoveId();
        Move foundMove = GameControllerHelper.findMoveIfExistsElseThrow403(moveService, requestedMoveId);

        //Find the user that made the request
        User userFromToken = new User();
        userFromToken.setToken(token);
        User requestingUser = userService.findUser(userFromToken);

        //Check if move and game and user build a valid set of instructions
        GameControllerHelper.checkIsValidGameMoveUserCombinationElseThrow(gameService, foundGame, foundMove, requestingUser);

        String message = String.format("PUT /games/%d called. Player: %d Move: %s",
                gameId, requestingUser.getId(), foundMove.getClass().getSimpleName());
        log.info(message);

        //If everything is correct perform the move
        moveService.performMove(foundMove);
    }

    /**
     * POST /games/:gameId/players
     * <p>
     * creates and adds a new player to the game, based on a user request
     * <p>
     * Success: 202 ACCEPTED, Failure: 401 UNAUTHORIZED, 404 NOT FOUND, 403 FORBIDDEN
     *
     * @param gameId unique game Id
     * @param token  unique authentication string for every user
     */
    @PostMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void postNewPlayerToGameWithGameId(@PathVariable Long gameId,
                                              @RequestHeader(name = "Token") String token) {


        //If user does not possess a valid token return 401
        GameControllerHelper.checkToken(userService, token);

        //If game does not exist throw 404
        Game game = GameControllerHelper.checkIfGameExists(gameService, gameId);

        //Find User (succeeds because it already did in check token
        User requestingUser = userService.findUserWithToken(token);

        //Check if the user is already a player in the game
        GameControllerHelper.checkIfUserIsInAnotherGameElseThrow403Forbidden(requestingUser, playerService);

        //Returns 403 if max was already reached
        GameControllerHelper.checkPlayerMax(game);

        //Check if game has started
        if (game.getStarted()) {
            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.THE_GAME_HAS_STARTED);
        }

        //Create a new player form requesting user
        Player createdPlayer = playerService.createPlayer(requestingUser.getId(), gameId);

        String message = String.format("POST /games/%d /players called. Player: %d", gameId, requestingUser.getId());
        log.info(message);

        //Add player to the game
        gameService.addPlayerToGame(createdPlayer, game);
    }

}
