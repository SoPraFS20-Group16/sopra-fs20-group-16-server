package ch.uzh.ifi.seal.soprafs20.entity.history;

import ch.uzh.ifi.seal.soprafs20.rest.dto.history.MoveHistoryDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DICE_MOVE_HISTORY")
public class DiceMoveHistory extends MoveHistory {

    private int roll = 0;

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    @Override
    public MoveHistoryDTO getDTO() {
        return DTOMapper.INSTANCE.convertDiceMoveHistoryToDiceMoveHistoryDTO(this);
    }
}
