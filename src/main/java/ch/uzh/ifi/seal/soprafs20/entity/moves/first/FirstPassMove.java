package ch.uzh.ifi.seal.soprafs20.entity.moves.first;

import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.first.FirstPassMoveHandler;

public class FirstPassMove extends Move {

    @Override
    public MoveHandler getMoveHandler() {
        return new FirstPassMoveHandler();
    }
}
