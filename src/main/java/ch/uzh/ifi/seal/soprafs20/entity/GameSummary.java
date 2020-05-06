package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GAME_SUMMARY")
public class GameSummary {

    @Id
    @Column
    private Long gameId;

    @Column
    private String winner;

    @ElementCollection
    private List<String> players;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
