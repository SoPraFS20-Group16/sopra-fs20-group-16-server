package ch.uzh.ifi.seal.soprafs20.service.move.handler.development;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.StealMove;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

public class StealMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != StealMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        StealMove stealMove = (StealMove) move;

        // pass back to moveService
        moveService.performStealMove(stealMove);
    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        StealMove stealMove = (StealMove) move;

        return DTOMapper.INSTANCE.convertStealMoveToStealMove(stealMove);
    }
}
