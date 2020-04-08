package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.constant.TileType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TILE")
public class Tile implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private TileType type;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Coordinate> coordinates;

    public Tile() {
        this.coordinates = new ArrayList<>();
    }
}
