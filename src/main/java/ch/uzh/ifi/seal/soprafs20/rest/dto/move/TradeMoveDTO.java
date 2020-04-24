package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

public class TradeMoveDTO extends MoveDTO {

    private ResourceType neededType;
    private ResourceType tradedType;

    public void setTradedType(ResourceType tradedType) {
        this.tradedType = tradedType;
    }

    public ResourceType getTradedType() {
        return tradedType;
    }

    public void setNeededType(ResourceType neededType) {
        this.neededType = neededType;
    }

    public ResourceType getNeededType() {
        return neededType;
    }
}
