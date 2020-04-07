package ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs;

import ch.uzh.ifi.seal.soprafs20.rest.dto.UserDTOs.UserGetDTO;

import java.util.ArrayList;

public class GameDTO {

    private Long gameId;
    private String name;
    private ArrayList<UserGetDTO> players;
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

    public ArrayList<UserGetDTO> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<UserGetDTO> players) {
        this.players = players;
    }

    public boolean isWithBots() {
        return withBots;
    }

    public void setWithBots(boolean withBots) {
        this.withBots = withBots;
    }
}
