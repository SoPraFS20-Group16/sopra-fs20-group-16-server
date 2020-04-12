package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Building implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private BuildingType type;

    @OneToOne(optional = false)
    private Coordinate coordinate;

    //Abstract methods
    abstract int getVictoryPoints();

    //Shared Methods
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public BuildingType getType() {
        return this.type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }
}
