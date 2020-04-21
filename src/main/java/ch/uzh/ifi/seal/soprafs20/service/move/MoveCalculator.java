package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;

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

        // check if max settlement is reached
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

        List<Settlement> settlements = MoveCalculatorHelper.getSettlementsOfPlayer(player, board);
        for (Settlement settlement: settlements) {
            Coordinate coordinate = settlement.getCoordinate();
            for (Coordinate neighbor: coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    BuildMove move = MoveCalculatorHelper.createRoadMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
            }
        }

        List<City> cities = MoveCalculatorHelper.getCitiesOfPlayer(player, board);
        for (City city: cities) {
            Coordinate coordinate = city.getCoordinate();
            for (Coordinate neighbor: coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    BuildMove move = MoveCalculatorHelper.createRoadMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
            }
        }

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

    public static List<Move> calculateAllStandardMoves(Game game) {

        List<Move> moves = new ArrayList<>();

        //Add all the build moves
        moves.addAll(MoveCalculator.getAllCityMoves(game));
        moves.addAll(MoveCalculator.getAllSettlementMoves(game));
        moves.addAll(MoveCalculator.getAllRoadMoves(game));

        //Add purchase of devcard moves
        moves.addAll(MoveCalculator.getAllPurchaseMoves(game));

        //Add trade with bank moves
        moves.addAll(MoveCalculator.getAllTradeMoves(game));

        //Add devcard moves
        moves.addAll(MoveCalculator.getAllCardMoves(game));

        //return all the moves
        return moves;
    }

    public static List<Move> getAllFirstMoves() {
        //TODO: Calculate the FirstMoves
        return new ArrayList<>();
    }
}