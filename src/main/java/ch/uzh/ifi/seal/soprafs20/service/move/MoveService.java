package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveService.class);

    private final PlayerService playerService;

    @Autowired
    public MoveService(PlayerService playerService) {
        this.playerService = playerService;
    }


    /**
     * Gets the passed Move from the moveRepository
     *
     * @param move the move
     * @return the move
     */
    public Move findMove(Move move) {
        //TODO: Implement findMove method in MoveService
        //If no move matches a given primary key return null
        return null;
    }

    /**
     * Gets the correct move handler form the move
     * passes the move an the MoveService (this) to the handler
     *
     * @param move the move
     */
    public void performMove(Move move) {

        MoveHandler handler = move.getMoveHandler();
        handler.perform(move, this);
    }

    //Is performed after performMove terminates
    public void makeRecalculations() {

        //TODO: Recalculate Vicotory Points


        //TODO: Recalculate Possible moves
    }


    /**
     * Performs a BuildMove
     * Is called from the BuildMoveHandler
     *
     * @param buildMove the BuildMove that is passed from the handler
     */
    public void performBuildMove(BuildMove buildMove) {


        //Get the building from the move
        Building building = buildMove.getBuilding();


        //Find the player that has the move done
        Long playerId = buildMove.getUserId();

        //Give the building to the player (save in the correct array)
        playerService.buildAndPay(playerId, building);
    }
}
