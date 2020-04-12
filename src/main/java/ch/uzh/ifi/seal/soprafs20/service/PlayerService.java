package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.Card;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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

    //TODO: implement logic

    /**
     * Checks if the player can afford to build a road.
     * If yes, a new Road gets created and returned.
     * Will throw an Exception otherwise.
     *
     * @param cards the cards that the player currently holds
     * @return Road
     */
    public Road RoadRequest(List<Card> cards) {

        if (false) {
            throw new RestException(HttpStatus.BAD_REQUEST, "you can not afford this road");
        }

        return new Road();
    }

    /**
     * Checks if the player can afford to build a settlement.
     * If yes, a new Settlement gets created and returned.
     * Will throw an Exception otherwise.
     *
     * @param cards the cards that the player currently holds
     * @return Settlement
     */
    public Settlement SettlementRequest(List<Card> cards) {

        if (false) {
            throw new RestException(HttpStatus.BAD_REQUEST, "you can not afford this settlement");
        }

        return new Settlement();
    }

    /**
     * Checks if the player can afford to build a City.
     * If yes, a new City gets created and returned.
     * Will throw an Exception otherwise.
     *
     * @param cards the cards that the player currently holds
     * @return City
     */
    public City CityRequest(List<Card> cards) {

        if (false) {
            throw new RestException(HttpStatus.BAD_REQUEST, "you can not afford this city");
        }

        return new City();
    }

}
