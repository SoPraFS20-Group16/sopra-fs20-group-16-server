package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class TokenDTO {

    private String token;

    /**
     * Instantiates a new TokenDTO with emtpy token field
     */
    public TokenDTO(){

    }

    /**
     * Instantiates a new TokenDTO directly with the token passed to it
     *
     * @param token the token
     */
    public TokenDTO(String token) {
        setToken(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
