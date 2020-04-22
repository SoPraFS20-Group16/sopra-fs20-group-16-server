package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs20.service.*;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Transactional
public class MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveService.class);

    private final MoveRepository moveRepository;

    private PlayerService playerService;
    private TileService tileService;
    private GameService gameService;
    private BoardService boardService;
    private QueueService queueService;

    @Autowired
    public MoveService(@Qualifier("moveRepository") MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setTileService(TileService tileService) {
        this.tileService = tileService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    @Autowired
    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }


    /**
     * Gets the passed Move from the moveRepository
     *
     * @param moveId the moveId
     * @return the move
     */
    public Move findMoveById(Long moveId) {
        Optional<Move> optionalMove = moveRepository.findById(moveId);

        return optionalMove.orElse(null);
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

        //Get the game
        Game game = gameService.getGameById(move.getGameId());

        //Make the recalculations
        makeRecalculations(game, handler, move);
    }

    //Is performed after performMove terminates
    public void makeRecalculations(Game game, MoveHandler handler, Move move) {

        // update the victory points of the current player
        Player player = game.getCurrentPlayer();

        int devPoints = playerService.getPointsFromDevelopmentCards(player);
        int buildingPoints = boardService.getPointsFromBuildings(game, player);

        int victoryPoints = devPoints + buildingPoints;

        player.setVictoryPoints(victoryPoints);

        //save player
        playerService.save(player);

        //If the player has more 10 or more points, then the game is over
        if (player.getVictoryPoints() >= 10) {
            moveRepository.deleteAll();
            moveRepository.flush();
            return;
        }

        //Calculate all new possible moves and saves them to the move repository
        List<Move> nextMoves = handler.calculateNextMoves(game, move);
        moveRepository.saveAll(nextMoves);
        moveRepository.flush();
    }

    public void makeSetupRecalculations(Game game) {

        // calculate all initial moves
        List<Move> initialMoves = MoveCalculator.calculateAllFirstSettlementMoves(game);
        moveRepository.saveAll(initialMoves);
        moveRepository.flush();
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

        dice1 = ThreadLocalRandom.current().nextInt(min, max);
        dice2 = ThreadLocalRandom.current().nextInt(min, max);

        int diceRoll = dice1 + dice2;

        // get tile(s) with rolled number
        List<Tile> tiles = boardService.getTilesWithNumber(diceMove.getGameId(), diceRoll);

        // update wallet of every player with building on tile
        for (Tile tile : tiles) {

            // get tile type
            TileType tileType = tile.getType();

            // convert into resource
            ResourceType resourceType = tileService.convertToResource(tileType);

            // get playerIDs with settlements on tile & update their wallets
            List<Long> playersWithSettlement = boardService.getPlayerIDsWithSettlement(diceMove.getGameId(), tile);
            playerService.updateWallet(playersWithSettlement, resourceType, new Settlement().getBuildingFactor());

            // get playerIDs with settlements on tile & update their wallets
            List<Long> playersWithCity = boardService.getPlayerIDsWithCity(diceMove.getGameId(), tile);
            playerService.updateWallet(playersWithCity, resourceType, new City().getBuildingFactor());
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

        Game game = gameService.getGameById(passMove.getGameId());

        PlayerQueue queue = queueService.queueForGameWithId(game.getId());

        Player nextPlayer = playerService.findPlayerByUserId(queue.getNextUserId());

        game.setCurrentPlayer(nextPlayer);
    }

    /**
     * Performs a TradeMove
     * Is called from the TradeMoveHandler
     *
     * the player can trade resources to get a designated resource
     *
     * @param tradeMove the TradeMove that is passed from the handler
     */
    public void performTradeMove(TradeMove tradeMove) {

        // player must pay for needed resourceType
        playerService.payForResource(tradeMove);

        // new resource gets added to the players wallet
        playerService.addResource(tradeMove);

    }

    /**
     * Performs a purchaseMove
     * Is called from the PurchaseMoveHandler
     *
     * the player can purchase a (random) development card with resources
     *
     * @param purchaseMove the PurchaseMove that is passed from the handler
     */
    public void performPurchaseMove(PurchaseMove purchaseMove) {

        // player must pay for the development card
        playerService.payForDevelopmentCard(purchaseMove);

        // add the development card to the player
        playerService.addDevelopmentCard(purchaseMove);

    }

    /**
     * Performs a CardMove
     * Is called from the CardMoveHandler
     *
     * the player invokes a development card and the card gets removed from the player
     *
     * @param cardMove the CardMove that is passed from the handler
     */
    public void performCardMove(CardMove cardMove) {

        // Get the development card type from the move
        DevelopmentType type = cardMove.getDevelopmentCard().getDevelopmentType();

        // invoke the card
        // TODO: implement individual functionality of devCards
        if (type == DevelopmentType.VICTORYPOINT) {

            // increase victory points of player
            playerService.addVictoryPoint(cardMove);

        } else if (type == DevelopmentType.KNIGHT) {

            // calculate all possible moves step#1 (robber placement) & set robber on board
            // makeRobberPlacementRecalculations(cardMove.getGameId());
            // boardService.setRobber(KnightMove);

            // calculate all possible moves step#2 (choose player to steal resourceCards)
            // makeKnightChooseVictimRecalculations(cardMove.getGameId());

            // update wallet of players (steal and add)
            // playerService.stealResources(KnightMove);

        } else if (type == DevelopmentType.MONOPOLYPROGRESS) {

            // calculate all possible moves (provide resource options)
            // makeMonopolyRecalculations(cardMove.getGameId());

            //  update wallet of all players (deduct all cards of chosen resource from
            //  opponent players and add them to current player)
            // playerService.monopolizeResources(MonopolyMove);

        } else if (type == DevelopmentType.PLENTYPROGRESS) {

            // calculate all possible moves (provide resource options)
            // makePlentyRecalculations(cardMove.getGameId());

            // update wallet of player with the two chosen resource cards
            // playerService.plentyResources(PlentyMove);

        } else if (type == DevelopmentType.ROADPROGRESS) {

            // calculate all possible moves (provide road #1 building options)
            // makeProgressRecalculations(cardMove.getGameId());

            // build road #1
            // boardService.build(ProgressMove);

            // calculate all possible moves (provide road #2 building options)
            // makeProgressRecalculations(cardMove.getGameId());

            // build road #2
            // boardService.build(ProgressMove);
        } else {
            throw new IllegalStateException(ErrorMsg.UNDEFINED_DEVCARD_TYPE);
        }

        // Remove development card from player
        playerService.removeDevelopmentCard(cardMove);
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

    public List<Move> getMovesForPlayerWithUserId(Long gameId, Long userId) {
        return moveRepository.findAllByGameIdAndUserId(gameId, userId);
    }

    public void performFirstSettlementMove(FirstSettlementMove firstSettlementMove) {
        boardService.build(firstSettlementMove);
    }

    public void performFirstRoadMove(FirstRoadMove firstRoadMove) {
        boardService.build(firstRoadMove);
    }
}
