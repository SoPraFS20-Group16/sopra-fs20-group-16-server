package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PurchaseMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.TradeMove;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MoveCalculatorHelper {

    private MoveCalculatorHelper() {
        throw new IllegalStateException("Helper class should not be initializes!");
    }

    static boolean canAffordBuilding(Player player, Building building) {

        //get the building price
        ResourceWallet price = building.getPrice();

        //get the players funds
        ResourceWallet funds = player.getWallet();

        //Check for every resource required if the player has enough
        for (ResourceType type : price.getAllTypes()) {
            if (price.getResourceAmount(type) > funds.getResourceAmount(type)) {
                return false;
            }
        }
        return true;
    }

    static boolean canAffordRoad(Player player) {
        Road road = new Road();
        return canAffordBuilding(player, road);
    }

    static boolean canAffordSettlement(Player player) {
        Settlement settlement = new Settlement();
        return canAffordBuilding(player, settlement);
    }

    static boolean canAffordCity(Player player) {
        City city = new City();
        return canAffordBuilding(player, city);
    }

    static boolean canAffordDevelopmentCard(Player player) {

        // get development Card price
        DevelopmentCard developmentCard = new DevelopmentCard();
        ResourceWallet price = developmentCard.getPrice();

        // get funds of player
        ResourceWallet funds = player.getWallet();

        // check if player can afford card
        for (ResourceType type: price.getAllTypes()) {
            if (price.getResourceAmount(type) > funds.getResourceAmount(type)) {
                return false;
            }
        }

        return true;
    }

    public static boolean canAffordTrade(Player player) {

        // get funds of player
        ResourceWallet funds = player.getWallet();

        // check if player has at least 4 resources of the same type
        for (ResourceType type: funds.getAllTypes()) {
            if (funds.getResourceAmount(type) >= 4) {
                return true;
            }
        }

        return false;
    }

    static BuildMove createRoadMove(Game game, Player player, Coordinate coordinate, Coordinate neighbor) {

        Road newRoad = new Road();
        newRoad.setCoordinate1(coordinate);
        newRoad.setCoordinate2(neighbor);
        newRoad.setUserId(player.getUserId());

        BuildMove move = new BuildMove();
        move.setBuilding(newRoad);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static BuildMove createSettlementMove(Game game, Player player, Coordinate coordinate) {

        Settlement newSettlement = new Settlement();
        newSettlement.setCoordinate(coordinate);
        newSettlement.setUserId(player.getUserId());

        BuildMove move = new BuildMove();
        move.setBuilding(newSettlement);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static BuildMove createCityMove(Game game, Player player, Coordinate coordinate) {

        City newCity = new City();
        newCity.setCoordinate(coordinate);
        newCity.setUserId(player.getUserId());

        BuildMove move = new BuildMove();
        move.setBuilding(newCity);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static CardMove createCardMove(Game game, Player player, DevelopmentCard card) {

        CardMove move = new CardMove();
        move.setDevelopmentCard(card);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static List<TradeMove> createTradeMove(Game game, Player player) {

        List<TradeMove> tradeMoves = new ArrayList<>();

        // create a move for every possible needed Type
        for (ResourceType type: ResourceType.values()) {
            TradeMove move = new TradeMove();
            move.setNeededType(type);
            move.setGameId(game.getId());
            move.setUserId(player.getUserId());

            tradeMoves.add(move);
        }

        // return list of tradeMoves
        return tradeMoves;
    }

    static PurchaseMove createPurchaseMove(Game game, Player player) {

        // generate and set random development Card
        DevelopmentCard developmentCard = new DevelopmentCard();

        DevelopmentType devType;
        int randomCard = ThreadLocalRandom.current().nextInt(1, 100 + 1);

        if (randomCard == 1 || randomCard == 25) {
            devType = DevelopmentType.MONOPOLYPROGRESS;
        } else if (randomCard == 2 || randomCard == 24) {
            devType = DevelopmentType.PLENTYPROGRESS;
        } else if (randomCard == 3 || randomCard == 23) {
            devType = DevelopmentType.ROADPROGRESS;
        } else if (4 <= randomCard && randomCard <= 8) {
            devType = DevelopmentType.VICTORYPOINT;
        } else {
            devType = DevelopmentType.KNIGHT;
        }

        developmentCard.setDevelopmentType(devType);

        // create new Move
        PurchaseMove move = new PurchaseMove();
        move.setDevelopmentCard(developmentCard);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;

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

}
