package ch.uzh.ifi.seal.soprafs20.constant;

public class BuildingConstants {

    public static final int VICTORY_POINTS_ROAD = 0;
    public static final int BUILDING_FACTOR_ROAD = 0;

    public static final int VICTORY_POINTS_SETTLEMENT = 1;
    public static final int BUILDING_FACTOR_SETTLEMENT = 1;

    public static final int VICTORY_POINTS_CITY = 2;
    public static final int BUILDING_FACTOR_CITY = 2;

    private BuildingConstants() {
        throw new IllegalStateException(ErrorMsg.BUILDING_CONSTANTS_INIT_MSG);
    }
}
