package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PurchaseMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;

/**
 * The handler for the PurchaseMove
 */
public class PurchaseMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != PurchaseMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        //Cast move
        PurchaseMove purchaseMove = (PurchaseMove) move;

        //Pass back to the moveService
        moveService.performPurchaseMove(purchaseMove);
    }
}
