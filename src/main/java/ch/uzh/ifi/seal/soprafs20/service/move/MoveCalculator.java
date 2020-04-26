package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstPassMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstSettlementMove;

import java.util.ArrayList;
import java.util.List;

public class MoveCalculator {

    private MoveCalculator() {
        throw new IllegalStateException("Move Calculator should not be initialized!");
    }

    private static List<TradeMove> getAllTradeMoves(Game game) {

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford resource trade & add move
        if (MoveCalculatorHelper.canAffordTrade(player)) {
            return MoveCalculatorHelper.createTradeMove(game, player);
        }
        else {
            return new ArrayList<>();
        }
    }

    private static List<MonopolyMove> getAllMonopolyMoves(Game game) {

        // get current player
        Player player = game.getCurrentPlayer();

        // create & return moves for every resourceType
        return MoveCalculatorHelper.createMonopolyMove(game, player);
    }

    private static List<PlentyMove> getAllPlentyMoves(Game game) {

        // get current player
        Player player = game.getCurrentPlayer();

        // create & return moves for possible requested resourceType(s)
        return MoveCalculatorHelper.createPlentyMove(game, player);
    }

    private static List<PurchaseMove> getAllPurchaseMoves(Game game) {

        // create list for all possible moves
        List<PurchaseMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford development card & add move
        if (MoveCalculatorHelper.canAffordDevelopmentCard(player)) {
            PurchaseMove move = MoveCalculatorHelper.createPurchaseMove(game, player);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }

    private static List<BuildMove> getAllRoadMoves(Game game) {

        // create list for all possible moves
        List<BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford Road
        if (!MoveCalculatorHelper.canAffordRoad(player)) {
            return new ArrayList<>();
        }

        // get current board
        Board board = game.getBoard();

        // check if max road is reached
        List<Road> roadsBuild = MoveCalculatorHelper.getRoadsOfPlayer(player, board);
        if (roadsBuild.size() == PlayerConstants.MAX_NUMBER_ROADS) {
            return new ArrayList<>();
        }

        // - calculate all possible road building moves connecting to another road -

        // get all roads from user
        List<Road> roads = MoveCalculatorHelper.getRoadsOfPlayer(player, board);

        // get all coordinates from roads
        List<Coordinate> roadCoordinates = new ArrayList<>();

        for (Road road : roads) {
            roadCoordinates.add(road.getCoordinate1());
            roadCoordinates.add(road.getCoordinate2());
        }

        // get road end points
        List<Coordinate> roadEndPoints = MoveCalculatorHelper.getRoadEndPoints(roadCoordinates);

        // if all roads are connected to another road or settlement/city on both ends
        if (roadEndPoints.isEmpty()) {
            return new ArrayList<>();
        }

        // get all valid building coordinates
        for (Coordinate coordinate : roadEndPoints) {
            for (Coordinate neighbor : coordinate.getNeighbors())
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    BuildMove move = MoveCalculatorHelper.createRoadMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
        }

        // - calculate all possible road building moves connecting to settlement/city -

        MoveCalculatorHelper.calculateRoadBuildingMovesConnectingToBuilding(game, possibleMoves, player, board);

        return possibleMoves;
    }

    private static List<RoadProgressMove> getAllRoadProgressMoves(Game game) {

        // create list for all possible moves
        List<RoadProgressMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get current board
        Board board = game.getBoard();

        // check if max road is reached
        List<Road> roadsBuild = MoveCalculatorHelper.getRoadsOfPlayer(player, board);
        if (roadsBuild.size() == PlayerConstants.MAX_NUMBER_ROADS) {
            return new ArrayList<>();
        }

        // - calculate all possible road building moves connecting to another road -

        // get all roads from user
        List<Road> roads = MoveCalculatorHelper.getRoadsOfPlayer(player, board);

        // get all coordinates from roads
        List<Coordinate> roadCoordinates = new ArrayList<>();

        for (Road road : roads) {
            roadCoordinates.add(road.getCoordinate1());
            roadCoordinates.add(road.getCoordinate2());
        }

        // get road end points
        List<Coordinate> roadEndPoints = MoveCalculatorHelper.getRoadEndPoints(roadCoordinates);

        // if all roads are connected to another road or settlement/city on both ends
        if (roadEndPoints.isEmpty()) {
            return new ArrayList<>();
        }

        // get all valid building coordinates
        for (Coordinate coordinate : roadEndPoints) {
            for (Coordinate neighbor : coordinate.getNeighbors())
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    RoadProgressMove move = MoveCalculatorHelper.createRoadProgressMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
        }

        // - calculate all possible road building moves connecting to settlement/city -

        MoveCalculatorHelper.calculateRoadProgressMovesConnectingToBuilding(game, possibleMoves, player, board);

        return possibleMoves;
    }

    private static List<BuildMove> getAllSettlementMoves(Game game) {

        // create list for all possible moves
        List<BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford Settlement
        if (!MoveCalculatorHelper.canAffordSettlement(player)) {
            return new ArrayList<>();
        }

        // get current board
        Board board = game.getBoard();

        // check if max settlement is reached
        List<Settlement> settlementsBuild = MoveCalculatorHelper.getSettlementsOfPlayer(player, board);
        if (settlementsBuild.size() == PlayerConstants.MAX_NUMBER_SETTLEMENTS) {
            return new ArrayList<>();
        }

        // get all roads from user
        List<Road> roads = MoveCalculatorHelper.getRoadsOfPlayer(player, board);

        // get all coordinates from roads
        List<Coordinate> roadCoordinates = new ArrayList<>();

        for (Road road : roads) {
            roadCoordinates.add(road.getCoordinate1());
            roadCoordinates.add(road.getCoordinate2());
        }

        // get road end points
        List<Coordinate> roadEndPoints = MoveCalculatorHelper.getRoadEndPoints(roadCoordinates);

        // if all roads are connected to another road or settlement/city on both ends
        if (roadEndPoints.isEmpty()) {
            return new ArrayList<>();
        }

        // get all valid building coordinates
        for (Coordinate coordinate : roadEndPoints) {
            if (MoveCalculatorHelper.isValidBuildingCoordinate(board, coordinate)) {
                BuildMove move = MoveCalculatorHelper.createSettlementMove(game, player, coordinate);
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    private static List<BuildMove> getAllCityMoves(Game game) {

        // create list for all possible moves
        List<BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford city
        if (!MoveCalculatorHelper.canAffordCity(player)) {
            return new ArrayList<>();
        }

        // get current board
        Board board = game.getBoard();

        // check if max city is reached
        List<City> citiesBuild = MoveCalculatorHelper.getCitiesOfPlayer(player, board);
        if (citiesBuild.size() == PlayerConstants.MAX_NUMBER_CITIES) {
            return new ArrayList<>();
        }

        // calculate and add all possible moves
        for (Settlement settlement : board.getSettlements()) {
            if (settlement.getUserId().equals(player.getUserId())) {
                Coordinate coordinate = settlement.getCoordinate();
                BuildMove move = MoveCalculatorHelper.createCityMove(game, player, coordinate);
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    private static List<CardMove> getAllCardMoves(Game game) {

        // create list for all possible moves
        List<CardMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get all development cards from player
        List<DevelopmentCard> developmentCards = player.getDevelopmentCards();

        // create and add a new CardMove for every development card
        for (DevelopmentCard card : developmentCards) {
            CardMove move = MoveCalculatorHelper.createCardMove(game, player, card);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }

    public static List<Move> calculateAllMonopolyMoves(Game game) {

        return new ArrayList<>(getAllMonopolyMoves(game));

    }

    public static List<Move> calculateAllPlentyMoves(Game game) {

        return new ArrayList<>(getAllPlentyMoves(game));
    }

    public static List<Move> calculateAllRoadProgressMoves(Game game) {

        return new ArrayList<>(getAllRoadProgressMoves(game));
    }

    public static List<Move> calculateAllStandardMoves(Game game) {

        List<Move> moves = new ArrayList<>();

        //Add all the build moves
        moves.addAll(getAllCityMoves(game));
        moves.addAll(getAllSettlementMoves(game));
        moves.addAll(getAllRoadMoves(game));

        //Add purchase of devCard moves
        moves.addAll(getAllPurchaseMoves(game));

        //Add trade with bank moves
        moves.addAll(getAllTradeMoves(game));

        //Add devCard moves
        moves.addAll(getAllCardMoves(game));

        // add pass move(s)
        moves.addAll(getPassMove(game));

        //return all the moves
        return moves;
    }

    public static List<Move> calculateAllStandardMovesExclusiveDevCard(Game game) {

        List<Move> moves = new ArrayList<>();

        //Add all the build moves
        moves.addAll(getAllCityMoves(game));
        moves.addAll(getAllSettlementMoves(game));
        moves.addAll(getAllRoadMoves(game));

        //Add purchase of devCard moves
        moves.addAll(getAllPurchaseMoves(game));

        //Add trade with bank moves
        moves.addAll(getAllTradeMoves(game));

        // add pass move(s)
        moves.addAll(getPassMove(game));

        //return all the moves
        return moves;
    }

    private static List<FirstSettlementMove> getAllFirstSettlementMoves(Game game) {

        // create list for all possible moves
        List <FirstSettlementMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get current board
        Board board = game.getBoard();

        // calculate all valid settlement building coordinates
        for (Coordinate coordinate: board.getAllCoordinates()) {
            if (MoveCalculatorHelper.isValidBuildingCoordinate(board, coordinate)) {
                FirstSettlementMove settlementMove = MoveCalculatorHelper.createFirstSettlementMove(game, player, coordinate);
                possibleMoves.add(settlementMove);
            }
        }

        return possibleMoves;
    }

    private static List<FirstRoadMove> getAllFirstRoadMoves(Game game, Settlement settlement) {

        // create list for all possible moves
        List<FirstRoadMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // calculate all valid building coordinates
        for (Coordinate neighbour: settlement.getCoordinate().getNeighbors()) {
            FirstRoadMove roadMove = MoveCalculatorHelper.createFirstRoadMove(game, player, settlement.getCoordinate(), neighbour);
            possibleMoves.add(roadMove);
        }

        return possibleMoves;
    }

    private static List<PassMove> getPassMove(Game game) {

        // create list for possible move
        List<PassMove> possibleMove = new ArrayList<>();

        // get player current player
        Player player = game.getCurrentPlayer();

        // create passMove
        PassMove move = MoveCalculatorHelper.createPassMove(game, player);
        possibleMove.add(move);

        // return move
        return possibleMove;
    }

    private static List<FirstPassMove> getFirstPassMove(Game game) {

        // create list for possible move
        List<FirstPassMove> possibleMove = new ArrayList<>();

        // get player current player
        Player player = game.getCurrentPlayer();

        // create passMove
        FirstPassMove move = MoveCalculatorHelper.createFirstPassMove(game, player);
        possibleMove.add(move);

        // return move
        return possibleMove;
    }

    public static List<Move> calculateAllFirstSettlementMoves(Game game) {
        return new ArrayList<>(getAllFirstSettlementMoves(game));

    }

    public static List<Move> calculateAllFirstRoadMoves(Game game, FirstSettlementMove move) {

        if (move.getBuilding().getClass() != Settlement.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_BUILDING_IN_FIRST_SETTLEMENT_MOVE);
        }

        // cast building
        Settlement settlement = (Settlement) move.getBuilding();

        return new ArrayList<>(getAllFirstRoadMoves(game, settlement));
    }

    public static List<Move> calculatePassMove(Game game) {
        return new ArrayList<>(getPassMove(game));
    }

    public static List<Move> calculateFirstPassMove(Game game) {
        return new ArrayList<>(getFirstPassMove(game));
    }

    public static List<Move> calculateStartMoves(Game game) {
        return new ArrayList<>(getStartMoves(game));
    }

    private static List<StartMove> getStartMoves(Game game) {

        List<StartMove> moves = new ArrayList<>();

        StartMove move = new StartMove();
        move.setGameId(game.getId());
        move.setUserId(game.getCreatorId());
        moves.add(move);
        return moves;
    }

    public static List<Move> calculateAllKnightMoves(Game game) {

        return new ArrayList<>(getAllKnightMoves(game));
    }

    public static List<Move> calculateAllStealMoves(Game game) {

        return new ArrayList<>(getAllStealMoves(game));
    }

    private static List<StealMove> getAllStealMoves(Game game) {

        List<StealMove> possibleMoves = new ArrayList<>();

        // for every opponent player, create a move
        for (Player victim: game.getPlayers()) {
            if (victim != game.getCurrentPlayer()) {

                StealMove move = new StealMove();
                move.setVictim(victim);
                move.setGameId(game.getId());
                move.setUserId(game.getCurrentPlayer().getUserId());

                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    private static List<KnightMove> getAllKnightMoves(Game game) {

        List<KnightMove> knightMoves = new ArrayList<>();

        // get player
        Player player = game.getCurrentPlayer();

        // get current board
        Board board = game.getBoard();

        for (Tile tile: board.getTiles()) {

            KnightMove move = new KnightMove();
            move.setTile(tile);
            move.setGameId(game.getId());
            move.setUserId(player.getId());

            knightMoves.add(move);
        }

        // return all possible robber placement moves
        return knightMoves;
    }
}