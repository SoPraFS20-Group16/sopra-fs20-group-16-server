package ch.uzh.ifi.seal.soprafs20.rest.dto.building;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.CoordinateDTO;

import java.util.List;

public class BuildingDTO {

    private BuildingType buildingType;

    private Long userId;

    private List<CoordinateDTO> coordinates;

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public void setCoordinates(List<CoordinateDTO> coordinates) {
        this.coordinates = coordinates;
    }

    public List<CoordinateDTO> getCoordinates() {
        return coordinates;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
