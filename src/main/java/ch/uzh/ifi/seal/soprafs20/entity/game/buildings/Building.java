package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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


    //Abstract methods
    public abstract int getVictoryPoints();

    public abstract List<ResourceCard> getPrice();

    //Shared Methods
    public BuildingType getType() {
        return this.type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }
}
