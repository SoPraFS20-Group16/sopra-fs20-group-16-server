package ch.uzh.ifi.seal.soprafs20.constant;

public class GameConstants {

    //THIS MY SPECIAL RULE: SCREW YOU CATAN PEOPLE!
    public static final int DEFAULT_PLAYER_MINIMUM = 2;

    public static final int TRADE_WITH_BANK_RATIO = 4;

    private GameConstants() {
        throw new IllegalStateException(ErrorMsg.GAME_CONSTANTS_INITIALIZATION);
    }
}
