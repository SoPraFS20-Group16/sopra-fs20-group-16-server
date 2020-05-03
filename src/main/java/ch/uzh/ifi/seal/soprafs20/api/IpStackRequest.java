package ch.uzh.ifi.seal.soprafs20.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class IpStackRequest {

    private static final Logger log = LoggerFactory.getLogger(IpStackRequest.class);

    private static final String API_KEY = "e0cb9e120039bf799a32662c58b9b5e0";

    private final String ipAddress;
    private Map<String, Object> mappedBody;

    public IpStackRequest(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void makeRequest() {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://api.ipstack.com/" + ipAddress +
                "?access_key=" + API_KEY;

        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        System.out.println(response);
        mapping(response.getBody());
    }

    private void mapping(String body) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            this.mappedBody = mapper.readValue(body, new TypeReference<Map<String, Object>>() {});

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public String getZipCode() {

        Object result = this.mappedBody.get("zip");

        if (result != null) {
            return result.toString();
        } else {
            return null;
        }
    }

    public String getCity() {

        Object result = this.mappedBody.get("city");

        if (result != null) {
            return result.toString();
        } else {
            return null;
        }
    }

    public String getCountry() {

        Object result = this.mappedBody.get("country_name");

        if (result != null) {
            return result.toString();
        } else {
            return null;
        }
    }

}
