package ch.uzh.ifi.seal.soprafs20.entity.history;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.rest.dto.history.MoveHistoryDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BUILD_MOVE_HISTORY")
public class BuildMoveHistory extends MoveHistory {

    private BuildingType buildingType;

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    @Override
    public MoveHistoryDTO getDTO() {
        return DTOMapper.INSTANCE.convertBuildMoveHistoryToBuildMoveHistoryDTO(this);
    }
}
