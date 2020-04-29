package ch.uzh.ifi.seal.soprafs20.entity.moves.development;

import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.development.RoadProgressMoveHandler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ROAD_PROGRESS_MOVE")
public class RoadProgressMove extends BuildMove {

    @Column
    private int previousRoadProgressMoves = 0;

    public int getPreviousRoadProgressMoves() {
        return previousRoadProgressMoves;
    }

    public void setPreviousRoadProgressMoves(int previousRoadProgressMoves) {
        this.previousRoadProgressMoves = previousRoadProgressMoves;
    }

    @Override
    public MoveHandler getMoveHandler() {
        return new RoadProgressMoveHandler();
    }
}
