package ch.uzh.ifi.seal.soprafs20.entity.history;

import ch.uzh.ifi.seal.soprafs20.rest.dto.history.MoveHistoryDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;

import javax.persistence.*;

@Entity
@Table(name = "MOVE_HISTORY")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class MoveHistory {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long userId;

    private String username;

    @Column
    private String moveName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public MoveHistoryDTO getDTO() {
        return DTOMapper.INSTANCE.convertMoveHistoryToMoveHistoryDTO(this);
    }
}
