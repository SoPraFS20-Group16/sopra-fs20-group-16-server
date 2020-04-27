package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.constant.TileType;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TILE")
public class Tile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int tileNumber;

    @Column
    private TileType type;

    @Size(max = 6)
    @ManyToMany
    private List<Coordinate> coordinates;


    public Tile() {
        this.coordinates = new ArrayList<>();
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean hasCoordinate(Coordinate other) {
        for (Coordinate coordinate : this.coordinates) {
            if (coordinate.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public int getTileNumber() {
        return tileNumber;
    }

    public void setTileNumber(int tileNumber) {
        this.tileNumber = tileNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}