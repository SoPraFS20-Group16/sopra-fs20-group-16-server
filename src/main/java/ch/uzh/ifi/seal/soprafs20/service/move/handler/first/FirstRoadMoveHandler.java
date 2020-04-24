package ch.uzh.ifi.seal.soprafs20.service.move.handler.first;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

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
        return MoveCalculator.calculateFirstPassMove(game);
    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        BuildMove buildMove = (BuildMove) move;

        // map move to DTO
        return DTOMapper.INSTANCE.convertBuildMoveToBuildMoveDTO(buildMove);
    }
}
