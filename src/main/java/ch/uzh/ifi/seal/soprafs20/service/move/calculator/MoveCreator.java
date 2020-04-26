package ch.uzh.ifi.seal.soprafs20.service.move.calculator;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.*;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.MonopolyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.PlentyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.RoadProgressMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstPassMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstRoadMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstSettlementMove;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * this helper class creates individual moves and the entities (e.g. road) that come with it
 * it always assigns a gameId and playerId to the newly created move
 */
public class MoveCreator {

    private MoveCreator() {
        throw new IllegalStateException(ErrorMsg.INIT_MSG);
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

    static FirstRoadMove createFirstRoadMove(Game game, Player player, Coordinate coordinate1, Coordinate coordinate2) {

        Road newRoad = new Road();
        newRoad.setCoordinate1(coordinate1);
        newRoad.setCoordinate2(coordinate2);
        newRoad.setUserId(player.getUserId());

        FirstRoadMove move = new FirstRoadMove();
        move.setBuilding(newRoad);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static FirstSettlementMove createFirstSettlementMove(Game game, Player player, Coordinate coordinate) {

        Settlement newSettlement = new Settlement();
        newSettlement.setCoordinate(coordinate);
        newSettlement.setUserId(player.getUserId());

        FirstSettlementMove move = new FirstSettlementMove();
        move.setBuilding(newSettlement);
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

    static PassMove createPassMove(Game game, Player player) {

        PassMove move = new PassMove();
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static DiceMove createDiceMove(Game game, Player player) {

        DiceMove move = new DiceMove();
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static FirstPassMove createFirstPassMove(Game game, Player player) {

        FirstPassMove move = new FirstPassMove();
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static RoadProgressMove createRoadProgressMove(Game game, Player player, Coordinate coordinate, Coordinate neighbor) {

        Road newRoad = new Road();
        newRoad.setCoordinate1(coordinate);
        newRoad.setCoordinate2(neighbor);
        newRoad.setUserId(player.getUserId());

        RoadProgressMove move = new RoadProgressMove();
        move.setRoad(newRoad);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    static List<MonopolyMove> createMonopolyMove(Game game, Player player) {

        List<MonopolyMove> monopolyMoves = new ArrayList<>();

        // create a move for every resource type
        for (ResourceType type : ResourceType.values()) {
            MonopolyMove move = new MonopolyMove();

            move.setMonopolyType(type);
            move.setGameId(game.getId());
            move.setUserId(player.getUserId());

            monopolyMoves.add(move);
        }

        // return list of monopolyMoves
        return monopolyMoves;
    }

    static List<PlentyMove> createPlentyMove(Game game, Player player) {

        List<PlentyMove> plentyMoves = new ArrayList<>();

        // create a move for every resource type combination (of two)
        for (ResourceType type1 : ResourceType.values()) {
            for (ResourceType type2 : ResourceType.values()) {
                PlentyMove move = new PlentyMove();

                move.setPlentyType1(type1);
                move.setPlentyType2(type2);
                move.setGameId(game.getId());
                move.setUserId(player.getUserId());

                plentyMoves.add(move);

            }
        }
        // return list of plentyMoves
        return plentyMoves;
    }
}