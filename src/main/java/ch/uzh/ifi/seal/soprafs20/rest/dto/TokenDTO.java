package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class TokenDTO {

    private String token;

    TokenDTO() {
    }

    /**
     * The TokenDTO is to wrap the token in an object if it is returned to the client
     *
     * @param token the token
     */
    public TokenDTO(String token) {
        this.token = token;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
