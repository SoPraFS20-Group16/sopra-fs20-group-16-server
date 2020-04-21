package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

    @Override
    public int getBuildingFactor() {
        return 1;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
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

}
