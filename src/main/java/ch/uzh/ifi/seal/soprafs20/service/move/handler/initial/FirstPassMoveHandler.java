package ch.uzh.ifi.seal.soprafs20.service.move.handler.initial;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.first.FirstPassMove;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FirstPassMoveHandler implements MoveHandler {

    private boolean exitLoop = false;

    /**
     * Calls the correct method from the MoveService according to the Move subclass it belongs to.
     *
     * @param move        the move
     * @param moveService the service
     */
    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != FirstPassMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        FirstPassMove firstPassMove = (FirstPassMove) move;
        moveService.performFirstPassMove(firstPassMove);

        //Calculate if the first part is over
        exitLoop = moveService.canExitFirstPart(firstPassMove.getGameId());
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {

        if (exitLoop) {

            //Shuffle queue for random next (first) player
            selectRandomFirstPlayer(game);

            return MoveCalculator.calculateDiceMove(game);
        }
        return MoveCalculator.calculateFirstSettlementMoves(game);
    }

    private void selectRandomFirstPlayer(Game game) {
        int numberOfPlayers = game.getPlayers().size();

        int random = ThreadLocalRandom.current().nextInt(0, numberOfPlayers);

        game.setCurrentPlayer(game.getPlayers().get(random));
    }
}
