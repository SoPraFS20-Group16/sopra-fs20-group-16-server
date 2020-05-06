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

    // -- get building(s) from player --

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

    // -- get building(s) from tile --

    private static void getSettlementsFromTile(List<Building> buildings, Board board, Tile tile) {
        for (Settlement settlement : board.getSettlements()) {
            if (tile.getCoordinates().contains(settlement.getCoordinate())) {
                buildings.add(settlement);
            }
        }
    }

    private static void getCitiesFromTile(List<Building> buildings, Board board, Tile tile) {
        for (City city : board.getCities()) {
            if (tile.getCoordinates().contains(city.getCoordinate())) {
                buildings.add(city);
            }
        }
    }

    public static List<Building> getBuildingsFromTileWithRobber(Game game) {

        List<Building> buildings = new ArrayList<>();

        Board board = game.getBoard();

        for (Tile tile : board.getTiles()) {

            if (tile.hasRobber()) {

                getSettlementsFromTile(buildings, board, tile);

                getCitiesFromTile(buildings, board, tile);
            }
        }
        return buildings;
    }

    // -- valid-building-coordinate helper methods --

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

    static List<Coordinate> getRoadCoordinates(List<Road> roads) {

        List<Coordinate> roadCoordinates = new ArrayList<>();

        for (Road road : roads) {
            roadCoordinates.add(road.getCoordinate1());
            roadCoordinates.add(road.getCoordinate2());
        }

        return roadCoordinates;
    }

    static List<Coordinate> getRoadEndPoints(Player player, Board board) {

        List<Road> roads = getRoadsOfPlayer(player, board);

        //Duplicates are not endpoints
        List<Coordinate> coordinates = getRoadCoordinates(roads);
        List<Coordinate> duplicateList = getRoadCoordinates(roads);

        List<Coordinate> coordinatesToRemove = new ArrayList<>();

        for (Coordinate coordinate : coordinates) {
            duplicateList.remove(coordinate);
            for (Coordinate duplicateCandidate : duplicateList) {
                if (coordinate.equals(duplicateCandidate)) {
                    coordinatesToRemove.add(coordinate);
                }
            }
        }

        coordinates.removeAll(coordinatesToRemove);

        //Remove all coordinates that have a building on them
        coordinatesToRemove = new ArrayList<>();

        for (Coordinate coordinate : coordinates) {
            if (board.hasBuildingWithCoordinate(coordinate)) {
                coordinatesToRemove.add(coordinate);
            }
        }

        coordinates.removeAll(coordinatesToRemove);
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

    static void calculateRoadProgressMovesConnectingToRoad(Game game, int previousRoadProgressMoves,
                                                           List<RoadProgressMove> possibleMoves,
                                                           Player player, Board board) {
        // get road end points
        List<Coordinate> roadEndPoints = MoveLandRegistry.getRoadEndPoints(player, board);

        // get all valid building coordinates and create moves
        for (Coordinate coordinate : roadEndPoints) {
            for (Coordinate neighbor : coordinate.getNeighbors())
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    RoadProgressMove move = MoveCreator.createRoadProgressMove(game, player,
                            coordinate, neighbor, previousRoadProgressMoves);
                    possibleMoves.add(move);
                }
        }
    }

    static void calculateRoadProgressMovesConnectingToBuilding(Game game, List<RoadProgressMove> possibleMoves,
                                                               Player player, Board board, int previousRoadProgressMoves) {

        List<Settlement> settlements = getSettlementsOfPlayer(player, board);
        for (Settlement settlement : settlements) {
            Coordinate coordinate = settlement.getCoordinate();
            for (Coordinate neighbor : coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    RoadProgressMove move = MoveCreator.createRoadProgressMove(game, player, coordinate,
                            neighbor, previousRoadProgressMoves);
                    possibleMoves.add(move);
                }
            }
        }

        List<City> cities = getCitiesOfPlayer(player, board);
        for (City city : cities) {
            Coordinate coordinate = city.getCoordinate();
            for (Coordinate neighbor : coordinate.getNeighbors()) {
                if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                    RoadProgressMove move = MoveCreator.createRoadProgressMove(game, player, coordinate,
                            neighbor, previousRoadProgressMoves);
                    possibleMoves.add(move);
                }
            }
        }
    }

    static void calculateRoadBuildingMovesConnectingToRoad(Game game, List<BuildMove> possibleMoves, Player player, Board board) {

        // get all road end points
        List<Coordinate> roadEndPoints = getRoadEndPoints(player, board);

        // if there are open road end points, then calculate building coordinates
            for (Coordinate coordinate : roadEndPoints) {
                for (Coordinate neighbor : coordinate.getNeighbors())
                    if (!board.hasRoadWithCoordinates(coordinate, neighbor)) {
                        BuildMove move = MoveCreator.createRoadMove(game, player, coordinate, neighbor);
                        possibleMoves.add(move);
                    }
            }
    }

}
