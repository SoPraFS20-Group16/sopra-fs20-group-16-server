package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.Board;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final TileRepository tileRepository;

    @Autowired
    public BoardService(@Qualifier("boardRepository") BoardRepository boardRepository,
                        @Qualifier("tileRepository") TileRepository tileRepository) {

        this.boardRepository = boardRepository;
        this.tileRepository = tileRepository;
    }


    /**
     * Creates a new board, saves it to the database and returns it
     *
     * @return the board
     */
    public Board createBoard() {

        Board board = new Board();
        //TODO: Implement board setup

        return boardRepository.saveAndFlush(board);
    }
}
