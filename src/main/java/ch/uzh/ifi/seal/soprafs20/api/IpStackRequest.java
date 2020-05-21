package ch.uzh.ifi.seal.soprafs20.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class IpStackRequest {

    private static final Logger log = LoggerFactory.getLogger(IpStackRequest.class);

    private final String ipAddress;
    private Map<String, Object> mappedBody = new HashMap<>();
    private boolean success = false;

    public IpStackRequest(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Make request for the ip address given in the constructor
     */
    public void makeRequest() {

        RestTemplate restTemplate = new RestTemplate();

        // check if ipAddress input matches convention
        InetAddress address;

        try {
            //Checks if ipAddress is valid, else throws
            address = InetAddress.getByName(ipAddress);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            this.success = false;
            return;
        }

        String ipstackKey = System.getenv().get("IPSTACK_KEY");

        String url = "http://api.ipstack.com/" + address.getHostAddress() +
                "?access_key=" + ipstackKey;

        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        mapping(response.getBody());
        this.success = true;

    }

    private void mapping(String body) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            this.mappedBody = mapper.readValue(body, new TypeReference<>() {
            });

        }
        catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public boolean isSuccess() {
        return success;
    }

    public Integer getZipCode() {

        Object result = this.mappedBody.get("zip");

        if (result != null) {
            try {
                return Integer.parseInt(result.toString());
            }
            catch (Exception e) {
                log.error("Parsing zip code from ipStack API failed", e);
            }
        }
        return null;
    }

    public String getCity() {

        Object result = this.mappedBody.get("city");

        return result != null ? result.toString() : null;
    }

    public String getCountry() {

        Object result = this.mappedBody.get("country_name");

        return result != null ? result.toString() : null;
    }

    public Float getLongitude() {
        Object longitude = this.mappedBody.get("longitude");

        if (longitude != null) {
            try {
                return Float.parseFloat(longitude.toString());
            }
            catch (Exception e) {
                log.error("Parsing longitude from ipStack API failed", e);
            }
        }
        return null;
    }

    public Float getLatitude() {

        Object latitude = this.mappedBody.get("latitude");

        if (latitude != null) {
            try {
                return Float.parseFloat(latitude.toString());
            }
            catch (Exception e) {
                log.error("Parsing latitude from ipStack API failed", e);
            }
        }
        return null;
    }
}
