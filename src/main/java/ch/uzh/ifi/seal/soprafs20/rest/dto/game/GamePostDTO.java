package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

public class GamePostDTO {

    private String name;
    private boolean withBots;

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
}
