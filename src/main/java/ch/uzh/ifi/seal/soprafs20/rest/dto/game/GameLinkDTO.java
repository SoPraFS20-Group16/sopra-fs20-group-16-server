package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

/**
 * The GameLinkDTO represents an element in the array when a GET request is sent to /games
 * <p>
 */
public class GameLinkDTO {

    private Long gameId;

    private String url;

    private String name;

    private int maxPlayers;

    private int joinedPlayers;

    private boolean withBots;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getJoinedPlayers() {
        return joinedPlayers;
    }

    public void setJoinedPlayers(int joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    public boolean isWithBots() {
        return withBots;
    }

    public void setWithBots(boolean withBots) {
        this.withBots = withBots;
    }
}
