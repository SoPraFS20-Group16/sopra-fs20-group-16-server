package ch.uzh.ifi.seal.soprafs20.service.move.calculator;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.RoadProgressMove;

import java.util.ArrayList;
import java.util.List;

/**
 * this helper method has the task similar to a land registry
 * - it can get already created buildings from players
 * - it can validate new building coordinates
 */
public class MoveLandRegistry {

    private MoveLandRegistry() {
        throw new IllegalStateException(ErrorMsg.INIT_MSG);
    }


    static List<Road> getRoadsOfPlayer(Player player, Board board) {

        List<Road> roads = new ArrayList<>();

        for (Road road : board.getRoads()) {
            if (road.getUserId().equals(player.getUserId())) {
                roads.add(road);
            }
        }
        return roads;
    }

    static List<Settlement> getSettlementsOfPlayer(Player player, Board board) {

        List<Settlement> settlements = new ArrayList<>();

        for (Settlement settlement : board.getSettlements()) {
            if (settlement.getUserId().equals(player.getUserId())) {
                settlements.add(settlement);
            }
        }
        return settlements;
    }

    static List<City> getCitiesOfPlayer(Player player, Board board) {

        List<City> cities = new ArrayList<>();

        for (City city : board.getCities()) {
            if (city.getUserId().equals(player.getUserId())) {
                cities.add(city);
            }
        }
        return cities;
    }


    static boolean isValidBuildingCoordinate(Board board, Coordinate coordinate) {

        if (board.hasBuildingWithCoordinate(coordinate)) {
            return false;
        }
        for (Coordinate neighbor : coordinate.getNeighbors()) {
            if (board.hasBuildingWithCoordinate(neighbor)) {
                return false;
            }
        }
        return true;
    }

    static List<Coordinate> getRoadEndPoints(List<Coordinate> coordinates) {

        int currentSize = coordinates.size();
        for (int i = 0; i < currentSize; i++) {
            Coordinate currentCoordinate = coordinates.get(i);
            int count = 0;
            for (Coordinate coordinate : coordinates) {
                if (currentCoordinate.equals(coordinate)) {
                    count++;
                }
                if (count > 1) {
                    break;
                }
            }
            coordinates.removeIf(o -> o.equals(currentCoordinate));
            currentSize = coordinates.size();
        }

        return coordinates;
    }

    static void calculateRoadBuildingMovesConnectingToBuilding(Game game, List<BuildMove> possibleMoves, Player player, Board board) {

        List<Settlement> settlements = getSettlementsOfPlayer(player, board);
        for (Settlement settlement : settlements) {
            Coordinate coordinate = settlement.getCoordinate();
            for (Coordinate neighbor : coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    BuildMove move = MoveCreator.createRoadMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
            }
        }

        List<City> cities = getCitiesOfPlayer(player, board);
        for (City city : cities) {
            Coordinate coordinate = city.getCoordinate();
            for (Coordinate neighbor : coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    BuildMove move = MoveCreator.createRoadMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
            }
        }
    }

    static void calculateRoadProgressMovesConnectingToBuilding(Game game, List<RoadProgressMove> possibleMoves, Player player, Board board) {

        List<Settlement> settlements = getSettlementsOfPlayer(player, board);
        for (Settlement settlement : settlements) {
            Coordinate coordinate = settlement.getCoordinate();
            for (Coordinate neighbor : coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    RoadProgressMove move = MoveCreator.createRoadProgressMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
            }
        }

        List<City> cities = getCitiesOfPlayer(player, board);
        for (City city : cities) {
            Coordinate coordinate = city.getCoordinate();
            for (Coordinate neighbor : coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    RoadProgressMove move = MoveCreator.createRoadProgressMove(game, player, coordinate, neighbor);
                    possibleMoves.add(move);
                }
            }
        }
    }

    static void calculateRoadBuildingMovesConnectingToRoad(Game game, List<BuildMove> possibleMoves, Player player, Board board) {

        // get all roads from user
        List<Road> roads = getRoadsOfPlayer(player, board);

        // get all coordinates from roads
        List<Coordinate> roadCoordinates = new ArrayList<>();

        for (Road road : roads) {
            roadCoordinates.add(road.getCoordinate1());
            roadCoordinates.add(road.getCoordinate2());
        }

        // get road end points
        List<Coordinate> roadEndPoints = getRoadEndPoints(roadCoordinates);

        // if there are open road end points, then calculate building coordinates
        if (!roadEndPoints.isEmpty()) {
            for (Coordinate coordinate : roadEndPoints) {
                for (Coordinate neighbor : coordinate.getNeighbors())
                    if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                        BuildMove move = MoveCreator.createRoadMove(game, player, coordinate, neighbor);
                        possibleMoves.add(move);
                    }
            }
        }
    }

    public static List<Building> getBuildingsFromTileWithRobber(Game game) {

        List<Building> buildings = new ArrayList<>();

        Board board = game.getBoard();

        for (Tile tile : board.getTiles()) {

            if (tile.hasRobber()) {

                for (Settlement settlement : board.getSettlements()) {
                    if (tile.getCoordinates().contains(settlement.getCoordinate())) {
                        buildings.add(settlement);
                    }
                }

                for (City city : board.getCities()) {
                    if (tile.getCoordinates().contains(city.getCoordinate())) {
                        buildings.add(city);
                    }
                }
            }
        }
        return buildings;
    }
}
