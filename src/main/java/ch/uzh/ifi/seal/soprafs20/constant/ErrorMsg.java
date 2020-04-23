package ch.uzh.ifi.seal.soprafs20.constant;

public class ErrorMsg {

    public static final String TOKEN_INVALID = "The token was not valid!";
    public static final String NOT_LOGGED_IN = "You are not logged in!";
    public static final String NO_GAME_WITH_ID = "There is no game with this id";
    public static final String GAME_NOT_EXISTS = "This game does not exist!";
    public static final String USER_NOT_PLAYER_IN_GAME = "The user is not a player in the game!";
    public static final String GAME_ACCESS_DENIED = "Game access denied!";
    public static final String NO_MOVE_WITH_ID = "There is no move with that id";
    public static final String MOVE_INVALID = "This is not a valid move!";
    public static final String PATHVARIABLE_NOT_MATCH_ID = "The gameId and the PathVariable do not match!";
    public static final String USER_NOT_MATCH_PLAYER_ID = "Users id does not match moves playerId";
    public static final String NOT_ALLOWED_TO_MAKE_MOVE = "You are not allowed to make this move!";
    public static final String GAME_CREATION_CONFLICT = "The game creation ran into a conflict";
    public static final String ALREADY_GAME_WITH_NAME = "There is already a game with this Name!";
    public static final String NO_PLAYER_FOUND_WITH_USER_ID = "There was no player with this user id!";
    public static final String UNDEFINED_BUILDING_TYPE = "The building type should be defined!";
    public static final String WRONG_HANDLER_SETUP = "The Handler seems to be set up wrong!";
    public static final String BOARD_CONSTANTS_INIT_MSG = "BoardConstants should never be initialized";
    public static final String NO_NEGATIVE_RESOURCES = "The player can not have negative resources!";
    public static final String NO_MOVE_FOR_NONEXISTING_GAME = "There should not be a move for a nonexistent game!";
    public static final String NO_PLAYER_IN_QUEUE_WHEN_CALLED = "The queue should not be empty when getNextUserId is called!";
    public static final String UNDEFINED_DEVCARD_TYPE = "The development card type should be defined!";
    public static final String ALREADY_PLAYER_IN_ANOTHER_GAME = "The player with this userId already exists";
    public static final String YOU_ARE_IN_ANOTHER_GAME = "You are already a player in another game";
    public static final String NO_PLAYER_IN_STACK_WHEN_CALLED = "The Stack should not be empty when getUserIdForPlayerAfter is called!" ;
    public static final String GAME_CONSTANTS_INITIALIZATION = "The GameConstantsClass should not be initialized!";
    public static final String PLAYER_CONSTANTS_INITIALIZATION = "PlayerConstants class should not be initialized";

    private ErrorMsg() {
        throw new IllegalStateException("ErrorMsg class should not be initialized");
    }

}
