package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

public class TradeMoveDTO extends MoveDTO {

    private ResourceType neededType;
    private ResourceType offeredType;

    public ResourceType getOfferedType() {
        return offeredType;
    }

    public void setOfferedType(ResourceType offeredType) {
        this.offeredType = offeredType;
    }

    public ResourceType getNeededType() {
        return neededType;
    }

    public void setNeededType(ResourceType neededType) {
        this.neededType = neededType;
    }
}
