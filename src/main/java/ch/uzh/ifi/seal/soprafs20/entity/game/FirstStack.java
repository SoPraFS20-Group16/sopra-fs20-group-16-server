package ch.uzh.ifi.seal.soprafs20.entity.game;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "FIRST_STACK")
public class FirstStack {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long gameId;

    @Column
    private int lastIndex;

    @ElementCollection
    private final Map<Integer, Long> playerStack;


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
    public Long getNext() {
        lastIndex++;
        lastIndex = lastIndex % playerStack.size();
        return playerStack.get(lastIndex);
    }

    private void computeFinalStack() {

        int largestToMirror = playerStack.size()-1;
        int index = playerStack.size();
        Map<Integer, Long> toBeAdded = new HashMap<>();

        while (largestToMirror >= 0) {
            for (Integer key: playerStack.keySet()) {

                if (key == largestToMirror) {

                    toBeAdded.put(index, playerStack.get(largestToMirror));
                    largestToMirror--;
                    index++;
                }
            }
        }
        //Add the newly created elements to the player stack
        toBeAdded.keySet().forEach(key -> playerStack.put(key, toBeAdded.get(key)));
    }

    public Long getFirstPlayersUserId() {
        // TODO: necessary assertion?
        lastIndex = 0;
        return playerStack.get(lastIndex);
    }
}
