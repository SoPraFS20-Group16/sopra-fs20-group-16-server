package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveService.class);


//    @Autowired
//    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
//        this.userRepository = userRepository;
//    } TODO: Autowire MoveRepository in MoveService


    public Move findMove(Move move) {
        //TODO: Implement findMove method in MoveService
        //If no move matches a given primary key return null
        return null;
    }

    public void performMove(Move move) {
        //TODO: Implement perform Move in MoveService
        //passes the instruction to the game to perform the passed move
    }
}
