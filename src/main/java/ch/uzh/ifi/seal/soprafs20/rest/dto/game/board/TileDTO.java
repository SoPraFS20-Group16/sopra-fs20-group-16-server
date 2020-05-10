package ch.uzh.ifi.seal.soprafs20.rest.dto.game.board;

import ch.uzh.ifi.seal.soprafs20.constant.TileType;

import java.util.ArrayList;
import java.util.List;

public class TileDTO {

    private List<CoordinateDTO> coordinates = new ArrayList<>();

    private TileType type;
    private int tileNumber;

    private boolean robber;

    public List<CoordinateDTO> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<CoordinateDTO> coordinates) {
        this.coordinates = coordinates;
    }

    public int getTileNumber() {
        return tileNumber;
    }

    public void setTileNumber(int tileNumber) {
        this.tileNumber = tileNumber;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public boolean isRobber() {
        return robber;
    }

    public void setRobber(boolean robber) {
        this.robber = robber;
    }
}
