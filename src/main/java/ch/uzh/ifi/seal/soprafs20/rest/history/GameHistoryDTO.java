package ch.uzh.ifi.seal.soprafs20.rest.history;

import java.util.List;

public class GameHistoryDTO {

    private List<MoveHistoryDTO> moves;

    public List<MoveHistoryDTO> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveHistoryDTO> moves) {
        this.moves = moves;
    }
}
