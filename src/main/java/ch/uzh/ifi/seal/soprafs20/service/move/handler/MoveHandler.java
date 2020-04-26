package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;

import java.util.List;

public interface MoveHandler {

    /**
     * Calls the correct method from the MoveService according to the Move subclass it belongs to.
     *
     * @param move        the move
     * @param moveService the service
     */
    void perform(Move move, MoveService moveService);

    default List<Move> calculateNextMoves(Game game, Move move) {
        return MoveCalculator.calculateAllStandardMoves(game);
    }

    default MoveDTO mapToDTO(Move move) {
        return DTOMapper.INSTANCE.convertMoveToMoveDTO(move);
    }
}

