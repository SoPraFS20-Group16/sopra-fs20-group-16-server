package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;

/**
 * The handler for the CardMove
 */
public class CardMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != CardMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // Cast move
        CardMove cardMove = (CardMove) move;

        // Pass back to the moveService
        moveService.performCardMove(cardMove);
    }
}
