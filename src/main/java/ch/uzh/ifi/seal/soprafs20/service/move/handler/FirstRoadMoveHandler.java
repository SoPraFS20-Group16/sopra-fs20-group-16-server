package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;

import java.util.List;

public class FirstRoadMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != FirstRoadMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        FirstRoadMove firstRoadMove = (FirstRoadMove) move;

        // pass back to moveService
        moveService.performFirstRoadMove(firstRoadMove);

    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {
        return MoveCalculator.calculatePassMove(game);
    }
}
