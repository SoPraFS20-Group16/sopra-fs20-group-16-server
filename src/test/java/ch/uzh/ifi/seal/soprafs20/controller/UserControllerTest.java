package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserLocationService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserLocationService userLocationService;

    @MockBean
    GameService gameService;

    /**
     * Tests the GET /users endpoint which has to return an array of UserGetDTOs
     *
     * @throws Exception -> An exception can be thrown by the perform method of mockMvc
     */
    @Test
    void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
    }

    /**
     * Tests the POST /users endpoint which has to return a TokenDTO
     * and a location Header with a link to the created user
     *
     * @throws Exception the exception
     */
    @Test
    void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setPassword("password");
        user.setToken("ThisIsTheUserToken");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");

        given(userService.createUser(Mockito.any())).willReturn(user);
        given(userService.save(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().stringValues("Location", "/users/1"))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object the object converted into json
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString())
            );
        }
    }

    /**
     * Tests POST /users given that the userService will throw a RestException
     *
     * @throws Exception the exception
     */
    @Test
    void createUser_userAlreadyExists() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.createUser(Mockito.any())).willThrow(new RestException(HttpStatus.CONFLICT, "The Mocked Exception Reason"));

        // when
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage", is("The Mocked Exception Reason")))
                .andExpect(jsonPath("$.error", not("")));
    }

    /**
     * Tests the GET /users/:userId endpoint which has to return a UserGetDTO
     *
     * @throws Exception -> An exception can be thrown by the perform method of mockMvc
     */
    @Test
    void getUsers_givenUserId_userExists() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.findUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    /**
     * Tests GET /users/1 given that the userService will throw a RestException
     *
     * @throws Exception the exception
     */
    @Test
    void getUsers_givenUserId_userDoesNotExists() throws Exception {

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.findUser(Mockito.any())).willThrow(new RestException(HttpStatus.NOT_FOUND, "The Mocked Exception Reason"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", is("The Mocked Exception Reason")))
                .andExpect(jsonPath("$.error", not("")));
    }

    @Test
    void createUser_emptyInput() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("user_1");
        userPostDTO.setPassword("pw_1");

        given(userService.createUser(Mockito.any())).willThrow(new RestException(HttpStatus.CONFLICT, "empty words are not allowed"));

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage", is("empty words are not allowed")))
                .andExpect(jsonPath("$.error", not("")));
    }

    /**
     * Tests PUT /login given that the credentials are correct
     *
     * @throws Exception the exception
     */
    @Test
    void loginUser_credentialsCorrect() throws Exception {
        //given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("TheUserToken");
        user.setId(1L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");

        given(userService.loginUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("TheUserToken")));


    }

    /**
     * Tests PUT /login given the credentials are incorrect
     *
     * @throws Exception the exception
     */
    @Test
    void loginUser_credentialsIncorrect() throws Exception {
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");

        given(userService.loginUser(Mockito.any())).willThrow(new RestException(HttpStatus.UNAUTHORIZED, "The Mocked Exception Reason"));

        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is("The Mocked Exception Reason")));

    }

    @Test
    void logoutUser_success() throws Exception {

        String testToken = "TestToken";

        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.ONLINE);
        user.setId(1L);
        user.setToken(testToken);

        given(userService.findUserWithToken(testToken)).willReturn(user);

        MockHttpServletRequestBuilder putRequest = put("/logout")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void logoutUser_userNotFound() throws Exception {

        String testToken = "TestToken";

        given(userService.findUserWithToken(testToken)).willReturn(null);

        MockHttpServletRequestBuilder putRequest = put("/logout")
                .header("Token", testToken)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage", is(ErrorMsg.NO_USER_LOGOUT)));
    }
}
