package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.DevelopmentCardsDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.PlayerDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.ResourceDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * this class contains helper methods that are used by the GameController
 */
class GameControllerHelper {

    private GameControllerHelper() {
        throw new IllegalStateException("Helper class should not be initialized");
    }

    static Game checkIfGameExists(GameService gameService, Long gameId) {

        Game gameInput = new Game();
        gameInput.setId(gameId);

        Game foundGame = gameService.findGame(gameInput);

        //if game is null then there exists no game with that id
        if (foundGame == null) {
            throw new RestException(HttpStatus.NOT_FOUND, ErrorMsg.NO_GAME_WITH_ID,
                    ErrorMsg.GAME_NOT_EXISTS);
        }
        return foundGame;
    }

    /**
     * Throws RestException with status code 401 UNAUTHORIZED if token is not valid
     *
     * @param userService the user service
     * @param token       the user token
     */
    static void checkToken(UserService userService, String token) {
        if (tokenNotValid(userService, token)) {
            throw new RestException(HttpStatus.UNAUTHORIZED,
                    ErrorMsg.TOKEN_INVALID,
                    ErrorMsg.NOT_LOGGED_IN);
        }
    }

    /**
     * Checks the token for validity
     * This method helps guard the /games endpoints but does not
     * differentiate between users!
     *
     * @param userService
     * @param token       the token to be checked
     * @return returns true if the token is valid, else false
     */
    static boolean tokenNotValid(UserService userService, String token) {

        //Get the logged in user with the token
        User candidate = new User();
        candidate.setToken(token);
        User foundUser = userService.findUser(candidate);

        //If a user is found with that token, then the token is valid
        return foundUser == null;
    }

    static void addCardsAndMoves(MoveService moveService, PlayerService playerService, User requestingUser, GameDTO gameDTO) {

        //Add the players cards to the dto
        Player player = playerService.findPlayerByUserId(requestingUser.getId());

        //Find corresponding PlayerDTO
        for (PlayerDTO playerDTO : gameDTO.getPlayers()) {
            if (playerDTO.getUserId().equals(player.getUserId())) {

                //set development cards
                playerDTO.setDevelopmentCards(new DevelopmentCardsDTO(player.getDevelopmentCards()));

                //set resource wallet
                playerDTO.setResources(new ResourceDTO(player.getWallet()));

                //Add the victory points
                playerDTO.setPoints(player.getVictoryPoints());

                //No need to further compare playerDTOs
                break;
            }
        }


        //Add the moves to the GameDTO
        List<Move> moves = moveService.findMovesForGameAndPlayer(gameDTO.getGameId(), requestingUser.getId());

        //Transform moves to moveDTOs
        List<MoveDTO> moveDTOs = new ArrayList<>();
        for (Move move : moves) {
            moveDTOs.add(move.getMoveHandler().mapToDTO(move));
        }

        //Set the moves of the GameDTO
        gameDTO.setMoves(moveDTOs);


    }

    static User checkIfUserIsPlayerElseThrow403(GameService gameService, UserService userService, String token, Game foundGame) {

        User tempUser = new User();
        tempUser.setToken(token);
        User requestingUser = userService.findUser(tempUser);

        //If user is not a player return  403 forbidden
        if (!gameService.userCanAccessGame(requestingUser, foundGame)) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.USER_NOT_PLAYER_IN_GAME,
                    ErrorMsg.GAME_ACCESS_DENIED);
        }
        return requestingUser;
    }

    static void checkIsValidGameMoveUserCombinationElseThrow(GameService gameService, Game foundGame, Move foundMove, User requestingUser) {

        //If user is not a player of the game return 403 FORBIDDEN
        if (!gameService.userCanAccessGame(requestingUser, foundGame)) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.USER_NOT_PLAYER_IN_GAME,
                    ErrorMsg.GAME_ACCESS_DENIED);
        }

        //The move is not part of the game that was posted to
        if (!foundMove.getGameId().equals(foundGame.getId())) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.PATHVARIABLE_NOT_MATCH_ID,
                    ErrorMsg.MOVE_INVALID);
        }

        //If the users Id does not match the moves PlayerId return 403 FORBIDDEN
        if (!requestingUser.getId().equals(foundMove.getUserId())) {

            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.USER_NOT_MATCH_PLAYER_ID,
                    ErrorMsg.NOT_ALLOWED_TO_MAKE_MOVE);
        }
    }

    static Move findMoveIfExistsElseThrow403(MoveService moveService, Long requestedMoveId) {

        Move foundMove = moveService.findMoveById(requestedMoveId);

        //If move does not exist return 403 FORBIDDEN
        if (foundMove == null) {
            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.NO_MOVE_WITH_ID,
                    ErrorMsg.MOVE_INVALID);
        }
        return foundMove;
    }

    static void checkConflict(Game createdGame) {

        //if created game is null there was a conflict
        if (createdGame == null) {
            throw new RestException(HttpStatus.CONFLICT,
                    ErrorMsg.GAME_CREATION_CONFLICT,
                    ErrorMsg.ALREADY_GAME_WITH_NAME);
        }
    }

    public static void checkIfUserIsInAnotherGameElseThrow403Forbidden(User postUser, PlayerService playerService) {

        Player existingPlayer = playerService.findPlayerByUserId(postUser.getId());

        //If the player is not null then the user is already in another game!
        if (existingPlayer != null) {
            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.ALREADY_PLAYER_IN_ANOTHER_GAME, ErrorMsg.YOU_ARE_IN_ANOTHER_GAME);
        }
    }

    public static void checkPlayerMax(Game game) {
        if (game.getPlayers().size() >= GameConstants.DEFAULT_PLAYER_MAX) {
            throw new RestException(HttpStatus.FORBIDDEN, ErrorMsg.GAME_IS_FULL);
        }
    }
}
