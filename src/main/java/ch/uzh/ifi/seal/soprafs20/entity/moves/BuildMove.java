package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.BuildMoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BUILD_MOVE")
public class BuildMove extends Move {

    @OneToOne
    private Building building; //Has the coordinates set already


    @Override
    public MoveHandler getMoveHandler() {
        return new BuildMoveHandler();
    }


    public Building getBuilding() {
        return building;
    }
}
