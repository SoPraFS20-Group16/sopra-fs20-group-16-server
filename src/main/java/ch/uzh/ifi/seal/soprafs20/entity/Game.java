package ch.uzh.ifi.seal.soprafs20.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String name;


    @Id
    @GeneratedValue
    private Long id;
    @OneToMany
    private List<User> players;

    @Column
    private boolean withBots;

    public Game() {
        players = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWithBots() {
        return withBots;
    }

    public void setWithBots(boolean withBots) {
        this.withBots = withBots;
    }

    /**
     * Adds a player to the game.
     * Players must already be saved
     *
     * @param player the player
     */
    public void addPlayer(@NotNull User player) {

        if (player == null) {
            throw new NullPointerException("Game.addPlayer does not take null as input!");
        }

        players.add(player);
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    /**
     * Returns true if the passed user is a player of the game, else returns false
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isPlayer(User player) {
        for (User candidatePlayer : this.players) {
            if (player.getId().equals(candidatePlayer.getId())) {
                return true;
            }
        }
        return false;
    }
}
