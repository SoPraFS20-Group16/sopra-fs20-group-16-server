package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.api.IpStackRequest;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.TokenDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final GameService gameService;
    private final UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param gameService the game service
     * @param userService the user service
     */
    UserController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

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
    public ResponseEntity<TokenDTO> postUsers(@RequestBody UserPostDTO userPostDTO,
                                              HttpServletRequest request) {

        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // get location of user if allowed
        if (userInput.isTracking()) {
            String location = getLocation(request.getRemoteAddr());
            userInput.setLocation(location);
        }

        // create user
        User createdUser = userService.createUser(userInput);

        // add user location to header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", String.format("/users/%d", createdUser.getId()));

        // Compose Response
        return new ResponseEntity<>(new TokenDTO(createdUser.getToken()), headers, HttpStatus.CREATED);

    }

    private String getLocation(String ipAddress) {

        // test ip address "2a02:1206:4544:3000:994:4ed3:5028:ae1f"

        // get geolocation information from external API, based on IP address
        IpStackRequest ipStackRequest = new IpStackRequest(ipAddress);

        ipStackRequest.makeRequest();

        if(!ipStackRequest.isSuccess()) {
            return "n/a";
        }

        // transform into location parameters
        String zipCode = ipStackRequest.getZipCode();
        String city = ipStackRequest.getCity();
        String country = ipStackRequest.getCountry();

        if (zipCode == null || city == null || country == null) {
            return "n/a";
        }

        return zipCode + ", " + city + ", " + country;

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
     * @param userPostDTO the user post dto
     * @return token of the logged in user
     */
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TokenDTO putLogin(@RequestBody UserPostDTO userPostDTO) {

        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // login
        User loggedInUser = userService.loginUser(userInput);

        if (loggedInUser == null) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "username does not exist, register first");
        }

        // convert internal representation of user back to API
        return new TokenDTO(loggedInUser.getToken());
    }

    /**
     * PUT /logout
     * <p>
     * logs out the user with the passed credentials
     * <p>
     * Success: 204 NO CONTENT, Failure: 401 UNAUTHORIZED
     *
     * @param token the userToken
     */
    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logoutUser(@RequestHeader(name = "Token") String token) {

        //Get user from token
        User requestingUser = userService.findUserWithToken(token);
        if (requestingUser == null) {
            throw new RestException(HttpStatus.UNAUTHORIZED, ErrorMsg.NO_USER_LOGOUT);
        }

        // logout user
        userService.logoutUser(requestingUser);


        //------- Teardown ---------

        //If logged out user is player tear down game
        Game game = gameService.findGameOfUser(requestingUser.getId());

        //If a game is found then teardown
        if (game != null) {
            gameService.teardownGameWithId(game.getId());
        }

    }
}
