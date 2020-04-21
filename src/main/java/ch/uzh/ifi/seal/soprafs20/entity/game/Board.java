package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "BOARD")
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Tile> tiles;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Road> roads;

    @OneToMany(cascade = CascadeType.ALL)
    private List<City> cities;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Settlement> settlements;

    public Board() {
        tiles = new ArrayList<>();
        cities = new ArrayList<>();
        settlements = new ArrayList<>();
        roads = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    //getters and setters
    public void setId(Long id) {
        this.id = id;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Settlement> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<Settlement> settlements) {
        this.settlements = settlements;
    }

    public void addSettlement(Settlement settlement) {
        this.settlements.add(settlement);
    }

    public void addCity(City city) {
        this.cities.add(city);
    }

    public void addRoad(Road road) {
        this.roads.add(road);
    }

    public List<Coordinate> getAllCoordinates() {

        //Coordinate Set (no duplicates) - board has 54 coordinates
        Set<Coordinate> coordinateSet = new HashSet<>(54);

        //Add all coordinates
        for (Tile tile : tiles) {
            coordinateSet.addAll(tile.getCoordinates());
        }

        //Transform set to array
        return new ArrayList<>(coordinateSet);
    }

    public boolean hasBuildingWithCoordinate(Coordinate coordinate) {
        //Check cities
        for (City city : cities) {
            if (city.getCoordinate().equals(coordinate)) {
                return true;
            }
        }

        //Check settlements
        for (Settlement settlement : settlements) {
            if (settlement.getCoordinate().equals(coordinate)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasRoadWithCoordinates(Coordinate coordinate, Coordinate neighbor) {

        for (Road road : roads) {

            // check if road has first coordinate
            if ( (road.getCoordinate1().equals(coordinate) || road.getCoordinate2().equals(coordinate))
                    // if road has first coordinate, check if it also has second
                    && (road.getCoordinate1().equals(neighbor) || road.getCoordinate2().equals(neighbor)) ) {
                return true;
            }
        }
        return false;
    }

}
