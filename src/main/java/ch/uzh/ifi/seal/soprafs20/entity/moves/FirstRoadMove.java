package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.FirstRoadMoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FIRST_ROAD_MOVE")
public class FirstRoadMove extends Move {

    @OneToOne(cascade = CascadeType.ALL)
    private Road road;

    @Override
    public MoveHandler getMoveHandler() {
        return new FirstRoadMoveHandler();
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public Road getRoad() {
        return road;
    }
}
