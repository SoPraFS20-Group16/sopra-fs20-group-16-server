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
     * @return the appropriate ResponseEntity according to the API specification
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

        // Compose Response
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
     * @param userId the Path Variable of the url
     * @return the user with id
     */
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserWithId(@PathVariable(name = "userId") Long userId) {
        // convert API user to internal representation
        User userInput = new User();
        userInput.setId(userId);

        // find user
        User foundUser = userService.findUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }

    /**
     * PUT /login
     *
     * logs in the user with the passed credentials
     *
     * Success: 200 OK, Failure: 401 UNAUTHORIZED
     *
     *
     * @param userPostDTO the user post dto
     * @return TODO: Make return conform to the API specification and write tests
     */
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TokenDTO putLogin(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // login
        User loggedInUser = userService.loginUser(userInput);

        // convert internal representation of user back to API
        return new TokenDTO(loggedInUser.getToken());
    }

    @PutMapping("/logout")
    @ResponseBody
    public ResponseEntity logoutUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // logout user
        userService.logoutUser(userInput);

        return new ResponseEntity(HttpStatus.OK);
    }
}
