package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebAppConfiguration
@SpringBootTest
public class BoardServiceIntegrationTest {

    @Qualifier("coordinateRepository")
    @Autowired
    CoordinateRepository coordinateRepository;

    @Qualifier("tileRepository")
    @Autowired
    TileRepository tileRepository;

    @Qualifier("boardRepository")
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    TileService tileService;

    @BeforeEach
    public void setup() {
        coordinateRepository.deleteAll();
        boardRepository.deleteAll();
        tileRepository.deleteAll();
    }

    @Test
    public void testCreateBoard() {

        Board result = boardService.createBoard();

        assertNotNull(result, "The result should not be null!");

        List<Tile> tiles = result.getTiles();
        assertEquals(19, tiles.size(), "There should be 19 tiles");

        assertEquals(54, coordinateRepository.findAll().size(), "There should be 54 coordinates");
    }
}
