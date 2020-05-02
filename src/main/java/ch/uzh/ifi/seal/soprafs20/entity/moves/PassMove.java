package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.standard.PassMoveHandler;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "PASS_MOVE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class PassMove extends Move {

    @Override
    public MoveHandler getMoveHandler() {
        return new PassMoveHandler();
    }
}
