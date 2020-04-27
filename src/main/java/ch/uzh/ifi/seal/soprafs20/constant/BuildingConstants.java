package ch.uzh.ifi.seal.soprafs20.constant;

public class BuildingConstants {

    public static final int VICTORY_POINTS_ROAD = 0;
    public static final int RESOURCE_DISTRIBUTING_AMOUNT_ROAD = 0;

    public static final int VICTORY_POINTS_SETTLEMENT = 1;
    public static final int RESOURCE_DISTRIBUTING_AMOUNT_SETTLEMENT = 1;

    public static final int VICTORY_POINTS_CITY = 2;
    public static final int RESOURCE_DISTRIBUTING_AMOUNT_CITY = 2;

    private BuildingConstants() {
        throw new IllegalStateException(ErrorMsg.BUILDING_CONSTANTS_INIT_MSG);
    }
}
