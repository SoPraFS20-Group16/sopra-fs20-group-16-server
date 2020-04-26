package ch.uzh.ifi.seal.soprafs20.service.move.handler.standard;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PurchaseMove;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;

/**
 * The handler for the PurchaseMove
 */
public class PurchaseMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != PurchaseMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        //Cast move
        PurchaseMove purchaseMove = (PurchaseMove) move;

        //Pass back to the moveService
        moveService.performPurchaseMove(purchaseMove);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {

        // it's not allowed to invoke a development card in the same move it got purchased
        return MoveCalculator.calculateAllStandardMovesExclusiveDevCard(game);
    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        PurchaseMove purchaseMove = (PurchaseMove) move;

        // map move to DTO
        return DTOMapper.INSTANCE.convertPurchaseMoveToPurchaseMoveDTO(purchaseMove);
    }
}
