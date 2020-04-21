package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.FirstMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;

import java.util.List;

public class FirstMoveHandler implements MoveHandler {
    /**
     * Calls the correct method from the MoveService according to the Move subclass it belongs to.
     *
     * @param move        the move
     * @param moveService the service
     */
    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != FirstMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        //cast move
        FirstMove firstMove = (FirstMove) move;

        //pass back to the move service
        moveService.performFirstMove(firstMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game) {

        //TODO: Exit the first move logic after two turns
        return MoveCalculator.getAllFirstMoves();
    }
}
