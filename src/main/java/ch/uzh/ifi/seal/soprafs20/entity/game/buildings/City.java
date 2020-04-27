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
@Table(name = "CITY")
public class City extends Building {

    private static final long serialVersionUID = 1L;

    @OneToOne
    private Coordinate coordinate;


    public City() {
        super.setType(BuildingType.CITY);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    @Override
    public int getVictoryPoints() {
        return BuildingConstants.VICTORY_POINTS_CITY;
    }

    @Override
    public int getResourceDistributingAmount() {
        return BuildingConstants.RESOURCE_DISTRIBUTING_AMOUNT_CITY;
    }

    @Override
    public ResourceWallet getPrice() {

        // establish amount of resources
        int oreRequired = 3;
        int grainRequired = 2;

        // create new wallet
        ResourceWallet price = new ResourceWallet();
        price.addResource(ResourceType.ORE, oreRequired);
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
