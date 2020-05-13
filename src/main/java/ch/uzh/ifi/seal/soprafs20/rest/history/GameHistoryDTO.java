package ch.uzh.ifi.seal.soprafs20.rest.history;

import ch.uzh.ifi.seal.soprafs20.entity.MoveHistory;

import java.util.List;

public class GameHistoryDTO {

    private Long gameId;

    private List<MoveHistoryDTO> moves;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<MoveHistoryDTO> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveHistoryDTO> moves) {
        this.moves = moves;
    }
}
