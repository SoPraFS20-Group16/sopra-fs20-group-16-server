package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameLinkDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.CoordinateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.TileDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.board.TileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
@SpringBootTest
public class DTOMapperTest {

    @Autowired
    private TileService tileService;


    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getUserId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void testGameToGameLinkDTO() {

        //Game instance
        Game game = new Game();
        game.setId(1L);

        //Expected Result
        GameLinkDTO expected = new GameLinkDTO();
        expected.setGameId(1L);
        expected.setUrl(1L);

        //Actual result
        GameLinkDTO result = DTOMapper.INSTANCE.convertGameToGameLinkDTO(game);
        assertEquals(expected.getGameId(), result.getGameId(), "The gameId is not mapped correctly");
        assertEquals(expected.getUrl(), result.getUrl(), "The game url is not mapped correctly");
        assertEquals("/games/1", expected.getUrl(),
                "The setUrl method of GameLinkDTO does not work correctly");
    }

    @Test
    public void testGamePostDTOtoEntity() {
        //GamePostDTO instance
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setName("GameName");
        gamePostDTO.setWithBots(false);

        //Expected Result
        Game expected = new Game();
        expected.setName("GameName");
        expected.setWithBots(false);

        //Actual result
        Game result = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        assertEquals(expected.getName(), result.getName(),
                "The name is not mapped correctly from GamePostDTO");
        assertEquals(expected.isWithBots(), result.isWithBots(),
                "The withBots property is not mapped correctly from GamePostDTO");
    }

    @Test
    public void testGameToGameDTO() {
        //Game instance
        Game game = new Game();
        game.setId(1L);
        game.setName("GameName");
        game.setWithBots(false);

        //Expected Result
        GameDTO expected = new GameDTO();
        expected.setGameId(1L);
        expected.setName("GameName");
        expected.setWithBots(false);

        //Actual result
        GameDTO result = DTOMapper.INSTANCE.convertGameToGameDTO(game);
        assertEquals(expected.getGameId(), result.getGameId(),
                "The id is not mapped correctly");
        assertEquals(expected.getName(), result.getName(),
                "The name is not mapped correctly");
        assertEquals(expected.isWithBots(), result.isWithBots(),
                "the withBot property is not mapped correctly");
    }

    @Test
    public void testCoordinateToCoordinateDTO() {

        Coordinate coordinate = new Coordinate(1, 2);

        CoordinateDTO result = DTOMapper.INSTANCE.convertCoordinateToCoordinateDTO(coordinate);

        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.getX(), "The x coordinate should be 1");
        assertEquals(2, result.getY(), "The y coordinate should be 2");
    }

    @Test
    public void testTileToTileDTO() {
        Tile tile = tileService.createTileWithTopCoordinate(new Coordinate(1, 2));

        TileDTO result = DTOMapper.INSTANCE.convertTileToTileDTO(tile);

        assertNotNull(result, "Result should not be null!");
        assertEquals(6, result.getCoordinates().size(), "There should be 6 coordinates!");
        assertEquals(tile.getTileNumber(), result.getTileNumber(), "The tile number does not match!");
        assertEquals(tile.getType(), result.getType(), "The TileType should match!");
    }
}
