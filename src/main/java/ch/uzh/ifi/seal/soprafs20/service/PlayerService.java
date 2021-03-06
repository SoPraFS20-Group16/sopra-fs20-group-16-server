package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PurchaseMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.TradeMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.MonopolyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.PlentyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.StealMove;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository,
                         @Qualifier("userRepository") UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create player from user player.
     * <p>
     * Will throw an exception if the user can not be found
     *
     * @param userId the userId to find the user
     * @return the player
     */
    public Player createPlayer(Long userId, Long gameId) {

        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("No user with given Id found!");
        }
        Player player = new Player();
        player.setUsername(user.getUsername());
        player.setUserId(user.getId());
        player.setGameId(gameId);

        return playerRepository.saveAndFlush(player);
    }

    public Player payForDevelopmentCard(PurchaseMove move) {

        // find player
        Player player = playerRepository.findByUserId(move.getUserId());

        if (player == null) {
            throw new NullPointerException(ErrorMsg.NO_PLAYER_FOUND_WITH_USER_ID);
        }

        // pay for developmentCard
        ResourceWallet price = new DevelopmentCard().getPrice();
        ResourceWallet funds = player.getWallet();

        for (ResourceType type : price.getAllTypes()) {
            funds.removeResource(type, price.getResourceAmount(type));
        }

        player.setWallet(funds);

        // save player
        return playerRepository.saveAndFlush(player);
    }

    public Player addDevelopmentCard(PurchaseMove move) {

        // find player
        Player player = playerRepository.findByUserId(move.getUserId());

        if (player == null) {
            throw new NullPointerException(ErrorMsg.NO_PLAYER_FOUND_WITH_USER_ID);
        }

        // generate and set random development Card
        DevelopmentCard developmentCard = new DevelopmentCard();

        DevelopmentType devType;
        int randomCard = ThreadLocalRandom.current().nextInt(1, 100 + 1);

        if (randomCard >= 1 && randomCard <= 25) {
            devType = DevelopmentType.KNIGHT;
        }
        else if (randomCard >= 26 && randomCard <= 60) {
            devType = DevelopmentType.PLENTYPROGRESS;
        }
        else if (randomCard >= 61 && randomCard <= 75) {
            devType = DevelopmentType.ROADPROGRESS;
        }
        else if (randomCard >= 76 && randomCard <= 90) {
            devType = DevelopmentType.MONOPOLYPROGRESS;
        }
        else {
            devType = DevelopmentType.VICTORYPOINT;
        }

        developmentCard.setDevelopmentType(devType);

        // add DevelopmentCard
        player.addDevelopmentCard(developmentCard);

        return playerRepository.saveAndFlush(player);
    }

    public Player payForTrade(TradeMove move) {

        //Find Player
        Player player = playerRepository.findByUserId(move.getUserId());

        if (player == null) {
            throw new NullPointerException(ErrorMsg.NO_PLAYER_FOUND_WITH_USER_ID);
        }

        // pay for resource
        ResourceWallet funds = player.getWallet();
        ResourceType type = move.getOfferedType();

        int tradeRatio = GameConstants.TRADE_WITH_BANK_RATIO;

        funds.removeResource(type, tradeRatio);

        return playerRepository.saveAndFlush(player);
    }

    public Player receiveFromTrade(TradeMove move) {

        //Find Player
        Player player = playerRepository.findByUserId(move.getUserId());

        if (player == null) {
            throw new NullPointerException(ErrorMsg.NO_PLAYER_FOUND_WITH_USER_ID);
        }

        // get traded resourceType and funds from player
        ResourceType type = move.getNeededType();
        ResourceWallet funds = player.getWallet();

        // add resource to players' wallet
        funds.addResource(type, 1);

        return playerRepository.saveAndFlush(player);
    }

    public void monopolizeResources(MonopolyMove move, Player tycoon, List<Player> players) {

        // get monopolized resource
        ResourceType monopolyType = move.getMonopolyType();

        // get funds from tycoon
        ResourceWallet funds = tycoon.getWallet();

        // get all resources of monopoly type from all opponents and add them to the tycoon funds
        for (Player player : players) {
            if (!player.equals(tycoon)) {

                ResourceWallet opponentFunds = player.getWallet();
                int transfer = opponentFunds.getResourceAmount(monopolyType);

                funds.addResource(monopolyType, transfer);
                opponentFunds.removeResource(monopolyType, transfer);

                save(player);
            }
        }

        save(tycoon);
    }

    public Player save(Player player) {
        return playerRepository.saveAndFlush(player);
    }

    public void plentyResources(PlentyMove move) {

        // find player
        Player player = playerRepository.findByUserId(move.getUserId());

        if (player == null) {
            throw new NullPointerException(ErrorMsg.NO_PLAYER_FOUND_WITH_USER_ID);
        }

        // get funds from player
        ResourceWallet funds = player.getWallet();

        // update wallet according to plenty type
        PlentyType type = move.getPlentyType();

        switch (type) {
            case MINER:
                funds.addResource(ResourceType.BRICK, 2);
                funds.addResource(ResourceType.ORE, 2);
                break;
            case FARMER:
                funds.addResource(ResourceType.WOOL, 2);
                funds.addResource(ResourceType.GRAIN, 2);
                break;
            case LUMBERJACK:
                funds.addResource(ResourceType.LUMBER, 5);
                break;
            default:
                throw new IllegalStateException(ErrorMsg.PLENTY_TYPE_INVALID);
        }

        save(player);
    }

    public Player payForBuilding(BuildMove move) {

        //Find Player
        Player player = playerRepository.findByUserId(move.getUserId());

        if (player == null) {
            throw new NullPointerException(ErrorMsg.NO_PLAYER_FOUND_WITH_USER_ID);
        }

        //Pay for the given building
        payForBuildingWorker(move.getBuilding(), player);

        //Save
        return playerRepository.saveAndFlush(player);
    }

    private void payForBuildingWorker(Building building, Player player) {

        //Get building price
        ResourceWallet price = building.getPrice();

        //Get players funds
        ResourceWallet funds = player.getWallet();

        //Remove all the resources required to complete payment
        for (ResourceType type : price.getAllTypes()) {
            funds.removeResource(type, price.getResourceAmount(type));
        }
    }

    public void removeDevelopmentCard(CardMove move) {

        // find player
        Player player = playerRepository.findByUserId(move.getUserId());

        // get owed development cardType
        DevelopmentType type = move.getDevelopmentCard().getDevelopmentType();

        // get currently owned development cards from player
        List<DevelopmentCard> ownedCards = player.getDevelopmentCards();

        // remove development card from player
        for (DevelopmentCard card : ownedCards) {
            if (card.getDevelopmentType() == type) {
                ownedCards.remove(card);
                break;
            }
        }

        // set the new owned cards
        player.setDevelopmentCards(ownedCards);
    }

    public int getPointsFromDevelopmentCards(Player player) {
        int victoryPoints = 0;

        // get all victory points earned from development cards
        for (DevelopmentCard card : player.getDevelopmentCards()) {
            if (card.getDevelopmentType().equals(DevelopmentType.VICTORYPOINT)) {
                victoryPoints += 1;
            }
        }

        return victoryPoints;
    }

    public void stealResource(StealMove move) {

        // find player & corresponding funds
        Player player = playerRepository.findByUserId(move.getUserId());
        ResourceWallet fundsPlayer = player.getWallet();

        // find victim & corresponding funds
        Player victim = playerRepository.findByUserId(move.getVictimId());
        ResourceWallet fundsVictim = victim.getWallet();

        // deduct random resource card from opponent and add to player
        List<ResourceType> availableTypes = new ArrayList<>();

        for (ResourceType type : ResourceType.values()) {
            if (fundsVictim.getResourceAmount(type) > 0) {
                availableTypes.add(type);
            }
        }

        // if victim does not have any resources end move here
        if (availableTypes.isEmpty()) {
            return;
        }

        int random = ThreadLocalRandom.current().nextInt(0, availableTypes.size());

        ResourceType stolenType = availableTypes.get(random);

        fundsPlayer.addResource(stolenType, 1);
        fundsVictim.removeResource(stolenType, 1);

        // save players
        playerRepository.saveAndFlush(player);
        playerRepository.saveAndFlush(victim);
    }

    public void updateResources(ResourceType type, List<Building> buildings, Player player) {

        ResourceWallet funds = player.getWallet();
        for (Building building : buildings) {
            int amount = building.getResourceDistributingAmount();
            funds.addResource(type, amount);
        }
        save(player);
    }

    public void receiveInitialResources(ResourceType type, Long userId, Building building) {

        // get player wallet
        Player player = findPlayerByUserId(userId);
        ResourceWallet funds = player.getWallet();

        // add resources accordingly (type and distributingAmount)
        funds.addResource(type, building.getResourceDistributingAmount());

        // save player
        save(player);
    }

    public Player findPlayerByUserId(Long id) {
        return playerRepository.findByUserId(id);
    }
}
