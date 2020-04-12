package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(@Qualifier("boardRepository") BoardRepository boardRepository) {

        this.boardRepository = boardRepository;
    }


    /**
     * Creates a new board, saves it to the database and returns it
     *
     * @return the board
     */
    public Board createBoard() {

        Board board = new Board();


        return boardRepository.saveAndFlush(board);
    }
}
