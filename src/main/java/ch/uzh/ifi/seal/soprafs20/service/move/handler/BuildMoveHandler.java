package ch.uzh.ifi.seal.soprafs20.service.move.handler;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class BuildMoveHandler extends MoveHandler {

    private final PlayerService playerService;

    @Autowired
    public BuildMoveHandler(PlayerService playerService) {
        this.playerService = playerService;
    }

    public static MoveHandler getInstance() {
        return null;
        //TODO: Return a working instance of the BuildMoveHandler
    }

    @Override
    public void perform(Move move) {

        if (move.getClass() != BuildMove.class) {
            throw new IllegalStateException("The Handler seems to be set up wrong!");
        }

        //Cast move
        BuildMove buildMove = (BuildMove) move;

        //Get the building from the move
        Building building = buildMove.getBuilding();


        //Find the player that has the move done
        Long playerId = buildMove.getUserId();

        //Give the building to the player (save in the correct array)
        playerService.buildAndPay(playerId, building);
    }
}
