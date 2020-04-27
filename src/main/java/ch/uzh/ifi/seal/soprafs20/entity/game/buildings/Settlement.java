package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingConstants;
import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    @Override
    public int getVictoryPoints() {
        return BuildingConstants.VICTORY_POINTS_SETTLEMENT;
    }

    @Override
    public int getResourceDistributingAmount() {
        return BuildingConstants.RESOURCE_DISTRIBUTING_AMOUNT_SETTLEMENT;
    }

    @Override
    public ResourceWallet getPrice() {

        // establish amount of resources
        int lumberRequired = 1;
        int brickRequired = 1;
        int woolRequired = 1;
        int grainRequired = 1;

        //Create new Wallet
        ResourceWallet price = new ResourceWallet();

        price.addResource(ResourceType.LUMBER, lumberRequired);
        price.addResource(ResourceType.BRICK, brickRequired);
        price.addResource(ResourceType.WOOL, woolRequired);
        price.addResource(ResourceType.GRAIN, grainRequired);

        return price;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinate);
        return coordinates;
    }

}
