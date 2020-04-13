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
@Table(name = "CITY")
public class City extends Building {

    private static final long serialVersionUID = 1L;

    @OneToOne
    private Coordinate coordinate;


    public City() {
        super.setType(BuildingType.CITY);
    }


    @Override
    public int getVictoryPoints() {
        return 2;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public List<ResourceCard> getPrice() {

        // create list for needed resource cards to pay for a city
        List<ResourceCard> price = new ArrayList<>();

        // establish amount of resources
        int oreRequired = 3;
        int grainRequired = 2;

        // create resource card instances and add them to price list
        for(int i = 0; i <= oreRequired; i++) {
            ResourceCard ore = new ResourceCard();
            ore.setResourceType(ResourceType.ORE);
            price.add(ore);
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
