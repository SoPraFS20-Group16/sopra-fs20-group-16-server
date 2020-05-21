package ch.uzh.ifi.seal.soprafs20.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.*;

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
 * <p>
 * The tests are only executed if there is non null IPSTACK_KEY in the environment
 */
@EnabledIfEnvironmentVariable(named = "IPSTACK_KEY", matches = "^(?!\\s*$).+")
class IpStackRequestTest {

    private final String TEST_IP = "2a02:1206:4544:3000:994:4ed3:5028:ae1f";
    private final String FALSE_IP = "abcde";

    private IpStackRequest testRequest;
    private IpStackRequest failedRequest;

    @BeforeEach
    void setup() {

        System.out.println(System.getenv().get("IPSTACK_KEY"));
        testRequest = new IpStackRequest(TEST_IP);
        testRequest.makeRequest();
        assert testRequest.isSuccess();

        failedRequest = new IpStackRequest(FALSE_IP);
        testRequest.makeRequest();
        assert !failedRequest.isSuccess();
    }

    @Test
    void testWithInvalidIp() {
        testRequest = new IpStackRequest("asdf");
        testRequest.makeRequest();

        assertFalse(testRequest.isSuccess());
    }

    @Test
    void testGetZipCode_success() {
        assertNotNull(testRequest.getZipCode());
    }

    @Test
    void testGetCity_success() {
        assertNotNull(testRequest.getCity());
    }

    @Test
    void testGetCountry_success() {
        assertNotNull(testRequest.getCountry());
    }

    @Test
    void testGetLongitude_success() {
        assertNotNull(testRequest.getLongitude());
    }

    @Test
    void testGetLatitude_success() {
        assertNotNull(testRequest.getLatitude());
    }

    @Test
    void testGetZipCode_fail() {
        assertNull(failedRequest.getZipCode());
    }

    @Test
    void testGetCity_fail() {
        assertNull(failedRequest.getCity());
    }

    @Test
    void testGetCountry_fail() {
        assertNull(failedRequest.getCountry());
    }

    @Test
    void testGetLongitude_fail() {
        assertNull(failedRequest.getLongitude());
    }

    @Test
    void testGetLatitude_fail() {
        assertNull(failedRequest.getLatitude());
    }
}
