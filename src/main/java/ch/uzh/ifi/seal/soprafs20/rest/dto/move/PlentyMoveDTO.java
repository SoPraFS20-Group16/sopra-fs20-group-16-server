package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

import ch.uzh.ifi.seal.soprafs20.constant.PlentyType;

public class PlentyMoveDTO extends MoveDTO {

    private PlentyType plentyType;

    public PlentyType getPlentyType() {
        return plentyType;
    }

    public void setPlentyType(PlentyType plentyType) {
        this.plentyType = plentyType;
    }
}
