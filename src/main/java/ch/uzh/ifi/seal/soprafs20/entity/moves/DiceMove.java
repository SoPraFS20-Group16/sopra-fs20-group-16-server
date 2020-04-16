package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.DiceMoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DICE_MOVE")
public class DiceMove extends Move {

    @Override
    public MoveHandler getMoveHandler() {

        return new DiceMoveHandler();
    }
}
