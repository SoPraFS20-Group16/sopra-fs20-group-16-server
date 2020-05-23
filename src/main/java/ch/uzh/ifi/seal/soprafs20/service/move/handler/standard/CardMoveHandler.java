package ch.uzh.ifi.seal.soprafs20.service.move.handler.standard;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;

/**
 * The handler for the CardMove
 */
public class CardMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != CardMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        CardMove cardMove = (CardMove) move;

        // cass back to the moveService (removes devCard from player)
        moveService.performCardMove(cardMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {

        // get devCard type
        DevelopmentType type = ((CardMove) move).getDevelopmentCard().getDevelopmentType();

        // calculate next moves considering devCard type
        switch (type) {
            case MONOPOLYPROGRESS:
                return MoveCalculator.calculateAllMonopolyMoves(game);
            case PLENTYPROGRESS:
                return MoveCalculator.calculateAllPlentyMoves(game);
            case ROADPROGRESS:
                List<Move> possibleMoves = MoveCalculator.calculateAllRoadProgressMoves(game, 0);
                if (possibleMoves.isEmpty()) {
                    return MoveCalculator.calculateAllStandardMoves(game);
                } else {
                    return possibleMoves;
                }
            case KNIGHT:
                return MoveCalculator.calculateAllKnightMoves(game);
            default:
                return MoveCalculator.calculateAllStandardMoves(game);
        }
    }


    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        CardMove cardMove = (CardMove) move;

        // map move to DTO
        return DTOMapper.INSTANCE.convertCardMoveToCardMoveDTO(cardMove);
    }

}
