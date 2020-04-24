package ch.uzh.ifi.seal.soprafs20.entity.moves.development;

import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.development.StealMoveHandler;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STEAL_MOVE")
public class StealMove extends Move {

    @OneToOne
    Player victim;

    @Override
    public MoveHandler getMoveHandler() {
        return new StealMoveHandler();
    }

    public Player getVictim() {
        return victim;
    }

    public void setVictim(Player victim) {
        this.victim = victim;
    }
}
