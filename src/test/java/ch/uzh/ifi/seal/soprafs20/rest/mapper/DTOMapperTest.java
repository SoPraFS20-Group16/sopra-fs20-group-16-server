package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GameLinkDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
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
    public void testMovePostDTOtoEntity() {

        //MovePostDTO instance
        MovePostDTO movePostDTO = new MovePostDTO();
        movePostDTO.setMoveId(1L);
        movePostDTO.setToken("The Users Token");

        //Expected Move object
        Move expected = new Move();
        expected.setId(1L);

        //Actual
        Move result = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(movePostDTO);
        assertEquals(expected.getId(), result.getId(), "The Id is not mapped correctly");
    }
}
