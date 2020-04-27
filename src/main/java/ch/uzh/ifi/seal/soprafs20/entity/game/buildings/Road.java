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
@Table(name = "ROAD")
public class Road extends Building {

    private static final long serialVersionUID = 1L;

    @OneToOne
    Coordinate coordinate1;

    @OneToOne
    Coordinate coordinate2;

    public Road() {
        super.setType(BuildingType.ROAD);
    }

    @Override
    public int getVictoryPoints() {
        return BuildingConstants.VICTORY_POINTS_ROAD;
    }

    @Override
    public int getResourceDistributingAmount() {
        return BuildingConstants.RESOURCE_DISTRIBUTING_AMOUNT_ROAD;
    }

    @Override
    public ResourceWallet getPrice() {

        // establish amount of resources
        int brickRequired = 2;
        int lumberRequired = 3;

        //Create new wallet
        ResourceWallet price = new ResourceWallet();
        price.addResource(ResourceType.BRICK, brickRequired);
        price.addResource(ResourceType.LUMBER, lumberRequired);

        return price;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinate1);
        coordinates.add(coordinate2);
        return coordinates;
    }

    public Coordinate getCoordinate1() {
        return coordinate1;
    }

    public void setCoordinate1(Coordinate coordinate1) {
        this.coordinate1 = coordinate1;
    }

    public Coordinate getCoordinate2() {
        return coordinate2;
    }

    public void setCoordinate2(Coordinate coordinate2) {
        this.coordinate2 = coordinate2;
    }

}
