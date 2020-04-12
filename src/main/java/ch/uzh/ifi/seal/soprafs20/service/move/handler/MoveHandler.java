package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;

public abstract class MoveHandler {

    public abstract void perform(Move move);
}
