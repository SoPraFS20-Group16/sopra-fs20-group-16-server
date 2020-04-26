package ch.uzh.ifi.seal.soprafs20.entity.moves.development;

import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.development.KnightMoveHandler;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "KNIGHT_MOVE")
public class KnightMove extends Move {

    @OneToOne
    Tile tile;

    @Override
    public MoveHandler getMoveHandler() {
        return new KnightMoveHandler();
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}