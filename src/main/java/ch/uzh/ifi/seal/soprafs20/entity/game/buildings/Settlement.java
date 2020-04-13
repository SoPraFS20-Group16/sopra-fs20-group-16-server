package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SETTLEMENT")
public class Settlement extends Building {

    private static final long serialVersionUID = 1L;


    @OneToOne
    private Coordinate coordinate;

    public Settlement() {
        super.setType(BuildingType.SETTLEMENT);
    }

    @Override
    public int getVictoryPoints() {
        return 1;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public List<ResourceCard> getPrice() {

        // create list for needed resource cards to pay for a settlement
        List<ResourceCard> price = new ArrayList<>();

        // establish amount of resources
        int lumberRequired = 1;
        int brickRequired = 1;
        int woolRequired = 1;
        int grainRequired = 1;

        // create resource card instances and add them to price list
        for(int i = 0; i <= lumberRequired; i++) {
            ResourceCard lumber = new ResourceCard();
            lumber.setResourceType(ResourceType.LUMBER);
            price.add(lumber);
        }

        for(int i = 0; i <= brickRequired; i++) {
            ResourceCard brick = new ResourceCard();
            brick.setResourceType(ResourceType.BRICK);
            price.add(brick);
        }

        for(int i = 0; i <= woolRequired; i++) {
            ResourceCard wool = new ResourceCard();
            wool.setResourceType(ResourceType.WOOL);
            price.add(wool);
        }

        for(int i = 0; i <= grainRequired; i++) {
            ResourceCard grain = new ResourceCard();
            grain.setResourceType(ResourceType.GRAIN);
            price.add(grain);
        }

        // return the list
        return price;
    }

}
