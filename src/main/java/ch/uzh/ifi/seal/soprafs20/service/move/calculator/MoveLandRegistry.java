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

        List<Coordinate> validEndPoints = new ArrayList<>();

        List<Road> roads = getRoadsOfPlayer(player, board);

        // check if road is between buildings / other roads
        for (Road road : roads) {

            //Check the road and get an array of endpoints (0, 1 or 2 coords)
            validEndPoints.addAll(getValidRoadEndPointsForRoad(road, player.getUserId(), board));
        }

        return validEndPoints;
    }

    /**
     * Calculates the endpoints of a road. Will return an array with 0, 1 or 2 coordinates
     *
     * @param road the road that is inspected for endpoints
     * @return a list that contains valid road end points
     */
    private static List<Coordinate> getValidRoadEndPointsForRoad(Road road, Long userId, Board board) {

        List<Coordinate> endPoints = new ArrayList<>();

        //Check first end (coordinate 1)
        if (isValidRoadEndPoint(road.getCoordinate1(), road.getCoordinate2(), userId, board)) {
            endPoints.add(road.getCoordinate1());
        }

        if (isValidRoadEndPoint(road.getCoordinate2(), road.getCoordinate1(), userId, board)) {
            endPoints.add(road.getCoordinate2());
        }

        return endPoints;
    }

    /**
     * @param coordinate1 the candidate coordinate that is looked at
     * @param coordinate2 the roads other coordinate
     * @return the boolean indicating if coordinate1 is a valid road end point
     */
    private static boolean isValidRoadEndPoint(Coordinate coordinate1, Coordinate coordinate2,
                                               Long userId, Board board) {

        Coordinate neighbour = null;
        Coordinate secondNeighbour = null;

        //Find the first neighbor coordinate that is not part of the road
        for (Coordinate candidate : coordinate1.getNeighbors()) {
            if (!candidate.equals(coordinate2)) {
                neighbour = candidate;
                break;
            }
        }

        //There has to be at least one neighbor that is not coordinate2
        if (neighbour == null) {
            throw new IllegalStateException(ErrorMsg.COORDINATE_ONLY_ONE_NEIGHBOR);
        }

        //Check if there is already a player owned road
        if (board.hasRoadWithCoordinates(coordinate1, neighbour)) {

            // check if road belongs to player
            if (hasRoadWithCoordinatesForPlayer(coordinate1, neighbour, userId, board)) {
                return false;
            }
        }

        //Find the second neighbor that is not coordinate 2 (if exists)
        for (Coordinate candidate : coordinate1.getNeighbors()) {
            if (!candidate.equals(coordinate1) && !candidate.equals(neighbour)) {
                secondNeighbour = candidate;
                break;
            }
        }

        //Check if second neighbor is null and if not, check if there is already a player owned street
        if (secondNeighbour != null &&
                board.hasRoadWithCoordinates(coordinate1, secondNeighbour)) {

            // check if road belongs to player
            return !hasRoadWithCoordinatesForPlayer(coordinate1, secondNeighbour, userId, board);
        }

        //If there is no counter reason
        return true;
    }

    private static boolean hasRoadWithCoordinatesForPlayer(Coordinate coordinate1, Coordinate coordinate2,
                                                           Long userId, Board board) {

        //Find the road
        Road foundRoad = board.getRoadWithCoordinates(coordinate1, coordinate2);

        //If there is no road return false
        if (foundRoad == null) {
            return false;
        }

        //if the found road belongs to player return true else false
        return foundRoad.getUserId().equals(userId);
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

}
