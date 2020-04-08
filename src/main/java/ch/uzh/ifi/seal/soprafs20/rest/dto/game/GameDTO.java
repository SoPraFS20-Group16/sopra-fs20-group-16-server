package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;

import java.util.List;

public class GameDTO {

    private Long gameId;
    private String name;
    private List<UserGetDTO> players;
    private boolean withBots;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserGetDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserGetDTO> players) {
        this.players = players;
    }

    public boolean isWithBots() {
        return withBots;
    }

    public void setWithBots(boolean withBots) {
        this.withBots = withBots;
    }
}
