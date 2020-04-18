package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.service.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.TileService;
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
    private final TileService tileService;

    @Autowired
    public MoveService(PlayerService playerService,
                       MoveCalculator moveCalculator,
                       BoardService boardService,
                       TileService tileService) {

        this.playerService = playerService;
        this.moveCalculator = moveCalculator;
        this.boardService = boardService;
        this.tileService = tileService;
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

        // update the victory points of the current player
        Player player = game.getCurrentPlayer();

        int devPoints = playerService.getPointsFromDevelopmentCards(player);
        int buildingPoints = boardService.getPointsFromBuildings(game, player);

        int victoryPoints = devPoints + buildingPoints;

        player.setVictoryPoints(victoryPoints);

        // TODO: add logic that ends the game if victoryPoints >= 10

        // TODO: Recalculate Possible moves
    }

    /**
     * Performs a DiceMove
     * Is called from the DiceMoveHandler
     *
     * imitates dice roll and distributes resources to designated players
     *
     * @param diceMove the DiceMove that is passed to the handler
     */
    public void performDiceMove(DiceMove diceMove) {

        // roll dice1 & dice2
        int min = 1;    // inclusive
        int max = 7;    // exclusive

        int dice1;
        int dice2;

        dice1 = (int) Math.random() * (max - min + 1) + min;
        dice2 = (int) Math.random() * (max - min + 1) + min;

        int diceRoll = dice1 + dice2;

        // get tile(s) with rolled number
        List<Tile> tiles = boardService.getTiles(diceMove, diceRoll);

        // update wallet of every player with building on tile
        for (Tile tile: tiles) {

            // get tile type
            TileType tileType = tile.getType();

            // convert into resource
            ResourceType resourceType = tileService.convertToResource(tileType);

            // get playerIDs with settlements on tile & update their wallets
            List<Long> playersWithSettlement = boardService.getPlayerIDsWithSettlement(diceMove, tile);
            playerService.updateWallet(playersWithSettlement, resourceType, 1);

            // get playerIDs with settlements on tile & update their wallets
            List<Long> playersWithCity = boardService.getPlayerIDsWithCity(diceMove, tile);
            playerService.updateWallet(playersWithCity, resourceType, 2);
        }
    }

    /**
     * Performs a PassMove
     * Is called from the PassMoveHandler
     *
     * will set the next player to the current player (current player passes to make another move)
     *
     * @param passMove the PassMove that is passed from the handler
     */
    public void performPassMove(PassMove passMove) {

        // TODO: implement functionality
    }

    /**
     * Performs a TradeMove
     * Is called from the TradeMoveHandler
     *
     * the player can trade resources to get a development card or a designated resource
     *
     * @param tradeMove the TradeMove that is passed from the handler
     */
    public void performTradeMove(TradeMove tradeMove) {

        // get wished development card
        DevelopmentCard card = tradeMove.getDevelopmentCard();

        if (card != null) {
            // player must pay for the development card
            playerService.payForDevelopmentCard(tradeMove);

            // add the development card to the player
            playerService.addDevelopmentCard(tradeMove);

        } else {
            // player must pay for needed resourceType
            playerService.payForResource(tradeMove);

            // new resource gets added to the players wallet
            playerService.addResource(tradeMove);
        }
    }

    /**
     * Performs a BuildMove
     * Is called from the BuildMoveHandler
     *
     * the player pays for and builds a building (road, settlement or city)
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
     * the player invokes a development card and the card gets removed from the player
     *
     * @param cardMove the Cardmove that is passed from the handler
     */
    public void performCardMove(CardMove cardMove) {

        // Get the development card from the move
        DevelopmentCard developmentCard = cardMove.getDevelopmentCard();

        // Find the player that has done the move
        Long playerId = cardMove.getUserId();

        // Invoke Card
        // TODO: implement functionality
        switch (developmentCard.getDevelopmentType()) {
            case VICTORYPOINT:
                //playerService.addVictoryPoint(playerId);
                break;
            case ROADPROGRESS:
            case KNIGHT:
            case PLENTYPROGRESS:
            case MONOPOLYPROGRESS:
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
