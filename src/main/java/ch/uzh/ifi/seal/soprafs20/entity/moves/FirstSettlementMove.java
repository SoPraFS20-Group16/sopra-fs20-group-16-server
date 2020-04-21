package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.FirstSettlementMoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import javax.persistence.OneToOne;

public class FirstSettlementMove extends Move {

    @OneToOne
    private Settlement settlement;

    @Override
    public MoveHandler getMoveHandler() {

        return new FirstSettlementMoveHandler();
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }
}
