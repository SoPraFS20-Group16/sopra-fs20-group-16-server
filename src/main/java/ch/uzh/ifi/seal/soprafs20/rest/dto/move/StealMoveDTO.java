package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

public class StealMoveDTO extends MoveDTO {

    private Long victimId;

    public Long getVictimId() {
        return victimId;
    }

    public void setVictimId(Long victimId) {
        this.victimId = victimId;
    }
}
