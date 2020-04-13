package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveCalculationHelper {

    public List<Move> getAllMovesFor(Game game) {

        // create List for all possible moves
        List<Move> possibleMoves = new ArrayList<>();

        // find current player
        Player player = game.findCurrentPlayer();

        // -- calculate all possible build moves --

        for (Road road: player.getRoads()) {

            // TODO: add game logic where a new road/settlement is allowed in if condition
            if (road.getCoordinate1().getNeighbors().isEmpty()) {

                if (player.getRoads().size() < PlayerConstants.MAX_NUMBER_ROADS &&
                        canAffordRoad(player)) {

                    Move move = createRoadMove(game, player);
                    possibleMoves.add(move);
                }

                if (player.getSettlements().size() < PlayerConstants.MAX_NUMBER_SETTLEMENTS &&
                        canAffordSettlement(player)) {

                    Move move = createSettlementMove(game, player);
                    possibleMoves.add(move);
                }

                if (player.getCities().size() < PlayerConstants.MAX_NUMBER_CITIES &&
                        canAffordSettlement(player) && canAffordCity(player)) {

                    Move move = createCityMove(game, player);
                    possibleMoves.add(move);
                }
            }
        }

        // check for available cities and instantiate new moves
        for (int i = 0; i <= player.getSettlements().size(); i++) {

            if (player.getCities().size() < PlayerConstants.MAX_NUMBER_CITIES &&
                    canAffordCity(player)) {

                Move move = createCityMove(game, player);
                possibleMoves.add(move);
            }
        }

        // -- calculate all possible card moves --

        // get all development cards from player
        List<DevelopmentCard> developmentCards = player.getDevelopmentCards();

        // create and add a new CardMove, if the development card is not victoryPointCard
        for (DevelopmentCard card: developmentCards) {

            if(!card.getDevelopmentType().equals(DevelopmentType.VICTORYPOINT)) {

                Move move = createCardMove(game, player, card);
                possibleMoves.add(move);
            }
        }

        // return all possible moves for current player
        return possibleMoves;
    }

    // -- helper methods --

    private Move createCardMove(Game game, Player player, DevelopmentCard card) {

        CardMove move = new CardMove();
        move.setDevelopmentCard(card);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    private Move createRoadMove(Game game, Player player) {

        Road newRoad = new Road();
        BuildMove move = new BuildMove();
        move.setBuilding(newRoad);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;

    }

    private Move createSettlementMove(Game game, Player player) {

        Settlement newSettlement = new Settlement();
        BuildMove move = new BuildMove();
        move.setBuilding(newSettlement);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;

    }

    private Move createCityMove(Game game, Player player) {

        City newCity = new City();
        BuildMove move = new BuildMove();
        move.setBuilding(newCity);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;

    }

    private boolean canAffordCity(Player player) {

        boolean canAfford = false;

        City city = new City();
        List<ResourceCard> price = city.getPrice();
        List<ResourceCard> available = player.getResourceCards();

        if (available.containsAll(price)) {
            canAfford = true;
        }

        return canAfford;
    }

    private boolean canAffordSettlement(Player player) {

        boolean canAfford = false;

        Settlement settlement = new Settlement();
        List<ResourceCard> price = settlement.getPrice();
        List<ResourceCard> available = player.getResourceCards();

        if (available.containsAll(price)) {
            canAfford = true;
        }

        return canAfford;

    }

    private boolean canAffordRoad(Player player) {

        boolean canAfford = false;

        Road road = new Road();
        List<ResourceCard> price = road.getPrice();
        List<ResourceCard> available = player.getResourceCards();

        if (available.containsAll(price)) {
            canAfford = true;
        }

        return canAfford;
    }

}
