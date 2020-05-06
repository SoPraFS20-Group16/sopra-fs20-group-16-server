package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import java.util.List;

public class GameSummaryDTO {

    private Long gameId;

    private String winner;

    private List<String> players;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

}
