package ch.uzh.ifi.seal.soprafs20.service.move.handler.first;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstSettlementMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;

public class FirstSettlementMoveHandler implements MoveHandler {
    /**
     * Calls the correct method from the MoveService according to the Move subclass it belongs to.
     *
     * @param move        the move
     * @param moveService the service
     */
    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != FirstSettlementMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        //cast move
        FirstSettlementMove firstSettlementMove = (FirstSettlementMove) move;

        //pass back to the move service
        moveService.performFirstSettlementMove(firstSettlementMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {

        return MoveCalculator.calculateAllFirstRoadMoves(game, (FirstSettlementMove) move);
    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        BuildMove buildMove = (BuildMove) move;

        // map move to DTO
        return DTOMapper.INSTANCE.convertBuildMoveToBuildMoveDTO(buildMove);
    }
}
