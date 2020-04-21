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
    @Column
    int next;
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
        next = 0;
    }

    public boolean hasNext() {
        return queueSize != 0;
    }

    public Long getNextUserId() {
        if (!hasNext()) {
            throw new IllegalStateException(ErrorMsg.NO_PLAYER_IN_QUEUE_WHEN_CALLED);
        }

        //The key of the next element to be returned
        int currentNext = next;

        //Calculate the new "next key"
        next = (next + 1) % queueSize;

        //Return the element
        return queue.get(currentNext);
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

