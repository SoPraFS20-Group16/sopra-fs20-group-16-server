package ch.uzh.ifi.seal.soprafs20.entity.summary;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GAME_SUMMARY")
public class GameSummary {

    @Id
    @Column
    private Long gameId;

    @OneToMany
    private List<PlayerSummary> players;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<PlayerSummary> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerSummary> players) {
        this.players = players;
    }
}
