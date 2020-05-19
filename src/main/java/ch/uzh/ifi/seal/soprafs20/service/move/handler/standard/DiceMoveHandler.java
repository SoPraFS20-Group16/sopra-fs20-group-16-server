package ch.uzh.ifi.seal.soprafs20.service.move.handler.standard;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.history.DiceMoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.MoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.moves.DiceMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.move.MoveService;
import ch.uzh.ifi.seal.soprafs20.service.move.calculator.MoveCalculator;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * the handler for the DiceMove
 */
public class DiceMoveHandler implements MoveHandler {

    private boolean robberRoutine = false;
    private int diceRoll;

    @Override
    public void perform(Move move, MoveService moveService) {

        if (move.getClass() != DiceMove.class) {
            throw new IllegalStateException(ErrorMsg.WRONG_HANDLER_SETUP);
        }

        diceRoll = getDiceRoll();

        if (diceRoll == 7) {
            robberRoutine = true;
        }

        // cast move
        DiceMove diceMove = (DiceMove) move;

        // pass back to the moveService
        moveService.performDiceMove(diceMove, diceRoll);
    }

    @Override
    public List<Move> calculateNextMoves(Game game, Move move) {

        if (robberRoutine) {
            return MoveCalculator.calculateAllKnightMoves(game);
        }
        else {
            return MoveCalculator.calculateAllStandardMoves(game);
        }
    }

    @Override
    public MoveHistory getHistory() {
        DiceMoveHistory history = new DiceMoveHistory();
        history.setRoll(this.diceRoll);
        return history;
    }

    private int getDiceRoll() {
        // roll dice1 & dice2
        int min = 1;    // inclusive
        int max = 7;    // exclusive

        int dice1;
        int dice2;

        dice1 = ThreadLocalRandom.current().nextInt(min, max);
        dice2 = ThreadLocalRandom.current().nextInt(min, max);

        return dice1 + dice2;
    }
}
