package ch.uzh.ifi.seal.soprafs20.entity.game;


import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "PLAYER_QUEUE")
public class PlayerQueue {

    @Column
    Long gameId;
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private int queueSize;
    @ElementCollection
    private Map<Integer, Long> queue;

    public PlayerQueue() {
        queue = new HashMap<>();
        queueSize = 0;
    }

    public boolean hasNext() {
        return queueSize != 0;
    }

    public Long getNextUserIdAfter(Long currentUserId) {
        if (!hasNext()) {
            throw new IllegalStateException(ErrorMsg.NO_PLAYER_IN_QUEUE_WHEN_CALLED);
        }

        for (Integer key : queue.keySet()) {
            if (queue.get(key).equals(currentUserId)) {
                return queue.get((key + 1) % queueSize);
            }
        }

        throw new IllegalStateException("The current user could not be found in the queue!");
    }

    public void addUserId(Long userId) {

        //add to map with new key
        queue.put(queueSize, userId);
        queueSize += 1;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}

