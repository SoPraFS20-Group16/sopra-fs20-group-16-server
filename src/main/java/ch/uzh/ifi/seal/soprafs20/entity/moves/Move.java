package ch.uzh.ifi.seal.soprafs20.entity.moves;

import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "MOVE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Move implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column
    private Long userId;      //the players userId

    @Column
    private Long gameId;        //the games Id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public abstract MoveHandler getMoveHandler();
}
