package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs.GameLinkDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
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
    public List<GameLinkDTO> getGames() {

        //Check token for validity

        //TestGameList
        //TODO: Get real list of games
        return Collections.singletonList(new GameLinkDTO());
    }

    /**
     * POST /games
     *
     * creates a new game instance
     *
     * Success: 201 CREATED, Failure 503 SERVICE UNAVAILABLE
     *
     * @param gamePostDTO the gamePostDTO
     * @return the appropriate Response entity according to API specification
     */
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpHeaders> postGames(@RequestBody GamePostDTO gamePostDTO) {

        //Check token for validity

        // convert API game to internal representation
        //Game createdGame = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create game
        //Game createdGame = userService.createUser(userInput);

        // add game location to header
        HttpHeaders headers = new HttpHeaders();

        //TODO: Create real game Id in gameService and get it from createdGame
        headers.add("Location", String.format("/game/%d", 1));

        //Compose Response
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * GET /games/:gameId
     *
     * returns the game that has the Id gameId
     *
     * Success: 200 OK, Failure: 403 FORBIDDEN, 404 NOT FOUND
     *(If the game exists but the user lacks permission, then 403 is returned)
     *
     * @param token the users token
     * @param gameId the games unique id
     * @return the game
     */
    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDTO getGameWithId(@RequestBody String token, @PathVariable Long gameId) {

        //Check token for validity

        // find game
        //User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);


        // convert internal representation of user back to API
        //return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);

        //For now return dummy object
        return new GameDTO();
    }
}
