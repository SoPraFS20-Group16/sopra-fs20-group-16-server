package ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs;

/**
 * The GameLinkDTO represents an element in the array when a GET request is sent to /games
 *
 * TODO: Could be extended with additional information (for Join Game functionality)
 */
public class GameLinkDTO {

    private String id;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
