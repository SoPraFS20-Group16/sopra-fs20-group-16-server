package ch.uzh.ifi.seal.soprafs20.service.move.handler.development;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.KnightMove;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;

public class KnightMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != KnightMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        KnightMove knightMove = (KnightMove) move;

        // pass back to moveService
        moveService.performKnightMove(knightMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {

        List<Move> possibleStealMoves = MoveCalculator.calculateAllStealMoves(game);
        if (!possibleStealMoves.isEmpty()) {
            return possibleStealMoves;
        } else {
            return MoveCalculator.calculateAllStandardMoves(game);
        }

    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        KnightMove knightMove = (KnightMove) move;

        return DTOMapper.INSTANCE.convertKnightMoveToKnightMoveDTO(knightMove);
    }
}
