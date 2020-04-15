package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    public Player createPlayerFromUserId(@NotNull Long userId) {

        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("No user with given Id found!");
        }
        Player player = new Player();
        player.setUsername(user.getUsername());
        player.setUserId(user.getId());

        return playerRepository.saveAndFlush(player);
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

    public void payDevCard(Long playerId, DevelopmentCard developmentCard) {

        // find player
        Player player = playerRepository.findByUserId(playerId);

        // get the currently owned development cards from the player
        List<DevelopmentCard> playerOwnedCards = player.getDevelopmentCards();

        // remove development card
        playerOwnedCards.remove(developmentCard);

        // set the new player owned development cards
        player.setDevelopmentCards(playerOwnedCards);
    }

    public void addVictoryPoint(Long playerId) {

        // find player
        Player player = playerRepository.findByUserId(playerId);

        // get current victoryPoints
        int victoryPoints = player.getVictoryPoints();

        // increase victoryPoints
        victoryPoints += 1;

        // set new victoryPoints
        player.setVictoryPoints(victoryPoints);
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

    public Player findPlayerByUserId(Long id) {
        return playerRepository.findByUserId(id);
    }
}
