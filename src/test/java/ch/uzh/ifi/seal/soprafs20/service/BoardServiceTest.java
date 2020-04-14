package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.BoardConstants;
import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.CoordinateRepository;
import ch.uzh.ifi.seal.soprafs20.repository.TileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BoardServiceTest {

    @InjectMocks
    TileService tileService;
    @Mock
    private BoardRepository boardRepository;
    @InjectMocks
    private BoardService boardService;
    @Mock
    private TileRepository tileRepository;

    @Mock
    private CoordinateService coordinateService;

    @Mock
    private CoordinateRepository coordinateRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(boardService, "coordinateService", coordinateService);
        ReflectionTestUtils.setField(tileService, "coordinateRepository", coordinateRepository);
        ReflectionTestUtils.setField(tileService, "tileRepository", tileRepository);
        ReflectionTestUtils.setField(boardService, "tileService", tileService);

    }

    @Test
    public void testCreateBoard() {
        when(boardRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(tileRepository.saveAndFlush(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        when(coordinateRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        Board createdBoard = boardService.createBoard();

        List<Tile> tiles = createdBoard.getTiles();

        assertEquals(19, tiles.size(), "There should be 19 Tiles");

        int deserts = 0;
        for (Tile tile : tiles) {
            if (tile.getType().equals(TileType.DESERT)) {
                deserts++;
            }
        }
        assertEquals(BoardConstants.NUMBER_OF_DESERTS, deserts, "Not the right number of deserts");

        int forests = 0;
        for (Tile tile : tiles) {
            if (tile.getType().equals(TileType.FOREST)) {
                forests++;
            }
        }
        assertEquals(BoardConstants.NUMBER_OF_FORESTS, forests, "Not the right number of forests");

    }
}
