package ch.uzh.ifi.seal.soprafs20.service.move.calculator;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;

/**
 * this helper class checks if a player has enough resources to afford
 * a requested entity (e.g. settlement or development card)
 */
public class MoveTeller {

    private MoveTeller() {
        throw new IllegalStateException(ErrorMsg.INIT_MSG);
    }


    static boolean canAffordBuilding(Player player, Building building) {

        // get building price
        ResourceWallet price = building.getPrice();

        // get funds of player
        ResourceWallet funds = player.getWallet();

        // check for every resource required if the player has enough
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
        for (ResourceType type : price.getAllTypes()) {
            if (price.getResourceAmount(type) > funds.getResourceAmount(type)) {
                return false;
            }
        }
        return true;
    }

    static boolean canAffordTrade(Player player) {

        // get funds of player
        ResourceWallet funds = player.getWallet();

        // check if player has at least X resources of the same type
        for (ResourceType type : funds.getAllTypes()) {
            if (funds.getResourceAmount(type) >= GameConstants.TRADE_WITH_BANK_RATIO) {
                return true;
            }
        }
        return false;
    }
}
