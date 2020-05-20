package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.KnightMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.MonopolyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.PlentyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.StealMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstPassMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstSettlementMove;
import ch.uzh.ifi.seal.soprafs20.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs20.service.*;
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
    private BotService botService;
    private HistoryService historyService;

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

    @Autowired
    public void setBotService(BotService botService) {
        this.botService = botService;
    }

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
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

        //add move to history
        historyService.addMoveToHistory(move, handler);

        // delete all recent moves of the game
        deleteAllMovesForGame(move.getGameId());

        //Get the game
        Game game = gameService.findGameById(move.getGameId());

        //Make the recalculations
        makeRecalculations(game, handler, move);
    }

    /**
     * Recalculates the possible next moves.
     * Called after the given move was performed.
     * The passed handler may contain information about the possible next moves
     * (A counter for example)
     * <p>
     * This method is deprecated because relying on information in the handler is error prone.
     * It should only be relied on information that was saved to a repository and not
     * in a service like worker
     *
     * @param game    the game
     * @param handler the handler
     * @param move    the move
     */
    @Deprecated
    public void makeRecalculations(Game game, MoveHandler handler, Move move) {

        //Calculate current players points
        Player player = updateVictoryPoints(game);

        //If the player has more 10 or more points, then the game is over
        if (player.getVictoryPoints() >= GameConstants.WIN_POINTS) {
            deleteAllMovesForGame(game.getId());
            gameService.teardownGameWithId(game.getId());
            return;
        }

        //Calculate all new possible moves and saves them to the move repository
        List<Move> nextMoves = handler.calculateNextMoves(game, move);

        //Save the game (necessary to do here because of first part ->
        // randomly select first player in FirstPassMoveHandler)
        gameService.save(game);

        moveRepository.saveAll(nextMoves);
        moveRepository.flush();

        //Game awaits new put request or bot is notified
        notifyBotIfNeeded(game);
    }

    /**
     * Helper method that takes a game and recalculates the current players victory points
     * after a move was executed.
     *
     * @param game the game for which the current players victory points should be recalculated
     * @return the current player of the game
     */
    private Player updateVictoryPoints(Game game) {
        // update the victory points of the current player
        Player player = game.getCurrentPlayer();

        int devPoints = playerService.getPointsFromDevelopmentCards(player);
        int buildingPoints = boardService.getPointsFromBuildings(game, player);

        int victoryPoints = devPoints + buildingPoints;

        player.setVictoryPoints(victoryPoints);

        //save player
        playerService.save(player);
        return player;
    }

    // -- helper methods --

    /**
     * Delete all moves for the given game
     * <p>
     * Is used to remove expired moves after a move was executed
     *
     * @param gameId the games id
     */
    public void deleteAllMovesForGame(Long gameId) {
        List<Move> expiredMoves = moveRepository.findAllByGameId(gameId);
        moveRepository.deleteAll(expiredMoves);
        moveRepository.flush();
    }

    /**
     * Sees if the current player of a game is a bot and if so asks the botService
     * to perform the next move.
     *
     * @param game the game
     */
    private void notifyBotIfNeeded(Game game) {

        Player currentPlayer = game.getCurrentPlayer();

        if (currentPlayer.isBot()) {
            botService.performBotMove(game.getId(), currentPlayer.getUserId());
        }
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
     * Checks if the game can exit the firstPart subroutine by checking if every player
     * built the required amount of roads.
     *
     * @param gameId the game id
     * @return boolean indicating if first part can be exited
     */
    public boolean canExitFirstPart(Long gameId) {

        Game game = gameService.findGameById(gameId);

        int numberOfPlayers = game.getPlayers().size();
        int numberOfRoads = game.getBoard().getRoads().size();

        return (numberOfRoads / GameConstants.NUMBER_OF_FIRST_ROUNDS) == numberOfPlayers;
    }

    /**
     * Finds all the moves that a given player can make in a game.
     *
     * @param gameId the games id
     * @param userId the users id
     * @return the list of moves
     */
    public List<Move> findMovesForGameAndPlayer(Long gameId, Long userId) {
        return moveRepository.findAllByGameIdAndUserId(gameId, userId);
    }

    // -- start move(s) --

    /**
     * Checks for the game if the start move can be made available
     *
     * @param game the game
     */
    public void makeSetupRecalculations(Game game) {

        //Calculate the first move
        if (game.getPlayers().size() >= game.getPlayerMinimum() || game.isWithBots()) {
            List<Move> startMoves = MoveCalculator.calculateStartMove(game);
            moveRepository.saveAll(startMoves);
            moveRepository.flush();
        }
    }

    // -- initial moves --

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param startMove the start move
     */
    public void performStartMove(StartMove startMove) {

        Game startedGame = gameService.findGameById(startMove.getGameId());

        //Add bots if needed
        if (startedGame.isWithBots()) {
            gameService.fillWithBots(startedGame);
        }

        // create stack
        firstStackService.createStackForGameWithId(startMove.getGameId());

        // start and save started game
        startedGame.setStarted(true);
        gameService.save(startedGame);
    }

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param firstPassMove the first pass move
     */
    public void performFirstPassMove(FirstPassMove firstPassMove) {
        Game game = gameService.findGameById(firstPassMove.getGameId());

        //Find the userId for the next player in the game!
        Long nextUserId = firstStackService.getNextPlayerInGame(game.getId());

        Player nextPlayer = playerService.findPlayerByUserId(nextUserId);

        game.setCurrentPlayer(nextPlayer);

        gameService.save(game);
    }

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param move the move
     */
    public void performFirstSettlementMove(FirstSettlementMove move) {

        // build settlement
        boardService.build(move);

        // get adjacent tiles
        List<Tile> tiles = boardService.getTilesWithBuilding(move.getGameId(),
                move.getBuilding());

        // if tile is not a desert (produces no resource), update wallet of player
        for (Tile tile : tiles) {
            if (tile.getType() != TileType.DESERT) {
                ResourceType type = tileService.convertToResource(tile.getType());
                playerService.receiveInitialResources(type, move.getUserId(), move.getBuilding());
            }
        }

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

        //Set last dice roll
        game.setLastDiceRoll(diceRoll);
        gameService.save(game);

        // update wallet of every player with buildings on tile, skipped when empty (7)
        for (Tile tile : tiles) {
            if (!tile.isRobber()) {
                ResourceType type = tileService.convertToResource(tile.getType());
                for (Player player : game.getPlayers()) {
                    List<Building> buildings = boardService.getBuildingsFromTileForPlayer(game, tile, player);
                    playerService.updateResources(type, buildings, player);
                }
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

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param firstRoadMove the first road move
     */
    public void performFirstRoadMove(FirstRoadMove firstRoadMove) {
        boardService.build(firstRoadMove);
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

    /**
     * Is called by the move handler to execute move specific routine.
     * <p>
     * removes invoked development card from player
     *
     * @param cardMove the card move
     */
    public void performCardMove(CardMove cardMove) {

        // get the development card type from the move
        DevelopmentType type = cardMove.getDevelopmentCard().getDevelopmentType();

        // remove development card from player, if it's not a victoryPoint card
        if (type != DevelopmentType.VICTORYPOINT) {
            playerService.removeDevelopmentCard(cardMove);
        }
    }

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param monopolyMove the monopoly move
     */
    public void performMonopolyMove(MonopolyMove monopolyMove) {

        // get current game
        Game game = gameService.findGameById(monopolyMove.getGameId());

        // get player that monopolizes and opponents
        Player tycoon = game.getCurrentPlayer();

        // get all players
        List<Player> players = game.getPlayers();

        playerService.monopolizeResources(monopolyMove, tycoon, players);
    }

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param plentyMove the plenty move
     */
    public void performPlentyMove(PlentyMove plentyMove) {

        playerService.plentyResources(plentyMove);
    }

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param buildMove the build move
     */
    public void performRoadProgressMove(BuildMove buildMove) {

        // since the player does not have to pay for road, it gets directly build
        boardService.build(buildMove);
    }

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param knightMove the knight move
     */
    public void performKnightMove(KnightMove knightMove) {

        // get current board
        Board board = gameService.findGameById(knightMove.getGameId()).getBoard();

        // get tileId where robber will be placed
        Long tileId = knightMove.getTileId();

        // set robber on board
        tileService.setRobber(tileId, board);
    }

    /**
     * Is called by the move handler to execute move specific routine.
     *
     * @param stealMove the steal move
     */
    public void performStealMove(StealMove stealMove) {

        // deduct a random resource from victim and add it to player wallet
        playerService.stealResource(stealMove);
    }

    /**
     * Find moves currently available for game with the given id
     *
     * @param gameId the game id
     * @return the list
     */
    public List<Move> findMovesForGameId(Long gameId) {
        return moveRepository.findAllByGameId(gameId);
    }
}
