package ch.uzh.ifi.seal.soprafs20.entity.moves.initial;

import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.initial.FirstRoadMoveHandler;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FIRST_ROAD_MOVE")
public class FirstRoadMove extends BuildMove {

    @Override
    public MoveHandler getMoveHandler() {
        return new FirstRoadMoveHandler();
    }
}
