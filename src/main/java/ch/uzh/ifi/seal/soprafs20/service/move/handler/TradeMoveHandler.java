package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.TradeMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;

public class TradeMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != TradeMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        TradeMove tradeMove = (TradeMove) move;

        // pass back to the moveService
        moveService.performTradeMove(tradeMove);
    }
}
