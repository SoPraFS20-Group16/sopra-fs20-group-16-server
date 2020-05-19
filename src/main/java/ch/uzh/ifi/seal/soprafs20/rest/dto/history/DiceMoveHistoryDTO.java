package ch.uzh.ifi.seal.soprafs20.rest.dto.history;

public class DiceMoveHistoryDTO extends MoveHistoryDTO {

    private int roll = 0;

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }
}
