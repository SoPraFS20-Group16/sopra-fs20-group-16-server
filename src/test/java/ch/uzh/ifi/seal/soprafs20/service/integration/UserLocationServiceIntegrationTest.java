package ch.uzh.ifi.seal.soprafs20.service.integration;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserLocation;
import ch.uzh.ifi.seal.soprafs20.repository.UserLocationRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.service.UserLocationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class UserLocationServiceIntegrationTest {

    private final Long testUserId = 12L;
    private final String testUsername = "TestUsername";
    private final String TEST_IP = "2a02:1206:4544:3000:994:4ed3:5028:ae1f";
    @Qualifier("userRepository")
    @Autowired
    UserRepository userRepository;
    @Qualifier("userLocationRepository")
    @Autowired
    UserLocationRepository userLocationRepository;
    @Autowired
    UserLocationService userLocationService;
    private User testUser;
    private UserLocation testLocation;

    @BeforeEach
    void setup() {

        testUser = new User();
        testUser.setId(testUserId);
        testUser.setUsername(testUsername);
        testUser.setPassword("password");
        testUser.setStatus(UserStatus.ONLINE);
        testUser = userRepository.saveAndFlush(testUser);

        testLocation = new UserLocation();
        testLocation.setUserId(testUserId);
        testLocation.setUserAllowsTracking(true);
        testLocation = userLocationRepository.saveAndFlush(testLocation);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUserLocation_trackingTrue() {

        UserLocation location = userLocationService.createUserLocation(testUserId,
                true, TEST_IP);

        assertNotNull(location, "The location should not be null");
        assertTrue(location.isUserAllowsTracking());
        assertNotNull(location.getCountryName(), "The Country should not be null");
        assertNotNull(location.getCity(), "The city should not be null");
        assertNotNull(location.getZipCode(), "The zipcode should not be null");
        assertNotNull(location.getLongitude(), "The longitude should not be null");
        assertNotNull(location.getLatitude(), "The latitude should not be null");
    }

    @Test
    void testCreateUserLocation_trackingFalse() {
        UserLocation location = userLocationService.createUserLocation(testUserId,
                false, TEST_IP);

        assertNotNull(location, "The location should not be null");
        assertFalse(location.isUserAllowsTracking());
        assertNull(location.getCountryName(), "The Country should be null");
        assertNull(location.getCity(), "The city should be null");
        assertNull(location.getZipCode(), "The zipcode should be null");
        assertNull(location.getLongitude(), "The longitude should be null");
        assertNull(location.getLatitude(), "The latitude should be null");
    }

    @Test
    void testUpdateUserLocation_trackingTrue() {

        UserLocation location = userLocationService.updateUserLocation(TEST_IP, testUserId);

        assertNotNull(location, "The location should not be null");
        assertTrue(location.isUserAllowsTracking());
        assertNotNull(location.getCountryName(), "The Country should not be null");
        assertNotNull(location.getCity(), "The city should not be null");
        assertNotNull(location.getZipCode(), "The zipcode should not be null");
        assertNotNull(location.getLongitude(), "The longitude should not be null");
        assertNotNull(location.getLatitude(), "The latitude should not be null");
    }

    @Test
    void testUpdateUserLocation_trackingFalse() {

        //set userAllows tracking to false
        UserLocation location = userLocationService.findByUserId(testUserId);
        location.setUserAllowsTracking(false);
        userLocationService.save(location);

        location = userLocationService.updateUserLocation(TEST_IP, testUserId);

        assertNotNull(location, "The location should not be null");
        assertFalse(location.isUserAllowsTracking());
        assertNull(location.getCountryName(), "The Country should be null");
        assertNull(location.getCity(), "The city should be null");
        assertNull(location.getZipCode(), "The zipCode should be null");
        assertNull(location.getLongitude(), "The longitude should be null");
        assertNull(location.getLatitude(), "The latitude should be null");
    }
}
