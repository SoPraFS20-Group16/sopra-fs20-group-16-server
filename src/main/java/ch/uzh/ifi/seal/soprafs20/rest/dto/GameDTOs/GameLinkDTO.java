package ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs;

/**
 * The GameLinkDTO represents an element in the array when a GET request is sent to /games
 * <p>
 * TODO: GameLinkDTO could be extended with additional information (for Join Game functionality)
 */
public class GameLinkDTO {

    private Long gameId;
    private String url;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(Long gameId) {
        this.url = String.format("/games/%d", gameId);
    }
}
