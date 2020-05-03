package ch.uzh.ifi.seal.soprafs20.api;

import ch.uzh.ifi.seal.soprafs20.constant.ApiConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.net.Inet4Address;
import java.net.Inet6Address;

import java.net.InetAddress;
import java.util.Map;

public class IpStackRequest {

    private static final Logger log = LoggerFactory.getLogger(IpStackRequest.class);

    private final String ipAddress;
    private Map<String, Object> mappedBody;
    private boolean success = false;

    public IpStackRequest(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void makeRequest() {

        RestTemplate restTemplate = new RestTemplate();

        // check if ipAddress input matches convention
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            if (!(address instanceof Inet4Address) && !(address instanceof Inet6Address)) {
                this.success = false;
                return;
            }
        } catch(Exception e) {
            log.error(e.getMessage());
            this.success = false;
            return;
        }

        String url = "http://api.ipstack.com/" + ipAddress +
                "?access_key=" + ApiConstants.API_KEY;

        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        mapping(response.getBody());
        this.success = true;

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

    public boolean isSuccess() {
        return success;
    }

}
