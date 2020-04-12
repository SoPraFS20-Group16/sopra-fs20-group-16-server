package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

public class PostMove extends Move {
    @Override
    public MoveHandler getMoveHandler() {
        throw new IllegalStateException("The PostMove never has a handler!");
    }
}
