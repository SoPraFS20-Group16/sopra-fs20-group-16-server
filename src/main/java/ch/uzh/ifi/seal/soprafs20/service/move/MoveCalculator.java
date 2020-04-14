package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MoveCalculator {

    public static List<BuildMove> getAllRoadMoves(Game game) {

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

        return possibleMoves;
    }

    public static List<BuildMove> getAllSettlementMoves(Game game) {

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

    public static List<BuildMove> getAllCityMoves(Game game) {

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

    public static List<CardMove> getAllCardMoves(Game game) {

        // create list for all possible moves
        List<CardMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get all development cards from player
        List<DevelopmentCard> developmentCards = player.getDevelopmentCards();

        // create and add a new CardMove, if the development card is not victoryPointCard
        for (DevelopmentCard card : developmentCards) {

            if (!card.getDevelopmentType().equals(DevelopmentType.VICTORYPOINT)) {
                CardMove move = MoveCalculatorHelper.createCardMove(game, player, card);
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }
}