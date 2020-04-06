package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.Player;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(@Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayerFromUser(User user) {
        //TODO: Implement and save to database

        return new Player();
    }

}
