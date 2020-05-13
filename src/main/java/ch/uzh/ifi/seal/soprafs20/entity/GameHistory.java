package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GAME_HISTORY")
public class GameHistory {

    @Id
    private Long gameId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MoveHistory> moves;

    public GameHistory(){
        this.moves = new ArrayList<>();
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<MoveHistory> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveHistory> moves) {
        this.moves = moves;
    }

    public void addMoveHistory(MoveHistory moveHistory) {
        this.moves.add(moveHistory);
    }
}
