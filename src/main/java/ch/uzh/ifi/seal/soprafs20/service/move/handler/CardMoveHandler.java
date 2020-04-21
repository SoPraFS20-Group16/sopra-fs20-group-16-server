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

        // TODO: add logic for subsequent moves (either here or in performCardMove method)
        // 1)   invoke knight card
        // 2)   calculate possible moves (placements of knight on board)
        // 3)   return options to player
        // 4)   receive new instructions & perform according move
        // 5)   recalculate possible moves (from which player deduct cards)
        // 6)   return options to player
        // 7)   receive new instructions & perform according move
        // 8)   terminate move and call getAllPossibleMoves
    }
}
