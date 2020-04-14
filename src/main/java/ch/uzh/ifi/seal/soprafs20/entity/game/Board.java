package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
}
