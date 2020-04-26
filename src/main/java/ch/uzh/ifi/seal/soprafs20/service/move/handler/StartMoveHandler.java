package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.StartMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;

import java.util.List;

public class StartMoveHandler implements MoveHandler {

    /**
     * Calls the correct method from the MoveService according to the Move subclass it belongs to.
     *
     * @param move        the move
     * @param moveService the service
     */
    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != StartMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        //cast move
        StartMove startMove = (StartMove) move;

        // pass back to moveService
        moveService.performStartMove(startMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {
        return MoveCalculator.calculateFirstSettlementMoves(game);
    }
}
