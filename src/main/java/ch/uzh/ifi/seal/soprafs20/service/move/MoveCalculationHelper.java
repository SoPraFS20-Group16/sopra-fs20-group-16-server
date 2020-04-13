package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MoveCalculationHelper {

    public ArrayList<Move> getAllMovesFor(Game game) {

        // find current player
        Player player = game.findCurrentPlayer();

        // -- calculate all possible build moves --

        //


        // -- calculate all possible card moves --

        // return all possible moves for current player
        return null;
    }
}
