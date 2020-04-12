package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;
import ch.uzh.ifi.seal.soprafs20.service.move.handler.MoveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveService.class);

    private final PlayerService playerService;

    @Autowired
    public MoveService(PlayerService playerService) {
        this.playerService = playerService;
    }


    public Move findMove(Move move) {
        //TODO: Implement findMove method in MoveService
        //If no move matches a given primary key return null
        return null;
    }

    public void performMove(Move move) {

        MoveHandler handler = move.getMoveHandler();
        handler.perform(move);
    }

    //Is performed after performMove terminates
    public void makeRecalculations() {

        //TODO: Recalculate Vicotory Points


        //TODO: Recalculate Possible moves
    }
}
