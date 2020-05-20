package ch.uzh.ifi.seal.soprafs20.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test class assumes that the correct input will return a correct
 * response from ipstack.com
 * <p>
 * Should this not be the case, then the further test will not be able to
 * get a correct response and thus fail. Therefore the test cases implicitly
 * test the request functionality
 * <p>
 * Tested are the individual fields in order to verify that the mapping
 * between the api response and the concrete fields works.
 */
class IpStackRequestTest {

    private final String TEST_IP = "2a02:1206:4544:3000:994:4ed3:5028:ae1f";

    private IpStackRequest testRequest;

    @BeforeEach
    void setup() {
        testRequest = new IpStackRequest(TEST_IP);
        testRequest.makeRequest();
        assert testRequest.isSuccess();
    }

    @Test
    void testWithInvalidIp() {
        testRequest = new IpStackRequest("asdf");
        testRequest.makeRequest();

        assertFalse(testRequest.isSuccess());
    }

    @Test
    void testGetZipCode() {
        assertNotNull(testRequest.getZipCode());
    }

    @Test
    void testGetCity() {
        assertNotNull(testRequest.getCity());
    }

    @Test
    void testGetCountry() {
        assertNotNull(testRequest.getCountry());
    }

    @Test
    void testGetLongitude() {
        assertNotNull(testRequest.getLongitude());
    }

    @Test
    void testGetLatitude() {
        assertNotNull(testRequest.getLatitude());
    }
}
