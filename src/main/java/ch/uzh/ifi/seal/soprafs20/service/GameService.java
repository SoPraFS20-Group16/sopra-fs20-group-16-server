package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Move;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

//    @Autowired
//    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
//        this.userRepository = userRepository;
//    }

    public List<Game> getGames() {
        //TODO; Implement getGames in GameService
        return null;
    }

    public Game createGame(Game gameInput) {
        //TODO; Implement createGame in GameService

        //Returns null if creation fails!
        return null;
    }

    public Game findGame(Game gameInput) {
        //Must be able to find game from Id
        //If no game exists return null
        return null;
    }

    public boolean userCanAccessGame(User user, Game game) {
        //TODO: Implement userCanAccessGame() method in GameService
        return false;
    }

    public Move findMove(Move move) {
        //TODO: Implement findMove method in GameService
        //If no move matches a given primary key return null
        return null;
    }

    public void performMove(Move move) {
        //TODO: Implement perform Move in GameService
        //passes the instruction to the game to perform the passed move
    }
}
