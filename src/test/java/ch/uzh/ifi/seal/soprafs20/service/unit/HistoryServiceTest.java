package ch.uzh.ifi.seal.soprafs20.service.unit;

import ch.uzh.ifi.seal.soprafs20.entity.history.GameHistory;
import ch.uzh.ifi.seal.soprafs20.repository.GameHistoryRepository;
import ch.uzh.ifi.seal.soprafs20.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

class HistoryServiceTest {

    private final Long testGameId = 123L;
    @InjectMocks
    private HistoryService historyService;
    @Mock
    private GameHistoryRepository gameHistoryRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateGameHistory() {
        given(historyService.createGameHistory(Mockito.any())).will(i -> i.getArgument(0));

        GameHistory history = historyService.createGameHistory(testGameId);

        assertNotNull(history, "The history should not be null");
        assertEquals(testGameId, history.getGameId());
        assertNotNull(history.getMoves(), "The moves should not be null (but an empty array)");
    }
}
