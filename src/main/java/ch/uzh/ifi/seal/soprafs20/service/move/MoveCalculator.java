package ch.uzh.ifi.seal.soprafs20.service.move;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.game.Board;
import ch.uzh.ifi.seal.soprafs20.entity.game.Player;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.moves.BuildMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.CardMove;
import ch.uzh.ifi.seal.soprafs20.entity.moves.Move;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MoveCalculator {

    // -- plain vanilla code --

    public List<BuildMove> getAllRoadMoves(Game game) {
        return null;
    }

    public List<BuildMove> getAllSettlementMoves(Game game) {

        // create list for all possible moves
        List <BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // check if player can afford Settlement
        if (!canAffordSettlement(player)) {
            return new ArrayList<>();
        }

        // get current board
        Board board = game.getBoard();

        // check if max settlement is reached
        List <Settlement> settlements = getSettlementsOfPlayer(player, board);
        if (settlements.size() == PlayerConstants.MAX_NUMBER_SETTLEMENTS) {
            return new ArrayList<>();
        }

        // get all roads from user
        List<Road> roads = getRoadsOfPlayer(player, board);

        // get all coordinates from roads
        List<Coordinate> roadCoordinates = new ArrayList<>();

        for(Road road: roads) {
            roadCoordinates.add(road.getCoordinate1());
            roadCoordinates.add(road.getCoordinate2());
        }

        // get road end points
        List<Coordinate> roadEndPoints = getRoadEndPoints(roadCoordinates);

        // if all roads are connected to another road or settlement/city on both ends
        if (roadEndPoints.isEmpty()) {
            return new ArrayList<>();
        }

        // get all valid building coordinates
        for (Coordinate coordinate: roadEndPoints) {
            if (isValidBuildingCoordinate(board, coordinate)) {
                BuildMove move = createSettlementMove(game, player, coordinate);
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    public List<BuildMove> getAllCityMoves(Game game) {

        // create list for all possible moves
        List<BuildMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get current board
        Board board = game.getBoard();

        // calculate and add all poss
        for (Settlement settlement: board.getSettlements()) {
            Coordinate coordinate = settlement.getCoordinate();
        }



        return null;
    }

    public List<CardMove> getAllCardMoves(Game game) {

        // create list for all possible moves
        List<CardMove> possibleMoves = new ArrayList<>();

        // get current player
        Player player = game.getCurrentPlayer();

        // get all development cards from player
        List<DevelopmentCard> developmentCards = player.getDevelopmentCards();

        // create and add a new CardMove, if the development card is not victoryPointCard
        for (DevelopmentCard card: developmentCards) {

            if(!card.getDevelopmentType().equals(DevelopmentType.VICTORYPOINT)) {
                CardMove move = createCardMove(game, player, card);
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    // -- helper methods --

    private boolean canAffordSettlement(Player player) {

        boolean canAfford = false;

        Settlement settlement = new Settlement();
        List<ResourceCard> price = settlement.getPrice();
        List<ResourceCard> available = player.getResourceCards();

        if (price.size() > available.size()) {
            return false;
        }

        // TODO: add working functionality
        if (available.containsAll(price)) {
            canAfford = true;
        }

        return canAfford;

    }

    private BuildMove createSettlementMove(Game game, Player player, Coordinate coordinate) {

        Settlement newSettlement = new Settlement();
        newSettlement.setCoordinate(coordinate);
        newSettlement.setUserId(player.getUserId());

        BuildMove move = new BuildMove();
        move.setBuilding(newSettlement);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;

    }

    private boolean isValidBuildingCoordinate(Board board, Coordinate coordinate) {
        if (board.hasBuildingWithCoordinate(coordinate)) {
            return false;
        }
        for (Coordinate neighbor: coordinate.getNeighbors()) {
            if (board.hasBuildingWithCoordinate(neighbor)) {
                return false;
            }
        }
        return true;
    }

    private List<Coordinate> getRoadEndPoints(List<Coordinate> coordinates) {
        int currentSize = coordinates.size();
        for (int i = 0; i < currentSize; i++) {
            Coordinate currentCoordinate = coordinates.get(i);
            int count = 0;
            for (Coordinate coordinate: coordinates) {
                if (currentCoordinate.equals(coordinate)) {
                    count++;
                }
                if (count > 1) {
                    break;
                }
            }
            coordinates.removeIf(o -> o.equals(currentCoordinate));
            currentSize = coordinates.size();
        }

        return coordinates;
    }

    private List<Road> getRoadsOfPlayer(Player player, Board board) {
        List<Road> roads = new ArrayList<>();

        for(Road road: board.getRoads()) {
            if(road.getUserId().equals(player.getUserId())) {
                roads.add(road);
            }
        }
        return roads;
    }

    private List<Settlement> getSettlementsOfPlayer(Player player, Board board) {
        List<Settlement> settlements = new ArrayList<>();

        for(Settlement settlement: board.getSettlements()) {
            if(settlement.getUserId().equals(player.getUserId())) {
                settlements.add(settlement);
            }
        }
        return settlements;
    }




    // -- !! construction work ahead !! --

    public List<Move> getInitSettlementMoves(Game game) {

        // create list for all possible initial moves
        List<Move> possibleMoves = new ArrayList<>();

        // find game board
        Board board = game.getBoard();

        // find current player
        Player player = game.getCurrentPlayer();

        // get all coordinates from board
        List<Coordinate> allCoordinates = board.getAllCoordinates();

        // create list for all invalid build coordinates
        List<Coordinate> invalidCoordinates = new ArrayList<>();

        // get all coordinates from already build settlements
        for (Settlement settlement: board.getSettlements()){
            Coordinate tmp = settlement.getCoordinate();
            invalidCoordinates.add(tmp);
            if (settlement.getUserId().equals(player.getUserId())){
                createRoadMove(game, player);
            }
        }

        // get all coordinates from already build roads
        for (Road road: board.getRoads()) {
            Coordinate tmp1 = road.getCoordinate1();
            Coordinate tmp2 = road.getCoordinate2();
            invalidCoordinates.add(tmp1);
            invalidCoordinates.add(tmp2);
        }

        allCoordinates.removeAll(invalidCoordinates);

        for (Coordinate coordinate: allCoordinates) {
            // TODO: add constraint so that the minimal distance between two Settlements holds
            Move move = createSettlementMove(game, player, coordinate);
            possibleMoves.add(move);
        }

        return possibleMoves;
    }

    public List<Move> getInitRoadMoves(Game game) {

        return null;
    }

    public List<Move> getAllMovesFor(Game game) {

        // create List for all possible moves
        List<Move> possibleMoves = new ArrayList<>();

        // find current player
        Player player = game.getCurrentPlayer();

        // -- calculate all possible build moves --

        for (Road road: player.getRoads()) {

            // TODO: add game logic where a new road/settlement is allowed in if condition
            if (road.getCoordinate1().getNeighbors()) {

                if (player.getRoads().size() < PlayerConstants.MAX_NUMBER_ROADS &&
                        canAffordRoad(player)) {

                    Move move = createRoadMove(game, player);
                    possibleMoves.add(move);
                }

                if (player.getSettlements().size() < PlayerConstants.MAX_NUMBER_SETTLEMENTS &&
                        canAffordSettlement(player)) {

                    Move move = createSettlementMove(game, player);
                    possibleMoves.add(move);
                }

                if (player.getCities().size() < PlayerConstants.MAX_NUMBER_CITIES &&
                        canAffordSettlement(player) && canAffordCity(player)) {

                    Move move = createCityMove(game, player);
                    possibleMoves.add(move);
                }
            }
        }

        // check for available cities and instantiate new moves
        for (int i = 0; i <= player.getSettlements().size(); i++) {

            if (player.getCities().size() < PlayerConstants.MAX_NUMBER_CITIES &&
                    canAffordCity(player)) {

                Move move = createCityMove(game, player);
                possibleMoves.add(move);
            }
        }

        // -- calculate all possible card moves --

        // get all development cards from player
        List<DevelopmentCard> developmentCards = player.getDevelopmentCards();

        // create and add a new CardMove, if the development card is not victoryPointCard
        for (DevelopmentCard card: developmentCards) {

            if(!card.getDevelopmentType().equals(DevelopmentType.VICTORYPOINT)) {
                Move move = createCardMove(game, player, card);
                possibleMoves.add(move);
            }
        }

        // return all possible moves for current player
        return possibleMoves;
    }

    // -- helper methods --
    // TODO: add coordinates to new Buildings

    private CardMove createCardMove(Game game, Player player, DevelopmentCard card) {

        CardMove move = new CardMove();
        move.setDevelopmentCard(card);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;
    }

    private Move createRoadMove(Game game, Player player) {

        Road newRoad = new Road();
        BuildMove move = new BuildMove();
        move.setBuilding(newRoad);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;

    }

    private Move createCityMove(Game game, Player player) {

        City newCity = new City();
        BuildMove move = new BuildMove();
        move.setBuilding(newCity);
        move.setGameId(game.getId());
        move.setUserId(player.getUserId());

        return move;

    }

    private boolean canAffordCity(Player player) {

        boolean canAfford = false;

        City city = new City();
        List<ResourceCard> price = city.getPrice();
        List<ResourceCard> available = player.getResourceCards();

        if (available.containsAll(price)) {
            canAfford = true;
        }

        return canAfford;
    }

    private boolean canAffordRoad(Player player) {

        boolean canAfford = false;

        Road road = new Road();
        List<ResourceCard> price = road.getPrice();
        List<ResourceCard> available = player.getResourceCards();

        if (available.containsAll(price)) {
            canAfford = true;
        }

        return canAfford;
    }

    private List<Integer> countResourceTypes(List<ResourceCard> cards) {

        List<Integer> counter = new ArrayList<>();

        int ore = 0;
        int wool = 0;
        int lumber = 0;
        int brick = 0;
        int grain = 0;

        for (ResourceCard card: cards) {

            switch (card.getResourceType()) {
                case ORE:
                    ore += 1;
                    break;
                case WOOL:
                    wool += 1;
                    break;
                case BRICK:
                    brick += 1;
                    break;
                case GRAIN:
                    grain += 1;
                    break;
                case LUMBER:
                    lumber += 1;
                    break;
            }
        }

        counter.add(ore);
        counter.add(wool);
        counter.add(lumber);
        counter.add(grain);
        counter.add(brick);

        return counter;
    }

}
