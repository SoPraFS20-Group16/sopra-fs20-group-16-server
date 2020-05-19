package ch.uzh.ifi.seal.soprafs20.rest.dto.history;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;

public class BuildMoveHistoryDTO extends MoveHistoryDTO {

    private BuildingType buildingType;

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
}
