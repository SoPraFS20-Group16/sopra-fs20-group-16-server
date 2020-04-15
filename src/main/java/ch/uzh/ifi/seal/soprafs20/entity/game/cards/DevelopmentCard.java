package ch.uzh.ifi.seal.soprafs20.entity.game.cards;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


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

    public ResourceWallet getPrice() {

        // establish amount of resources
        int oreRequired = 1;
        int grainRequired = 1;
        int woolRequired = 1;

        //Create new Wallet
        ResourceWallet price = new ResourceWallet();
        price.addResource(ResourceType.ORE, oreRequired);
        price.addResource(ResourceType.GRAIN, grainRequired);
        price.addResource(ResourceType.WOOL, woolRequired);

        return price;
    }


}