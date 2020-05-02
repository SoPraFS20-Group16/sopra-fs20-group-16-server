package ch.uzh.ifi.seal.soprafs20.service.move.calculator;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.KnightMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.RoadProgressMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.StealMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstPassMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.initial.FirstSettlementMove;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MoveCalculator {

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    // -- start move(s) --

    public static List<Move> calculateStartMove(Game game) {

        // create a list for possible move(s)
        List<Move> possibleMoves = new ArrayList<>();

        // the start move gets assigned to the game creator (player)
        StartMove move = new StartMove();
        move.setGameId(game.getId());
        move.setUserId(game.getCreatorId());
        possibleMoves.add(move);

        return possibleMoves;
    }

    // -- initial moves --

    public static List<Move> calculateFirstPassMove(Game game) {

        // create list for possible move(s)
        List<Move> possibleMove = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // create passMove
        FirstPassMove move = MoveCreator.createFirstPassMove(game, player);
        possibleMove.add(move);

        // return move
        return possibleMove;
    }

    public static List<Move> calculateFirstSettlementMoves(Game game) {

        // create list for possible move(s)
        List<Move> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get current board
        Board board = game.getBoard();

        // calculate all valid settlement building coordinates
        for (Coordinate coordinate : board.getAllCoordinates()) {
            if (MoveLandRegistry.isValidBuildingCoordinate(board, coordinate)) {
                FirstSettlementMove settlementMove = MoveCreator.createFirstSettlementMove(game, player, coordinate);
                possibleMoves.add(settlementMove);
            }
        }

        return possibleMoves;
    }

    public static List<Move> calculateFirstRoadMoves(Game game, FirstSettlementMove move) {

        if (move.getBuilding().getClass() != Settlement.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_BUILDING_IN_FIRST_SETTLEMENT_MOVE);
        }

        // create list for possible move(s)
        List<Move> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // cast building
        Settlement settlement = (Settlement) move.getBuilding();

        // calculate all valid building coordinates
        for (Coordinate neighbour : settlement.getCoordinate().getNeighbors()) {
            FirstRoadMove roadMove = MoveCreator.createFirstRoadMove(game, player, settlement.getCoordinate(), neighbour);
            possibleMoves.add(roadMove);
        }

        return possibleMoves;
    }

    // -- standard moves --

    // - default moves -

    public static List<Move> calculatePassMove(Game game) {

        // create list for possible move(s)
        List<Move> possibleMove = new ArrayList<>();

        // get player current player
        Player player = game.getCurrentPlayer();

        // create passMove
        PassMove move = MoveCreator.createPassMove(game, player);
        possibleMove.add(move);

        return possibleMove;
    }

    public static List<Move> calculateDiceMove(Game game) {

        // create list for possible move(s)
        List<Move> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // create Dice move
        DiceMove move = MoveCreator.createDiceMove(game, player);
        possibleMoves.add(move);

        return possibleMoves;
    }

    public static List<Move> calculateAllStandardMoves(Game game) {

        // create list for possible move(s)
        List<Move> moves = new ArrayList<>();

        //Add all the build moves
        moves.addAll(calculateRoadMoves(game));
        moves.addAll(calculateSettlementMoves(game));
        moves.addAll(calculateCityMoves(game));

        //Add purchase of devCard moves
        moves.addAll(calculatePurchaseMoves(game));

        //Add trade with bank moves
        moves.addAll(calculateTradeMoves(game));

        //Add devCard moves
        moves.addAll(calculateCardMoves(game));

        // add pass move(s)
        moves.addAll(calculatePassMove(game));

        //return all the moves
        return moves;
    }

    // - build moves -

    public static List<BuildMove> calculateRoadMoves(Game game) {

        // create list for possible move(s)
        List<BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford Road
        if (!MoveTeller.canAffordRoad(player)) {
            return new ArrayList<>();
        }

        // get current board
        Board board = game.getBoard();

        // check if max road is reached
        List<Road> roadsBuild = MoveLandRegistry.getRoadsOfPlayer(player, board);
        if (roadsBuild.size() == PlayerConstants.MAX_NUMBER_ROADS) {
            return new ArrayList<>();
        }

        // calculate all possible road building moves connecting to another road
        MoveLandRegistry.calculateRoadBuildingMovesConnectingToRoad(game, possibleMoves, player, board);

        // calculate all possible road building moves connecting to settlement/city
        MoveLandRegistry.calculateRoadBuildingMovesConnectingToBuilding(game, possibleMoves, player, board);

        return possibleMoves;
    }

    public static List<BuildMove> calculateSettlementMoves(Game game) {

        // create list for all possible moves
        List<BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford Settlement
        if (!MoveTeller.canAffordSettlement(player)) {
            return new ArrayList<>();
        }

        // get current board
        Board board = game.getBoard();

        // check if max settlement is reached
        List<Settlement> settlementsBuild = MoveLandRegistry.getSettlementsOfPlayer(player, board);
        if (settlementsBuild.size() == PlayerConstants.MAX_NUMBER_SETTLEMENTS) {
            return new ArrayList<>();
        }

        // get all roads end points from user (possible building coordinate)
        List<Coordinate> roadEndPoints = MoveLandRegistry.getRoadEndPoints(player, board);

        // get all valid building coordinates and create moves
        if (!roadEndPoints.isEmpty()) {
            for (Coordinate coordinate : roadEndPoints) {
                if (MoveLandRegistry.isValidBuildingCoordinate(board, coordinate)) {
                    BuildMove move = MoveCreator.createSettlementMove(game, player, coordinate);
                    possibleMoves.add(move);
                }
            }
        }

        return possibleMoves;
    }

    public static List<BuildMove> calculateCityMoves(Game game) {

        // create list for all possible moves
        List<BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford city
        if (!MoveTeller.canAffordCity(player)) {
            return new ArrayList<>();
        }

        // get current board
        Board board = game.getBoard();

        // check if max city is reached
        List<City> citiesBuild = MoveLandRegistry.getCitiesOfPlayer(player, board);
        if (citiesBuild.size() == PlayerConstants.MAX_NUMBER_CITIES) {
            return new ArrayList<>();
        }

        // calculate and add all possible moves
        for (Settlement settlement : board.getSettlements()) {
            if (settlement.getUserId().equals(player.getUserId())) {
                Coordinate coordinate = settlement.getCoordinate();
                BuildMove move = MoveCreator.createCityMove(game, player, coordinate);
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    // - card moves -

    public static List<CardMove> calculateCardMoves(Game game) {

        // create list for possible move(s)
        List<CardMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get all development cards from player
        List<DevelopmentCard> developmentCards = player.getDevelopmentCards();

        // create and add a new CardMove for every development card (except victoryPoints)
        for (DevelopmentCard card : developmentCards) {
            if (card.getDevelopmentType() != DevelopmentType.VICTORYPOINT) {
                CardMove move = MoveCreator.createCardMove(game, player, card);
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    public static List<Move> calculateAllStandardMovesExclusiveDevCard(Game game) {

        List<Move> moves = new ArrayList<>();

        //Add all the build moves
        moves.addAll(calculateCityMoves(game));
        moves.addAll(calculateSettlementMoves(game));
        moves.addAll(calculateRoadMoves(game));

        //Add purchase of devCard moves
        moves.addAll(calculatePurchaseMoves(game));

        //Add trade with bank moves
        moves.addAll(calculateTradeMoves(game));

        // add pass move(s)
        moves.addAll(calculatePassMove(game));

        //return all the moves
        return moves;
    }

    // - other moves -

    public static List<Move> calculateTradeMoves(Game game) {

        // create list for possible move(s)
        List<Move> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford resource trade
        for (ResourceType offeredType : ResourceType.values()) {
            if (MoveTeller.canAffordTrade(player, offeredType)) {
                possibleMoves.addAll(MoveCreator.createTradeMove(game, player, offeredType));
            }
        }

        return possibleMoves;
    }

    public static List<PurchaseMove> calculatePurchaseMoves(Game game) {

        // create list for possible move(s)
        List<PurchaseMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford development card & add move
        if (MoveTeller.canAffordDevelopmentCard(player)) {
            PurchaseMove move = MoveCreator.createPurchaseMove(game, player);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }

    // -- development card moves --

    public static List<Move> calculateAllKnightMoves(Game game) {

        List<KnightMove> knightMoves = new ArrayList<>();

        // get player
        Player player = game.getCurrentPlayer();

        // get current board
        Board board = game.getBoard();

        for (Tile tile: board.getTiles()) {

            KnightMove move = new KnightMove();
            move.setTileId(tile.getId());
            move.setGameId(game.getId());
            move.setUserId(player.getId());

            knightMoves.add(move);
        }

        // return all possible robber placement moves
        return new ArrayList<>(knightMoves);
    }

    public static List<Move> calculateAllStealMoves(Game game) {

        List<StealMove> possibleMoves = new ArrayList<>();

        // get all buildings adjacent to tile with robber
        List<Building> buildings = MoveLandRegistry.getBuildingsFromTileWithRobber(game);

        Set<Long> playerIds = new HashSet<>();

        for (Building building : buildings) {
            if (!building.getUserId().equals(game.getCurrentPlayer().getUserId())) {
                playerIds.add(building.getUserId());
            }
        }

        for (Long playerId : playerIds) {
            StealMove move = MoveCreator.createStealMove(game, playerId);
            possibleMoves.add(move);
        }

        return new ArrayList<>(possibleMoves);
    }

    public static List<Move> calculateAllMonopolyMoves(Game game) {

        // get current player
        Player player = game.getCurrentPlayer();

        // create & return moves for every resourceType
        return new ArrayList<>(MoveCreator.createMonopolyMove(game, player));

    }

    public static List<Move> calculateAllPlentyMoves(Game game) {

        // get current player
        Player player = game.getCurrentPlayer();

        // create & return moves for possible requested resourceType(s)
        return new ArrayList<>(MoveCreator.createPlentyMove(game, player));
    }

    public static List<Move> calculateAllRoadProgressMoves(Game game, int previousRoadProgressMoves) {

        // create list for all possible moves
        List<RoadProgressMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get current board
        Board board = game.getBoard();

        // check if max road is reached
        List<Road> roadsBuild = MoveLandRegistry.getRoadsOfPlayer(player, board);
        if (roadsBuild.size() == PlayerConstants.MAX_NUMBER_ROADS) {
            return new ArrayList<>();
        }

        // - calculate all possible road building moves connecting to another road -

        // get road end points
        List<Coordinate> roadEndPoints = MoveLandRegistry.getRoadEndPoints(player, board);

        // get all valid building coordinates and create moves
        if (!roadEndPoints.isEmpty()) {
            for (Coordinate coordinate : roadEndPoints) {
                for (Coordinate neighbor : coordinate.getNeighbors())
                    if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                        RoadProgressMove move = MoveCreator.createRoadProgressMove(game, player,
                                coordinate, neighbor, previousRoadProgressMoves);
                        possibleMoves.add(move);
                    }
            }
        }

        // - calculate all possible road building moves connecting to settlement/city -

        MoveLandRegistry.calculateRoadProgressMovesConnectingToBuilding(game, possibleMoves,
                player, board, previousRoadProgressMoves);

        return new ArrayList<>(possibleMoves);
    }
}