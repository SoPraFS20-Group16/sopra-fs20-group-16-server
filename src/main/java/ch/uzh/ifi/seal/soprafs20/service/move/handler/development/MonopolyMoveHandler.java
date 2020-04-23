package ch.uzh.ifi.seal.soprafs20.service.move.handler.development;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.MonopolyMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;

/**
 * The Handler for the Monopoly move
 */
public class MonopolyMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if(move.getClass() != MonopolyMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        MonopolyMove monopolyMove = (MonopolyMove) move;

        // send back to moveService
        moveService.performMonopolyMove(monopolyMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {
        return MoveCalculator.calculateAllStandardMoves(game);
    }
}
