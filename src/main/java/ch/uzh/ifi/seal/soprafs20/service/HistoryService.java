package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.GameHistory;
import ch.uzh.ifi.seal.soprafs20.entity.MoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.repository.GameHistoryRepository;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HistoryService {

    private final GameHistoryRepository gameHistoryRepository;

    private PlayerService playerService;

    @Autowired
    public HistoryService(@Qualifier("gameHistoryRepository") GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public GameHistory createGameHistory(Long gameId) {
        GameHistory gameHistory = new GameHistory();
        gameHistory.setGameId(gameId);
        return save(gameHistory);
    }

    public GameHistory save(GameHistory gameHistory) {
        return gameHistoryRepository.saveAndFlush(gameHistory);
    }

    public void addMoveToHistory(Move move, MoveHandler handler) {

        Player player = playerService.findPlayerByUserId(move.getUserId());

        MoveHistory moveHistory = handler.getHistory();
        moveHistory.setUserId(player.getUserId());
        moveHistory.setUsername(player.getUsername());
        moveHistory.setMoveName(move.getClass().getSimpleName());

        GameHistory history = findGameHistory(move.getGameId());
        history.addMoveHistory(moveHistory);
        save(history);
    }

    public GameHistory findGameHistory(Long gameId) {
        return gameHistoryRepository.findByGameId(gameId);
    }
}
