package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;

public class PassMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != PassMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        PassMove passMove = (PassMove) move;

        // pass back to the moveService
        moveService.performPassMove(passMove);
    }
}
