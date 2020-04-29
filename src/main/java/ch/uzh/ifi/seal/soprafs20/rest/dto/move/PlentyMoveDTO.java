package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

public class PlentyMoveDTO extends MoveDTO {

    private ResourceType plentyType1;
    private ResourceType plentyType2;

    public ResourceType getPlentyType1() {
        return plentyType1;
    }

    public void setPlentyType1(ResourceType plentyType1) {
        this.plentyType1 = plentyType1;
    }

    public ResourceType getPlentyType2() {
        return plentyType2;
    }

    public void setPlentyType2(ResourceType plentyType2) {
        this.plentyType2 = plentyType2;
    }
}
