package ch.uzh.ifi.seal.soprafs20.service.move.handler.development;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.PlentyMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

/**
 * The Handler for the Plenty Move
 */
public class PlentyMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != PlentyMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        PlentyMove plentyMove = (PlentyMove) move;

        // pass back to moveService
        moveService.performPlentyMove(plentyMove);

    }
}
