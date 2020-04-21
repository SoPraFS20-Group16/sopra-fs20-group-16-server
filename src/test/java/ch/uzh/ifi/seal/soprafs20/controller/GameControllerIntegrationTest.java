package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GamePostDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerIntegrationTest {


    // The example objects
    String testToken;
    String testUsername;
    String testPassword;
    UserStatus testStatus;
    Game testGame;
    User testUser;
    @Autowired
    private MockMvc mockMvc;
    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Qualifier("playerRepository")
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setup() {


        //A test game
        testGame = new Game();
        testGame.setName("TestName");
        testGame.setCreatorId(1L);

        //Set user fields
        testToken = "TestToken";
        testUsername = "TestUsername";
        testPassword = "TestPassword";
        testStatus = UserStatus.ONLINE;

        //Create the user in the database from whom the calls are made

        //Check if the user already exists
        User found = userRepository.findByUsername(testUsername);
        testUser = Objects.requireNonNullElseGet(found, User::new);

        //Reset all user fields
        testUser.setToken(testToken);
        testUser.setUsername(testUsername);
        testUser.setPassword(testPassword);
        testUser.setStatus(testStatus);

        userRepository.saveAndFlush(testUser);

    }

    @AfterEach
    public void teardown() {

        entityManager.clear();
    }

    @Test
    public void testIntegrationTestSetup() {
        assertEquals("TestToken", testUser.getToken());

        //find the correct user using the token
        User found = userRepository.findByToken(testToken);
        assertEquals(testUser.getUsername(), found.getUsername(), "The username does not match");
    }


    @Test
    public void callGetGames() throws Exception {

        // when
        MockHttpServletRequestBuilder getRequest = get("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", testToken);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void callPostGames() throws Exception {

        //the game post object
        GamePostDTO postDTO = new GamePostDTO();
        postDTO.setWithBots(false);
        postDTO.setName("TestName");

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", testToken)
                .content(asJsonString(postDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/games/%d",
                        gameRepository.findByName("TestName").getId())));

        //test the game was saved correctly
        Game foundGame = gameRepository.findByName("TestName");
        Player foundPlayer = playerRepository.findByUsername(testUsername);

        assertEquals(1, foundGame.getPlayers().size(), "There should be a player in the game!");

        Player gamePlayer = foundGame.getPlayers().get(0);

        assertNotNull(foundGame, "There should exist a game!");
        assertEquals(testUsername, foundPlayer.getUsername(), "There should be a player with that username!");
        assertEquals(foundPlayer.getUsername(), gamePlayer.getUsername(), "The player should be part of the game!");
    }


    /**
     * Helper Method to convert DTOs into a JSON string such that the input can be processed
     *
     * @param object the object to be returned as json
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

}
