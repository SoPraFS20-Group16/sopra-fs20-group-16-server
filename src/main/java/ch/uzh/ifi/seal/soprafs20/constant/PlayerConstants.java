package ch.uzh.ifi.seal.soprafs20.constant;

public class PlayerConstants {

    public static final int MAX_NUMBER_ROADS = 15;
    public static final int MAX_NUMBER_SETTLEMENTS = 5;
    public static final int MAX_NUMBER_CITIES = 4;

    public static final int INIT_RESOURCE_AMOUNT = 0;

    private PlayerConstants() {
        throw new IllegalStateException(ErrorMsg.PLAYER_CONSTANTS_INITIALIZATION);
    }
}
