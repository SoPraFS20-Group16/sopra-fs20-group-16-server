package ch.uzh.ifi.seal.soprafs20.constant;

public class GameConstants {

    public static final int DEFAULT_PLAYER_MINIMUM = 2;
    public static final int DEFAULT_PLAYER_MAX = 4;

    public static final int TRADE_WITH_BANK_RATIO = 4;
    public static final int NUMBER_OF_FIRST_ROUNDS = 2;
    public static final int NUMBER_OF_ROADS_ROAD_PROGRESS = 2;

    private GameConstants() {
        throw new IllegalStateException(ErrorMsg.GAME_CONSTANTS_INITIALIZATION);
    }
}
