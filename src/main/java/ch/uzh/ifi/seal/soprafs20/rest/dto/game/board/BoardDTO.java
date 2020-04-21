package ch.uzh.ifi.seal.soprafs20.rest.dto.game.board;

import java.util.ArrayList;
import java.util.List;

public class BoardDTO {

    List<TileDTO> tiles = new ArrayList<>();

    public List<TileDTO> getTiles() {
        return tiles;
    }

    public void setTiles(List<TileDTO> tiles) {
        this.tiles = tiles;
    }
}
