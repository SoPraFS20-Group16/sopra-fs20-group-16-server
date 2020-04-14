package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveService.class);

    private final PlayerService playerService;
    private final MoveCalculator moveCalculator;
    private final BoardService boardService;

    @Autowired
    public MoveService(PlayerService playerService,
                       MoveCalculator moveCalculator,
                       BoardService boardService) {

        this.playerService = playerService;
        this.moveCalculator = moveCalculator;
        this.boardService = boardService;
    }


    /**
     * Gets the passed Move from the moveRepository
     *
     * @param moveId the moveId
     * @return the move
     */
    public Move findMoveById(Long moveId) {
        //TODO: Implement findMove method in MoveService
        //If no move matches the move id return null
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
    public void makeRecalculations(Game game) {

        //TODO: Calculate and set Victory Points for all players

        //TODO: Recalculate Possible moves
        moveCalculator.getAllMovesFor(game);
    }


    /**
     * Performs a BuildMove
     * Is called from the BuildMoveHandler
     *
     * @param buildMove the BuildMove that is passed from the handler
     */
    public void performBuildMove(BuildMove buildMove) {

        //Player must pay for the building
        playerService.payForBuilding(buildMove);

        //Build the building on the board
        boardService.build(buildMove);
    }

    /**
     * Performs a CardMove
     * Is called from the CardMoveHandler
     *
     * @param cardMove the Cardmove that is passed from the handler
     */
    public void performCardMove(CardMove cardMove) {

        // Get the development card from the move
        DevelopmentCard developmentCard = cardMove.getDevelopmentCard();

        // Find the player that has done the move
        Long playerId = cardMove.getUserId();

        // Invoke Card
        switch (developmentCard.getDevelopmentType()) {
            case VICTORYPOINT:
                // TODO: implement functionality
                playerService.addVictoryPoint(playerId);
                break;
            case ROADPROGRESS:
                // TODO: implement functionality
                break;
            case KNIGHT:
                // TODO: implement functionality
                break;
            case PLENTYPROGRESS:
                // TODO: implement functionality
                break;
            case MONOPOLYPROGRESS:
                // TODO: implement functionality
                break;

        }

        // Remove development card from player
        playerService.payDevCard(playerId, developmentCard);
    }

    public List<Move> getMovesForPlayerWithUserId(Long userId) {
        //TODO: Implement functionality
        return new ArrayList<>();
    }
}
