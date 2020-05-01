package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.FirstStack;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.repository.FirstStackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class FirstStackService {

    private final FirstStackRepository firstStackRepository;

    private GameService gameService;

    @Autowired
    public FirstStackService(@Qualifier("firstStackRepository") FirstStackRepository firstStackRepository) {
        this.firstStackRepository = firstStackRepository;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }


    public Long getNextPlayerInGame(Long gameId) {

        FirstStack stack = firstStackRepository.findByGameId(gameId);

        Long next = stack.getNext();

        //save the stack to persist the new index
        firstStackRepository.saveAndFlush(stack);

        return next;
    }

    public void createStackForGameWithId(Long gameId) {
        Game game = gameService.findGameById(gameId);

        FirstStack stack = new FirstStack();
        stack.setGameId(gameId);
        stack.setup(game.getPlayers());
        Long firstPlayersUserId = stack.getFirstPlayersUserId();

        for (Player player : game.getPlayers()) {
            if (player.getUserId().equals(firstPlayersUserId)) {
                game.setCurrentPlayer(player);
            }
        }

        firstStackRepository.saveAndFlush(stack);
    }

    public void deleteStackForGame(Long gameId) {
        FirstStack stack = firstStackRepository.findByGameId(gameId);
        if (stack != null) {
            firstStackRepository.delete(stack);
            firstStackRepository.flush();
        }
    }
}
