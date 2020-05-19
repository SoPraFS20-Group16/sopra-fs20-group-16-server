package ch.uzh.ifi.seal.soprafs20.rest.dto.history;

import ch.uzh.ifi.seal.soprafs20.entity.MoveHistory;

import java.util.ArrayList;
import java.util.List;

public class GameHistoryDTO {

    private List<MoveHistoryDTO> moves;

    public List<MoveHistoryDTO> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveHistory> moveHistories) {
        moves = new ArrayList<>();

        moveHistories.forEach((moveHistory -> moves.add(moveHistory.getDTO())));
    }
}
