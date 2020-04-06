package ch.uzh.ifi.seal.soprafs20.entity.gameEntities;

import ch.uzh.ifi.seal.soprafs20.constant.TileType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TILE")
public class Tile {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private TileType type;

    @OneToMany
    private List<Coordinate> coordinates;

    public Tile() {
        this.coordinates = new ArrayList<>();
    }
}
