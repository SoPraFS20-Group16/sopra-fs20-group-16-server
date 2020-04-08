package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);

        // verify the uniqueness of the newUser credentials
        checkIfUserAlreadyExists(newUser);

        // check for empty words
        checkEmptyWords(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * Finds a user in the database.
     *
     * @param user the User for which the database is searched. Needs to contain a primary key
     * @return the user
     */
    public User findUser(User user) {


        //Should be able to find the user given any of its unique fields
        //At the moment of writing those are id, username and token
        //if the user cannot be found return null

        User foundUser = new User();

        if (user.getId() != null) {
            foundUser = userRepository.findUserById(user.getId());
        } else if (user.getUsername() != null) {
            foundUser = userRepository.findByUsername(user.getUsername());
        } else if (user.getToken() != null) {
            foundUser = userRepository.findByToken(user.getToken());
        }

        return foundUser;

    }

    public User loginUser(User userToBeLoggedIn) {

        // get user by username
        User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());

        // if user exists, get the corresponding password
        String password = null;
        if (userByUsername != null) {
            password = userByUsername.getPassword();
        }

        // get input password for comparison
        String passwordInput = userToBeLoggedIn.getPassword();

        //If it doesn't exist return null

        // verifies the user by its username and password
        if (userByUsername != null && !password.equals(passwordInput)) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "invalid password, try again");
        }
        else if (userByUsername == null) {
            return null;
        }

        // after verification, set userStatus accordingly
        userByUsername.setStatus(UserStatus.ONLINE);

        //return logged in user
        return userByUsername;

    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated the user that should be added to the database
     * @throws RestException throws if the username is set to be an empty word
     * @see User
     */
    private void checkIfUserAlreadyExists(User userToBeCreated) {

        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";

        if (userByUsername != null) {
            throw new RestException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }

    private void checkEmptyWords(User userToBeCreated) {

        if (userToBeCreated.getUsername().trim().length() == 0 ||
            userToBeCreated.getPassword().trim().length() == 0) {
            throw new RestException(HttpStatus.CONFLICT, "empty words are not allowed");
        }
    }


    public void logoutUser(User userToBeLoggedOut) {
        // find user by its username
        User userByUsername = userRepository.findByUsername(userToBeLoggedOut.getUsername());

        // set status accordingly
        userByUsername.setStatus(UserStatus.OFFLINE);
    }
}