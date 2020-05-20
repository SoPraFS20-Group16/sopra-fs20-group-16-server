package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserLocation;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.Tile;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.history.BuildMoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.DiceMoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.GameHistory;
import ch.uzh.ifi.seal.soprafs20.entity.history.MoveHistory;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import ch.uzh.ifi.seal.soprafs20.entity.moves.TradeMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.KnightMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.MonopolyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.PlentyMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.development.StealMove;
import ch.uzh.ifi.seal.soprafs20.entity.summary.GameSummary;
import ch.uzh.ifi.seal.soprafs20.entity.summary.PlayerSummary;
import ch.uzh.ifi.seal.soprafs20.rest.dto.building.BuildingDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.CoordinateDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.board.TileDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.history.BuildMoveHistoryDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.history.DiceMoveHistoryDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.history.GameHistoryDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.history.MoveHistoryDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.move.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserLocationDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "tracking", target = "tracking")
    @Mapping(target = "location", ignore = true)
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "tracking", target = "tracking")
    @Mapping(source = "location", target = "location")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "gameId")
    @Mapping(source = "id", target = "url")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "playerMinimum", target = "minPlayers")
    @Mapping(source = "players", target = "joinedPlayers")
    @Mapping(source = "started", target = "started")
    GameLinkDTO convertGameToGameLinkDTO(Game game);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "withBots", target = "withBots")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "gameId")
    @Mapping(source = "withBots", target = "withBots")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "board", target = "board")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "started", target = "started")
    @Mapping(source = "lastDiceRoll", target = "lastDiceRoll")
    @Mapping(source = "currentPlayer", target = "currentPlayer")
    GameDTO convertGameToGameDTO(Game game);

    @Mapping(source = "coordinates", target = "coordinates")
    @Mapping(source = "robber", target = "robber")
    @Mapping(source = "type", target = "type")
    TileDTO convertTileToTileDTO(Tile tile);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "x", target = "x")
    @Mapping(source = "y", target = "y")
    CoordinateDTO convertCoordinateToCoordinateDTO(Coordinate coordinate);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "players", target = "players")
    @Mapping(source = "gameId", target = "gameId")
    GameSummaryDTO convertGameSummaryToGameSummaryDTO(GameSummary summary);


    //Moves and cards are added separately according to requesting user
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "username", target = "username")
    @Mapping(source = "userId", target = "userId")
    PlayerDTO convertPlayerToPlayerDTO(Player player);

    @Mapping(target = "points", source = "points")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "userId", source = "userId")
    PlayerSummaryDTO convertPlayerSummaryToPLayerSummaryDTO(PlayerSummary summary);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    MoveDTO convertMoveToMoveDTO(Move move);

    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "building", target = "building")
    BuildMoveDTO convertBuildMoveToBuildMoveDTO(BuildMove buildMove);

    @Mapping(source = "type", target = "buildingType")
    @Mapping(source = "coordinates", target = "coordinates")
    @Mapping(source = "userId", target = "userId")
    BuildingDTO convertBuildingToBuildingDTO(Building building);

    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "developmentCard", target = "developmentCard")
    CardMoveDTO convertCardMoveToCardMoveDTO(CardMove cardMove);

    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "neededType", target = "neededType")
    @Mapping(source = "offeredType", target = "offeredType")
    TradeMoveDTO convertTradeMovetoTradeMoveDTO(TradeMove tradeMove);

    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "tileId", target = "tileId")
    KnightMoveDTO convertKnightMoveToKnightMoveDTO(KnightMove knightMove);

    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "monopolyType", target = "monopolyType")
    MonopolyMoveDTO convertMonopolyMoveToMonopolyMoveDTO(MonopolyMove monopolyMove);

    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "plentyType", target = "plentyType")
    PlentyMoveDTO convertPlentyMoveToPlentyMoveDTO(PlentyMove plentyMove);

    @Mapping(source = "id", target = "moveId")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "victimId", target = "victimId")
    StealMoveDTO convertStealMoveToStealMoveDTO(StealMove stealMove);

    @Mapping(source = "countryName", target = "countryName")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "latitude", target = "latitude")
    UserLocationDTO convertUserLocationToUserLocationDTO(UserLocation userLocation);

    @Mapping(source = "moves", target = "moves")
    GameHistoryDTO convertGameHistoryToGameHistoryDTO(GameHistory gameHistory);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "moveName", target = "moveName")
    MoveHistoryDTO convertMoveHistoryToMoveHistoryDTO(MoveHistory moveHistory);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "roll", target = "roll")
    DiceMoveHistoryDTO convertDiceMoveHistoryToDiceMoveHistoryDTO(DiceMoveHistory diceMoveHistory);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "moveName", target = "moveName")
    @Mapping(source = "buildingType", target = "buildingType")
    BuildMoveHistoryDTO convertBuildMoveHistoryToBuildMoveHistoryDTO(BuildMoveHistory buildMoveHistory);
}
