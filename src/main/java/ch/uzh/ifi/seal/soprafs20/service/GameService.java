package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.Board;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.Player;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final BoardService boardService;
    private final PlayerService playerService;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("playerRepository") PlayerRepository playerRepository,
                       BoardService boardService,
                       PlayerService playerService) {

        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.boardService = boardService;
        this.playerService = playerService;
    }

    /**
     * Returns all the games currently in the database
     *
     * @return the games
     */
    public List<Game> getGames() {

        return gameRepository.findAll();
    }

    /**
     * Creates a new game
     * <p>
     * The game input has to hold the following fields:
     * -CreatorId
     * -WithBots
     * -Name
     *
     * @param gameInput the game input
     * @return the game
     */
    public Game createGame(Game gameInput) {

        //Check for conflict
        //findGame is specified to work with all the games primary keys
        Game conflictGame = findGame(gameInput);

        //Returns null if there is a conflict
        if (conflictGame != null) {
            return null;
        }

        //Add creator as first player
        User creator = new User();
        creator.setId(gameInput.getCreatorId());

        Player creatorPlayer = playerService.createPlayerFromUser(creator);

        gameInput.addPlayer(creatorPlayer);

        //Add a Board
        Board newBoard = boardService.createBoard();
        gameInput.setBoard(newBoard);


        //Add more options here!
        //...

        return gameRepository.saveAndFlush(gameInput);
    }

    public Game findGame(Game gameInput) {


        //Must be able to find game from Id
        Optional<Game> foundGameOptional = gameRepository.findById(gameInput.getId());
        Game foundGame = foundGameOptional.orElse(null);

        //find by name if not found already
        if (foundGame == null && !(gameInput.getName() == null)) {
            foundGame = gameRepository.findByName(gameInput.getName());
        }


        //If no game exists found game is null
        return foundGame;
    }

    /**
     * A boolean stating if a user can access the game
     *
     * @param user the user as returned by the userService
     * @param game the game as returned by the gameService
     * @return the boolean
     */
    public boolean userCanAccessGame(@NotNull User user, @NotNull Game game) {

        Player player = playerRepository.findByUserId(user.getId());
        if (player == null) {
            return false;
        }
        return game.isPlayer(player);
    }
}
