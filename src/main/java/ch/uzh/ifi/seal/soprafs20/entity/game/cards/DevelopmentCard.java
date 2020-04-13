package ch.uzh.ifi.seal.soprafs20.entity.game.cards;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "DEVELOPMENT_CARD")
public class DevelopmentCard extends Card {


    private static final long serialVersionUID = 1L;


    @Column(updatable = false, nullable = false)
    private DevelopmentType developmentType;

    //getters and setters
    public DevelopmentType getDevelopmentType() {
        return developmentType;
    }

    public void setDevelopmentType(DevelopmentType developmentType) {
        this.developmentType = developmentType;
    }

    public List<ResourceCard> getPrice() {

        // create list for needed resource cards to pay for a development card
        List<ResourceCard> price = new ArrayList<>();

        // establish amount of resources
        int oreRequired = 1;
        int grainRequired = 1;
        int woolRequired = 1;

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

        for(int i = 0; i <= woolRequired; i++) {
            ResourceCard wool = new ResourceCard();
            wool.setResourceType(ResourceType.WOOL);
            price.add(wool);
        }

        // return the list
        return price;
    }


}