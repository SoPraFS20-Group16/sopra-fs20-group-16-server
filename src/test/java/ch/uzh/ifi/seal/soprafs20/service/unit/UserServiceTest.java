package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("testUsername");
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setToken("The testToken");

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(RestException.class, () -> userService.createUser(testUser));
    }

    @Test
    void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(RestException.class, () -> userService.createUser(testUser));
    }

    @Test
    void logoutUser_success() {

        userService.createUser(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        userService.logoutUser(testUser);
        assertEquals(UserStatus.OFFLINE, testUser.getStatus());
    }

    @Test
    void loginUser_success() {

        userService.createUser(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        userService.loginUser(testUser);

        assertEquals(UserStatus.ONLINE, testUser.getStatus());
    }

    @Test
    void testFindUser_findById_success() {
        given(userRepository.findUserById(Mockito.any())).willReturn(testUser);

        User user = new User();
        user.setId(testUser.getId());

        User foundUser = userService.findUser(user);

        assertEquals(testUser.getId(), foundUser.getId(), "The id does not match");
        assertEquals(testUser.getUsername(), foundUser.getUsername(), "The username does not match");
        assertEquals(testUser.getToken(), foundUser.getToken(), "The token does not match");
    }

    @Test
    void testFindUser_findByUsername_success() {
        given(userRepository.findByUsername(Mockito.any())).willReturn(testUser);

        User user = new User();
        user.setUsername(testUser.getUsername());

        User foundUser = userService.findUser(user);

        assertEquals(testUser.getId(), foundUser.getId(), "The id does not match");
        assertEquals(testUser.getUsername(), foundUser.getUsername(), "The username does not match");
        assertEquals(testUser.getToken(), foundUser.getToken(), "The token does not match");
    }

    @Test
    void testFindUser_findByToken_success() {
        given(userRepository.findByToken(Mockito.any())).willReturn(testUser);

        User user = new User();
        user.setToken(testUser.getToken());

        User foundUser = userService.findUser(user);

        assertEquals(testUser.getId(), foundUser.getId(), "The id does not match");
        assertEquals(testUser.getUsername(), foundUser.getUsername(), "The username does not match");
        assertEquals(testUser.getToken(), foundUser.getToken(), "The token does not match");
    }

    @Test
    void testFindUser_findById_noUser() {
        given(userRepository.findUserById(Mockito.any())).willReturn(null);

        User user = new User();
        user.setId(testUser.getId());

        User foundUser = userService.findUser(user);

        assertNull(foundUser, "No user should be found");
    }

    @Test
    void testFindUser_findByUsername_noUser() {
        given(userRepository.findByUsername(Mockito.any())).willReturn(null);

        User user = new User();
        user.setUsername(testUser.getUsername());

        User foundUser = userService.findUser(user);

        assertNull(foundUser, "No user should be found");

    }

    @Test
    void testFindUser_findByToken_noUser() {
        given(userRepository.findByToken(Mockito.any())).willReturn(null);

        User user = new User();
        user.setToken(testUser.getToken());

        User foundUser = userService.findUser(user);

        assertNull(foundUser, "No user should be found");

    }

}
