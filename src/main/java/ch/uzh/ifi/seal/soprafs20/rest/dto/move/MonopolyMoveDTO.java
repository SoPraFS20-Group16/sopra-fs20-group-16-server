package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

public class MonopolyMoveDTO extends MoveDTO {

    private ResourceType monopolyType;

    public ResourceType getMonopolyType() {
        return monopolyType;
    }

    public void setMonopolyType(ResourceType monopolyType) {
        this.monopolyType = monopolyType;
    }

}
