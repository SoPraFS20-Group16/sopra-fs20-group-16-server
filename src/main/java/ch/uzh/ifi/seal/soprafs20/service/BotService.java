package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.ApplicationConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Transactional
public class BotService {

    private static final Logger log = LoggerFactory.getLogger(BotService.class);

    private MoveService moveService;
    private PlayerService playerService;
    private GameService gameService;

    @Autowired
    public void setMoveService(MoveService moveService) {
        this.moveService = moveService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public Player createBot(Long gameId) {

        Long botId = findUnusedGeneratedValue();

        Player botPlayer = new Player();
        botPlayer.setBot(true);
        botPlayer.setUserId(botId);
        botPlayer.setGameId(gameId);
        botPlayer.setUsername(ApplicationConstants.BOT_NAME);

        //Save the player
        botPlayer = playerService.save(botPlayer);

        //Add player to game
        Game game = gameService.findGameById(gameId);
        gameService.addPlayerToGame(botPlayer, game);

        return botPlayer;
    }

    private Long findUnusedGeneratedValue() {

        //TODO: Revisit this Id generation!
        return new Date().getTime();
    }

    public void performBotMove(Long gameId, Long botId) {

        //The bot id is the equivalent of the userId and called UserId in the bots player persona

        //Find moves for bot
        List<Move> moves = moveService.findMovesForGameAndPlayer(gameId, botId);

        //pick a move
        Move picked = chooseMove(moves);

        moveService.performMove(picked);
        log.info("Bot performed move");

        List<Move> followUpMoves = moveService.findMovesForGameAndPlayer(gameId, botId);

        if (!followUpMoves.isEmpty()) {
            performBotMove(gameId, botId);
        }
    }

    private Move chooseMove(List<Move> moves) {

        int random = ThreadLocalRandom.current().nextInt(0, moves.size());
        return moves.get(random);
    }
}
