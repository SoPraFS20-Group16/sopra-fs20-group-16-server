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
    private int lastIndex;

    @ElementCollection
    private final Map<Integer, Long> queue;


    public PlayerQueue() {
        queue = new HashMap<>();
    }

    private boolean hasNext() {
        return queue.size() != 0;
    }

    public Long getNext() {

        if (!hasNext()) {
            throw new IllegalStateException(ErrorMsg.NO_PLAYER_IN_QUEUE_WHEN_CALLED);
        }

        lastIndex++;
        lastIndex = lastIndex % queue.size();
        return queue.get(lastIndex);
    }

    public void addUserId(Long userId) {

        //add to map with new key
        queue.put(queue.size(), userId);
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
