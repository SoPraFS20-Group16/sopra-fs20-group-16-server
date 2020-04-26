package ch.uzh.ifi.seal.soprafs20.rest.dto.game.board;

import ch.uzh.ifi.seal.soprafs20.rest.dto.building.BuildingDTO;

import java.util.ArrayList;
import java.util.List;

public class BoardDTO {

    List<TileDTO> tiles = new ArrayList<>();

    List<BuildingDTO> roads = new ArrayList<>();

    List<BuildingDTO> settlements = new ArrayList<>();

    List<BuildingDTO> cities = new ArrayList<>();

    public List<BuildingDTO> getRoads() {
        return roads;
    }

    public void setRoads(List<BuildingDTO> roads) {
        this.roads = roads;
    }

    public List<BuildingDTO> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<BuildingDTO> settlements) {
        this.settlements = settlements;
    }

    public List<BuildingDTO> getCities() {
        return cities;
    }

    public void setCities(List<BuildingDTO> cities) {
        this.cities = cities;
    }

    public List<TileDTO> getTiles() {
        return tiles;
    }

    public void setTiles(List<TileDTO> tiles) {
        this.tiles = tiles;
    }
}
