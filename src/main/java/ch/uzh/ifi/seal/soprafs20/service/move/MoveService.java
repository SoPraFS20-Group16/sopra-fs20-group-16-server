package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstPassMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstSettlementMove;
import ch.uzh.ifi.seal.soprafs20.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs20.service.FirstStackService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.QueueService;
import ch.uzh.ifi.seal.soprafs20.service.board.BoardService;
import ch.uzh.ifi.seal.soprafs20.service.board.TileService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


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
    private FirstStackService firstStackService;

    @Autowired
    public MoveService(@Qualifier("moveRepository") MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Autowired
    public void setFirstStackService(FirstStackService firstStackService) {
        this.firstStackService = firstStackService;
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
     * Gets the correct move handler form the move
     * passes the move an the MoveService (this) to the handler
     *
     * @param move the move
     */
    public void performMove(Move move) {

        MoveHandler handler = move.getMoveHandler();
        handler.perform(move, this);

        log.debug("passed handler");

        // delete all recent moves of the game
        deleteAllMovesForGame(move.getGameId());

        //Get the game
        Game game = gameService.findGameById(move.getGameId());

        //Make the recalculations
        makeRecalculations(game, handler, move);
    }

    // is performed after performMove terminates
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
            deleteAllMovesForGame(game.getId());
            return;
        }

        //Calculate all new possible moves and saves them to the move repository
        List<Move> nextMoves = handler.calculateNextMoves(game, move);

        //Save the game (necessary to do here because of first part ->
        // randomly select first player in FirstPassMoveHandler)
        gameService.save(game);

        moveRepository.saveAll(nextMoves);
        moveRepository.flush();
    }

    // -- helper methods --

    public boolean canExitFirstPart(Long gameId) {

        Game game = gameService.findGameById(gameId);

        int numberOfPlayers = game.getPlayers().size();
        int numberOfRoads = game.getBoard().getRoads().size();

        return (numberOfRoads / GameConstants.NUMBER_OF_FIRST_ROUNDS) == numberOfPlayers;
    }

    public List<Move> findMovesForGameAndPlayer(Long gameId, Long userId) {
        return moveRepository.findAllByGameIdAndUserId(gameId, userId);
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

    public void makeSetupRecalculations(Game game) {

        //Calculate the first move
        if (game.getPlayers().size() >= game.getPlayerMinimum()) {
            List<Move> startMoves = MoveCalculator.calculateStartMove(game);
            moveRepository.saveAll(startMoves);
            moveRepository.flush();
        }
    }

    public void deleteAllMovesForGame(Long gameId) {
        List<Move> expiredMoves = moveRepository.findAllByGameId(gameId);
        moveRepository.deleteAll(expiredMoves);
        moveRepository.flush();
    }

    // -- start move(s) --

    public void performStartMove(StartMove startMove) {

        firstStackService.createStackForGameWithId(startMove.getGameId());
    }

    // -- initial moves --

    public void performFirstPassMove(FirstPassMove firstPassMove) {
        Game game = gameService.findGameById(firstPassMove.getGameId());

        //Find the userId for the next player in the game!
        Long nextUserId = firstStackService.getNextPlayerInGame(game.getId());

        Player nextPlayer = playerService.findPlayerByUserId(nextUserId);

        game.setCurrentPlayer(nextPlayer);

        gameService.save(game);
    }

    public void performFirstSettlementMove(FirstSettlementMove firstSettlementMove) {
        boardService.build(firstSettlementMove);
    }

    public void performFirstRoadMove(FirstRoadMove firstRoadMove) {
        boardService.build(firstRoadMove);
    }

    // -- standard moves --

    // - default moves -

    /**
     * Performs a DiceMove
     * Is called from the DiceMoveHandler
     * <p>
     * imitates dice roll and distributes resources to designated players
     *
     * @param diceMove the DiceMove that is passed to the handler
     */
    public void performDiceMove(DiceMove diceMove, int diceRoll) {

        // get tile(s) with rolled number
        List<Tile> tiles = boardService.getTilesWithNumber(diceMove.getGameId(), diceRoll);

        // get game
        Game game = gameService.findGameById(diceMove.getGameId());

        // update wallet of every player with buildings on tile
        for (Tile tile : tiles) {
            ResourceType type = tileService.convertToResource(tile.getType());
            for (Player player : game.getPlayers()) {
                List<Building> buildings = boardService.getBuildingsFromTileForPlayer(game, tile, player);
                playerService.updateResources(type, buildings, player);
            }
        }
    }

    /**
     * Performs a PassMove
     * Is called from the PassMoveHandler
     * <p>
     * will set the next player to the current player (current player passes to make another move)
     *
     * @param passMove the PassMove that is passed from the handler
     */
    public void performPassMove(PassMove passMove) {

        Game game = gameService.findGameById(passMove.getGameId());

        Long queueReturn = queueService.getNextForGame(game.getId());

        Player nextPlayer = playerService.findPlayerByUserId(queueReturn);

        game.setCurrentPlayer(nextPlayer);

        gameService.save(game);
    }

    // - build moves -

    /**
     * Performs a BuildMove
     * Is called from the BuildMoveHandler
     * <p>
     * the player pays for and builds a building (road, settlement or city)
     *
     * @param buildMove the BuildMove that is passed from the handler
     */
    public void performBuildMove(BuildMove buildMove) {

        //Player must pay for the building
        playerService.payForBuilding(buildMove);

        if (buildMove.getBuilding().getClass() == City.class) {
            boardService.removeSettlementForCity(buildMove);
        }

        //Build the building on the board
        boardService.build(buildMove);
    }

    // - card moves -

    // removes invoked development card from player
    public void performCardMove(CardMove cardMove) {

        // get the development card type from the move
        DevelopmentType type = cardMove.getDevelopmentCard().getDevelopmentType();

        // remove development card from player, if it's not a victoryPoint card
        if (type != DevelopmentType.VICTORYPOINT) {
            playerService.removeDevelopmentCard(cardMove);
        }
    }

    // - other moves -

    /**
     * Performs a TradeMove
     * Is called from the TradeMoveHandler
     * <p>
     * the player can trade resources to get a designated resource
     *
     * @param tradeMove the TradeMove that is passed from the handler
     */
    public void performTradeMove(TradeMove tradeMove) {

        // player must pay for needed resourceType
        playerService.payForTrade(tradeMove);

        // new resource gets added to the players wallet
        playerService.receiveFromTrade(tradeMove);

    }

    /**
     * Performs a purchaseMove
     * Is called from the PurchaseMoveHandler
     * <p>
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

    // -- development card moves --

    // performs monopoly move
    public void performMonopolyMove(MonopolyMove monopolyMove) {

        // get current game
        Game game = gameService.findGameById(monopolyMove.getGameId());

        // get player that monopolizes and opponents
        Player player = game.getCurrentPlayer();
        List<Player> opponents = game.getPlayers();
        opponents.remove(player);

        playerService.monopolizeResources(monopolyMove, player, opponents);
    }

    // performs plenty move
    public void performPlentyMove(PlentyMove plentyMove) {

        playerService.plentyResources(plentyMove);
    }

    // performs roadProgress move
    public void performRoadProgressMove(RoadProgressMove roadProgressMove) {

        // since the player does not have to pay for road, it gets directly build
        boardService.build(roadProgressMove);
    }

    // performs knight move (relocates the robber on board)
    public void performKnightMove(KnightMove knightMove) {

        // get current board
        Board board = gameService.findGameById(knightMove.getGameId()).getBoard();

        // get tileId where robber will be placed
        Long tileId = knightMove.getTileId();

        // set robber on board
        tileService.setRobber(tileId, board);
    }

    // performs stealing move (usually invoked after knight move)
    public void performStealMove(StealMove stealMove) {

        // deduct a random resource from victim and add it to player wallet
        playerService.stealResource(stealMove);
    }
}
