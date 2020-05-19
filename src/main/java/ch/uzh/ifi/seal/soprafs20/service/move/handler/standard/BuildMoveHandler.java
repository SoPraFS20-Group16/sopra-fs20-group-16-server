package ch.uzh.ifi.seal.soprafs20.service.move.handler.standard;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.history.BuildMoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.MoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

/**
 * The Handler for the BuildMove
 */
public class BuildMoveHandler implements MoveHandler {

    private BuildingType buildingType;

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != BuildMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        //Cast move
        BuildMove buildMove = (BuildMove) move;

        //set buildingType
        buildingType = buildMove.getBuilding().getType();

        //Pass back to the moveService
        moveService.performBuildMove(buildMove);
    }

    @Override
    public MoveDTO mapToDTO(Move move) {

        // cast move
        BuildMove buildMove = (BuildMove) move;

        // map move to DTO
        return DTOMapper.INSTANCE.convertBuildMoveToBuildMoveDTO(buildMove);
    }

    @Override
    public MoveHistory getHistory() {
        BuildMoveHistory history = new BuildMoveHistory();
        history.setBuildingType(buildingType);
        return history;
    }
}
