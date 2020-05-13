package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameSummary;
import ch.uzh.ifi.seal.soprafs20.entity.PlayerSummary;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.PassMove;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerSummaryRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.building.BuildingDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.CoordinateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.TileDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.BuildMoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.CardMoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.MoveDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.board.TileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
@SpringBootTest
public class DTOMapperTest {

    @Autowired
    private TileService tileService;

    private Long testGameId = 123L;
    private final String testUsername = "testUsername";
    private final Long testUserId = 12L;
    private final Long testMoveId = 1234L;
    private final Coordinate testCoordinate = new Coordinate(1, 2);


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

        //TODO: Is this test case still up to date?
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
        Tile tile = tileService.createTile(new Coordinate(1, 2), testGameId);

        TileDTO result = DTOMapper.INSTANCE.convertTileToTileDTO(tile);

        assertNotNull(result, "Result should not be null!");
        assertEquals(6, result.getCoordinates().size(), "There should be 6 coordinates!");
        assertEquals(tile.getTileNumber(), result.getTileNumber(), "The tile number does not match!");
        assertEquals(tile.getType(), result.getType(), "The TileType should match!");
    }

    @Test
    public void testConvertGameSummaryToGameSummaryDTO() {

        PlayerSummary playerSummary = new PlayerSummary();
        playerSummary.setUsername(testUsername);
        playerSummary.setUserId(testUserId);
        playerSummary.setPoints(3);

        GameSummary summary = new GameSummary();
        summary.setGameId(testGameId);
        summary.setPlayers(Collections.singletonList(playerSummary));

        GameSummaryDTO gameSummaryDTO = DTOMapper.INSTANCE.convertGameSummaryToGameSummaryDTO(summary);

        assertEquals(summary.getPlayers().size(), gameSummaryDTO.getPlayers().size(),
                "There should be one player mapped to the dto");
        assertEquals(testGameId, gameSummaryDTO.getGameId(), "The gameId should be mapped correctly");
    }

    @Test
    public void testConvertPlayerToPlayerDTO() {
        Player player = new Player();
        player.setUserId(testUserId);
        player.setUsername(testUsername);

        PlayerDTO playerDTO = DTOMapper.INSTANCE.convertPlayerToPlayerDTO(player);

        assertEquals(player.getUsername(), playerDTO.getUsername(), "Username not mapped correctly");
        assertEquals(player.getUserId(), playerDTO.getUserId(), "UserId not mapped correctly");
    }

    @Test
    public void testConvertPlayerSummaryToPlayerSummaryDTO() {
        final int testPoints = 3;
        PlayerSummary summary = new PlayerSummary();
        summary.setPoints(testPoints);
        summary.setUserId(testUserId);
        summary.setUsername(testUsername);

        PlayerSummaryDTO summaryDTO = DTOMapper.INSTANCE.convertPlayerSummaryToPLayerSummaryDTO(summary);

        assertEquals(summary.getUsername(), summaryDTO.getUsername(), "The username is not mapped correctly");
        assertEquals(summary.getPoints(), summaryDTO.getPoints(), "The points are not mapped correctly");
        assertEquals(summary.getUserId(), summaryDTO.getUserId(), "The userId is not mapped correctly");
    }

    @Test
    public void testConvertMoveToMoveDTO() {
        //The passMove is a standard move with no additional field without its own mapper
        PassMove move = new PassMove();
        move.setUserId(testUserId);
        move.setId(testMoveId);

        MoveDTO moveDTO = DTOMapper.INSTANCE.convertMoveToMoveDTO(move);

        assertEquals(move.getUserId(), moveDTO.getUserId(), "UserId not mapped correctly");
        assertEquals(move.getId(), moveDTO.getMoveId(), "MoveId not mapped correctly");
        assertEquals(move.getClass().getSimpleName(), moveDTO.getMoveName(),
                "The move name is not mapped correctly");
    }


    @Test
    public void testConvertBuildMoveToBuildMoveDTO() {
        Settlement building = new Settlement();
        building.setCoordinate(testCoordinate);

        BuildMove move = new BuildMove();
        move.setUserId(testUserId);
        move.setId(testMoveId);
        move.setBuilding(building);

        BuildMoveDTO moveDTO = DTOMapper.INSTANCE.convertBuildMoveToBuildMoveDTO(move);

        assertEquals(move.getUserId(), moveDTO.getUserId(), "UserId not mapped correctly");
        assertEquals(move.getId(), moveDTO.getMoveId(), "MoveId not mapped correctly");
        assertEquals(move.getClass().getSimpleName(), moveDTO.getMoveName(),
                "The move name is not mapped correctly");
        assertEquals(building.getType(), moveDTO.getBuilding().getBuildingType(),
                "Building type does not match");
    }


    @Test
    public void testConvertBuildingToBuildingDTO() {
        Settlement building = new Settlement();
        building.setCoordinate(testCoordinate);
        building.setUserId(testUserId);

        BuildingDTO buildingDTO = DTOMapper.INSTANCE.convertBuildingToBuildingDTO(building);

        assertEquals(building.getType(), buildingDTO.getBuildingType());
        int xCoord = buildingDTO.getCoordinates().get(0).getX();
        int yCoord = buildingDTO.getCoordinates().get(0).getY();

        assertEquals(testCoordinate.getX(), xCoord, "THe x coordinate does not match");
        assertEquals(testCoordinate.getY(), yCoord, "The y coordinate does not match");

        assertEquals(building.getUserId(), buildingDTO.getUserId(), "The userId does not match!");
    }


    @Test
    public void testConvertCardMoveToCardMoveDTO() {
        final DevelopmentType testDevelopmentType = DevelopmentType.ROADPROGRESS;
        CardMove move = new CardMove();
        move.setId(testMoveId);
        move.setUserId(testUserId);
        move.setDevelopmentCard(new DevelopmentCard(testDevelopmentType));

        CardMoveDTO moveDTO = DTOMapper.INSTANCE.convertCardMoveToCardMoveDTO(move);

        assertEquals(move.getUserId(), moveDTO.getUserId(), "UserId not mapped correctly");
        assertEquals(move.getId(), moveDTO.getMoveId(), "MoveId not mapped correctly");
        assertEquals(move.getClass().getSimpleName(), moveDTO.getMoveName(),
                "The move name is not mapped correctly");

        assertEquals(testDevelopmentType, moveDTO.getDevelopmentCard().getDevelopmentType(),
                "The development type is not mapped correctly");
    }


    @Test
    public void testConvertTradeMoveToTradeMoveDTO() {
        //TODO: Implement this test
    }


    @Test
    public void testConvertKnightMoveToKnightMoveDTO() {
        //TODO: Implement this test
    }


    @Test
    public void testConvertMonopolyMoveToMonopolyMoveDTO() {
        //TODO: Implement this test
    }


    @Test
    public void testConvertPlentyMoveToPlentyMoveDTO() {
        //TODO: Implement this test
    }


    @Test
    public void testConvertStealMoveToStealMoveDTO() {
        //TODO: Implement this test
    }


    @Test
    public void testConvertUserLocationToUserLocationDTO() {
        //TODO: Implement this test
    }


    @Test
    public void testConvertMoveHistoryToMoveHistoryDTO() {
        //TODO: Implement this test
    }


    @Test
    public void testConvertGameHistoryToGameHistoryDTO() {
        //TODO: Implement this test
    }

}
