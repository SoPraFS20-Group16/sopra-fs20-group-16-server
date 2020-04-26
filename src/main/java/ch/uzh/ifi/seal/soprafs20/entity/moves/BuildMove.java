package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.standard.BuildMoveHandler;

import javax.persistence.*;

@Entity
@Table(name = "BUILD_MOVE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BuildMove extends Move {

    @OneToOne(cascade = CascadeType.PERSIST)
    private Building building; //Has the coordinates set already

    @Override
    public MoveHandler getMoveHandler() {
        return new BuildMoveHandler();
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
