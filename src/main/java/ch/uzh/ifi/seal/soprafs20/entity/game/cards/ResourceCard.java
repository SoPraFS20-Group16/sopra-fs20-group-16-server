package ch.uzh.ifi.seal.soprafs20.entity.game.cards;

import ch.uzh.ifi.seal.soprafs20.constant.CardType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RESOURCE_CARD")
public class ResourceCard extends Card {

    private static final long serialVersionUID = 1L;


    @Column(updatable = false, nullable = false)
    private ResourceType resourceType;

    public ResourceCard() {
        super.setCardType(CardType.RESOURCE);
    }

    //Getters and setters
    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    //Card Interface Methods
    @Override
    public boolean isResourceCard() {
        return true;
    }

    @Override
    public boolean isDevelopmentCard() {
        return false;
    }
}
