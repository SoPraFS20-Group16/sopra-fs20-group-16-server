package ch.uzh.ifi.seal.soprafs20.service.move.handler.development;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.RoadProgressMove;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;

public class RoadProgressMoveHandler implements MoveHandler {


    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != RoadProgressMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        BuildMove buildMove = (BuildMove) move;

        // pass back to moveService
        moveService.performRoadProgressMove(buildMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {

        int previousRoadProgressMoves = ((RoadProgressMove) move).getPreviousRoadProgressMoves();

        if (previousRoadProgressMoves < GameConstants.NUMBER_OF_ROADS_ROAD_PROGRESS) {
            return MoveCalculator.calculateAllRoadProgressMoves(game, previousRoadProgressMoves);
        }

        return MoveCalculator.calculateAllStandardMoves(game);
    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        BuildMove buildMove = (BuildMove) move;

        return DTOMapper.INSTANCE.convertBuildMoveToBuildMoveDTO(buildMove);
    }
}
