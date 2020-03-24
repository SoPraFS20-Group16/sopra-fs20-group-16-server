package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TokenDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserDTOs.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserDTOs.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService the user service
     */
    UserController(UserService userService) {
        this.userService = userService;
    }

    //TODO: Update all endpoint behaviors (are only mocked in unit test)

    /**
     * GET /users
     *
     * Success: 200 OK
     *
     * @return all the users in the database as DTOs in an array
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    /**
     * POST /users
     *
     * Creates a new user in the database
     *
     * Success: 201 CREATED, Failure: 409 CONFLICT
     *
     * @param userPostDTO the user DTO
     * @return the user DTO of the created user
     */
    @PostMapping("/users")
    public ResponseEntity<TokenDTO> postUsers(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // add user location to header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", String.format("/users/%d", createdUser.getId()));

        // convert internal representation of user back to API representation
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);

        //TODO: Generate a real Token
        createdUser.setToken("thisIsTheUserToken");

        //Compose Response
        return new ResponseEntity<>(new TokenDTO(createdUser.getToken()), headers, HttpStatus.CREATED);

    }

    /**
     * GET /users/:userId
     *
     * gets the user with the given Id as a path variable
     * and returns it as a DTO
     *
     * Success: 200 OK, Failure: 404 NOT FOUND
     *
     * @param userPostDTO the user post dto
     * @return the user with id
     */
    @GetMapping("/users/:userId")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserWithId(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    /**
     * PUT /login
     *
     * logs in the user with the passed credentials
     *
     * Success: 200 OK, Failure: 401 UNAUTHORIZED
     *
     * @param userPostDTO the user post dto
     * @return the user get dto
     */
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO putLogin(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    /**
     * POST /games
     *
     * creates a new game instance
     *
     * Success: 201 CREATED, Failure 503 SERVICE UNAVAILABLE
     *
     * @param userPostDTO the user post dto
     * @return the user get dto
     */
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO postGames(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    /**
     * GET /games.
     *
     * returns for all games an Id and a link in an array
     *
     * Success: 200
     *
     * @param userPostDTO the user post dto
     * @return the games
     */
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getGames(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    /**
     * GET /games/:gameId
     *
     * returns the game that has the Id gameId
     *
     * Success: 200 OK, Failure: 403 FORBIDDEN, 404 NOT FOUND
     *(If the game exists but the user lacks permission, then 403 is returned)
     *
     * @param userPostDTO the user post dto
     * @return the game with id
     */
    @GetMapping("/games/:gameId")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getGameWithId(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }


}
