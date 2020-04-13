package ch.uzh.ifi.seal.soprafs20.constant;

public class BoardConstants {

    public static final int NUMBER_OF_DESERTS = 1;
    public static final int NUMBER_OF_FIELDS = 4;
    public static final int NUMBER_OF_FORESTS = 4;
    public static final int NUMBER_OF_PASTURES = 4;
    public static final int NUMBER_OF_HILLS = 3;
    public static final int NUMBER_OF_MOUNTAINS = 3;

    private BoardConstants() {
        throw new IllegalStateException(ErrorMsg.BOARD_CONSTANTS_INIT_MSG);
    }
}
