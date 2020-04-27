package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.standard.PurchaseMoveHandler;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PURCHASE_MOVE")
public class PurchaseMove extends Move {

    @Override
    public MoveHandler getMoveHandler() {
        return new PurchaseMoveHandler();
    }

}
