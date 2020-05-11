package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import java.util.List;

public class GameSummaryDTO {

    private Long gameId;

    private List<PlayerSummaryDTO> players;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<PlayerSummaryDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerSummaryDTO> players) {
        this.players = players;
    }
}
