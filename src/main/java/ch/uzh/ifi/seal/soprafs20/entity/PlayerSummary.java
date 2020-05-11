package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;

@Entity
@Table(name = "PLAYER_SUMMARY")
public class PlayerSummary {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long userId;

    @Column
    private String username;

    @Column
    private int points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
