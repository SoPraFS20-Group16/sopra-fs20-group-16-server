package ch.uzh.ifi.seal.soprafs20.service.move.handler.development;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.RoadProgressMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;

public class RoadProgressMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if(move.getClass() != RoadProgressMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        RoadProgressMove roadProgressMove = (RoadProgressMove) move;

        // pass back to moveService
        moveService.performRoadProgressMove(roadProgressMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {
        // TODO: implement condition so that two roads can get build before all standard moves get returned
        return MoveCalculator.calculateAllStandardMoves(game);
    }
}
