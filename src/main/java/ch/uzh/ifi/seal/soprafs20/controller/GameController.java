package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameLinkDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
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
    private final PlayerService playerService;

    GameController(GameService gameService,
                   UserService userService,
                   MoveService moveService,
                   PlayerService playerService) {

        this.gameService = gameService;
        this.userService = userService;
        this.moveService = moveService;
        this.playerService = playerService;
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
        GameControllerHelper.checkToken(userService, token);

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
        GameControllerHelper.checkToken(userService, token);

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
        GameControllerHelper.checkConflict(createdGame);

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
        GameControllerHelper.checkToken(userService, token);

        //find game else throw 404
        Game foundGame = GameControllerHelper.checkIfGameExists(gameService, gameId);

        //check if the user is also a player
        User requestingUser = GameControllerHelper.checkIfUserIsPlayerElseThrow403(
                gameService, userService, token, foundGame);

        //If user has access create the GameDTO
        GameDTO gameDTO = DTOMapper.INSTANCE.convertGameToGameDTO(foundGame);

        //Add cards and moves to player
        GameControllerHelper.addCardsAndMoves(moveService, playerService, requestingUser, gameDTO);

        //Return gameDTO
        return gameDTO;
    }

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public void postToGameWithId(@RequestHeader(name = "Token") String token,
                                 @PathVariable Long gameId,
                                 @RequestBody MovePostDTO moveDTO) {

        //If user does not possess a valid token return 401
        GameControllerHelper.checkToken(userService, token);

        //If game does not exists return 404
        Game foundGame = GameControllerHelper.checkIfGameExists(gameService, gameId);


        //Find move
        Long requestedMoveId = moveDTO.getMoveId();
        Move foundMove = GameControllerHelper.findMoveIfExistsElseThrow403(moveService, requestedMoveId);

        //Find the user that made the request
        User userFromToken = new User();
        userFromToken.setToken(token);
        User requestingUser = userService.findUser(userFromToken);

        //Check if move and game and user build a valid set of instructions
        GameControllerHelper.checkIsValidGameMoveUserCombinationElseThrow(gameService, foundGame, foundMove, requestingUser);

        //If everything is correct perform the move
        moveService.performMove(foundMove);
    }


    //TODO POST /games/gameId/players endpoint
    @PostMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void postNewPlayerToGameWithGameId(@PathVariable Long gameId,
                                              @RequestHeader(name = "Token") String token) {


        //If user does not possess a valid token return 401
        GameControllerHelper.checkToken(userService, token);

        Game game = GameControllerHelper.checkIfGameExists(gameService, gameId);

        //Find User
        User requestingUser = userService.findUserWithToken(token);

        //Create a new player form requesting user
        Player createdPlayer = playerService.createPlayerFromUserId(requestingUser.getId());


        gameService.addPlayerToGame(createdPlayer, game);
    }

}
