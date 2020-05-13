package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.BoardDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;

import java.util.List;

public class GameDTO extends AbstractGameDTO {

    private Long gameId;
    private String name;
    private List<PlayerDTO> players;
    private boolean withBots;
    private BoardDTO board;
    private List<MoveDTO> moves;
    private boolean started;
    private int lastDiceRoll;

    public GameDTO() {
        super(GameDTO.class);
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public boolean isWithBots() {
        return withBots;
    }

    public void setWithBots(boolean withBots) {
        this.withBots = withBots;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public void setBoard(BoardDTO board) {
        this.board = board;
    }

    public List<MoveDTO> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveDTO> moves) {
        this.moves = moves;
    }

    public boolean getStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getLastDiceRoll() {
        return lastDiceRoll;
    }

    public void setLastDiceRoll(int lastDiceRoll) {
        this.lastDiceRoll = lastDiceRoll;
    }
}
