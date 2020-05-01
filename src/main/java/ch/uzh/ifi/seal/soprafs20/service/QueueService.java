package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.entity.game.PlayerQueue;
import ch.uzh.ifi.seal.soprafs20.repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class QueueService {

    private final QueueRepository queueRepository;

    @Autowired
    public QueueService(@Qualifier("queueRepository") QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    public Long getNextForGame(Long id) {
        PlayerQueue queue = queueRepository.findByGameId(id);
        Long next = queue.getNext();
        save(queue);

        return next;
    }

    public PlayerQueue save(PlayerQueue queue) {
        return queueRepository.saveAndFlush(queue);
    }

    public void addPlayerToQueue(Long gameId, Long userId) {
        PlayerQueue queue = queueRepository.findByGameId(gameId);
        queue.addUserId(userId);
        save(queue);
    }

    public void deleteQueueForGame(Long gameId) {
        PlayerQueue queue = queueRepository.findByGameId(gameId);

        if (queue != null) {
            queueRepository.delete(queue);
            queueRepository.flush();
        }
    }
}
