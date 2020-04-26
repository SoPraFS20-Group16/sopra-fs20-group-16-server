package ch.uzh.ifi.seal.soprafs20.service.move.handler.standard;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.TradeMove;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

public class TradeMoveHandler implements MoveHandler {

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != TradeMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        // cast move
        TradeMove tradeMove = (TradeMove) move;

        // pass back to the moveService
        moveService.performTradeMove(tradeMove);
    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // map move
        TradeMove tradeMove = (TradeMove) move;

        // map move to DTO
        return DTOMapper.INSTANCE.convertTradeMovetoTradeMoveDTO(tradeMove);
    }
}
