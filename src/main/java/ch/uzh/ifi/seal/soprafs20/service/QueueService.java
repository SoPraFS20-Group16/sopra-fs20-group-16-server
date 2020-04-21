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

    public PlayerQueue queueForGameWithId(Long id) {
        return queueRepository.findByGameId(id);
    }

    public void save(PlayerQueue queue) {
        queueRepository.saveAndFlush(queue);
    }
}
