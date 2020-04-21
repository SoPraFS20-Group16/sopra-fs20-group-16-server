package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class MoveDTO {

    private Long userId;

    private Long moveId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMoveId() {
        return moveId;
    }

    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }
}
