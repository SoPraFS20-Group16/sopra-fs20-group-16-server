package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveCalculationHelper {

    public ArrayList<Move> getAllMovesFor(Game game) {

        // create List for all possible moves
        List<Move> possibleMoves = new ArrayList<>();

        // find current player
        Player player = game.findCurrentPlayer();

        // -- calculate all possible build moves --

        // - roads -

        // check if player is allowed to build another road
        if (player.getRoads().size() < PlayerConstants.MAX_NUMBER_ROADS) {

        }

        // - settlements -

        // check if player is allowed to build another settlement
        if (player.getRoads().size() < PlayerConstants.MAX_NUMBER_SETTLEMENTS) {

        }

        // - cities -

        // check if player is allowed to build another cities
        if (player.getRoads().size() < PlayerConstants.MAX_NUMBER_CITIES) {

        }

        // -- calculate all possible card moves --

        // get all development cards from player
        List<DevelopmentCard> developmentCards = player.getDevelopmentCards();

        // create and add a new CardMove, if the development card is not victoryPointCard
        for (DevelopmentCard card: developmentCards) {
            if(!card.getDevelopmentType().equals(DevelopmentType.VICTORYPOINT)) {

                CardMove move = new CardMove();
                move.setDevelopmentCard(card);
                move.setGameId(game.getId());
                move.setUserId(player.getUserId());

                possibleMoves.add(move);
            }
        }

        // return all possible moves for current player
        return null;
    }
}
