package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

import ch.uzh.ifi.seal.soprafs20.rest.dto.building.BuildingDTO;

public class BuildMoveDTO extends MoveDTO {

    private BuildingDTO building;

    public BuildingDTO getBuilding() {
        return building;
    }

    public void setBuilding(BuildingDTO building) {
        this.building = building;
    }
}
