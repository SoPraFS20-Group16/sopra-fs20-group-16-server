package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROAD")
public class Road extends Building {

    private static final long serialVersionUID = 1L;

    @OneToOne
    Coordinate coordinate1;
    @OneToOne
    Coordinate coordinate2;
    @Id
    @GeneratedValue
    private Long id;

    @Override
    public int getVictoryPoints() {
        return 0;
    }

    @Override
    public List<ResourceCard> getPrice() {

        // create list for needed resource cards to pay for a road
        List<ResourceCard> price = new ArrayList<>();

        // establish amount of resources
        int brickRequired = 2;
        int lumberRequired = 3;

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

        // return the list
        return price;
    }

}
