package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.*;

@Entity
@Table(name = "MOVE")
public class Move {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long playerId;      //the players userId

    @Column
    private Long gameId;        //the games Id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
