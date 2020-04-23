package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "FIRST_STACK")
public class FirstStack {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long gameId;

    @ElementCollection
    private Map<Integer, Long> playerStack;


    public FirstStack() {
        this.playerStack = new HashMap<>();

    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setup(List<Player> players) {

        int playerIndex = 0;

        for (Player player: players) {
            playerStack.put(playerIndex, player.getUserId());
            playerIndex++;
        }

        computeFinalStack();
    }

    //Functionality:
    public Long getUserIdForPlayerAfter(Long userId) {


        for (Integer key : playerStack.keySet()) {
            if (playerStack.get(key).equals(userId)) {
                return playerStack.get((key + 1) % playerStack.size());
            }
        }
        throw new IllegalStateException(ErrorMsg.NO_PLAYER_IN_STACK_WHEN_CALLED);
    }

    private void computeFinalStack() {

        int largestToMirror = playerStack.size()-1;
        int index = playerStack.size();

        while (largestToMirror >= 0) {
            for (Integer key: playerStack.keySet()) {

                if (key == largestToMirror) {

                    playerStack.put(index, playerStack.get(largestToMirror));
                    largestToMirror--;
                    index++;
                }
            }
        }
    }

    public Long getFirstPlayersUserId() {
        return playerStack.get(0);
    }
}
