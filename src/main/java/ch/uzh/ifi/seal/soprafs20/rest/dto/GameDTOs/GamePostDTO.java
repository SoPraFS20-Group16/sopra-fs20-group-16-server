package ch.uzh.ifi.seal.soprafs20.rest.dto.GameDTOs;

public class GamePostDTO {

    private String token;
    private GameDTO game;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }
}
