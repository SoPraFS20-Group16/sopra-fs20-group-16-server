package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.FirstSettlementMoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FIRST_SETTLEMENT_MOVE")
public class FirstSettlementMove extends Move {

    @OneToOne(cascade = CascadeType.ALL)
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
