package ch.uzh.ifi.seal.soprafs20.rest.dto.user;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

public class UserGetDTO {

    private Long userId;
    private String username;
    private UserStatus status;

    private Boolean tracking;
    private UserLocationDTO location;

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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserLocationDTO getLocation() {
        return location;
    }

    public void setLocation(UserLocationDTO location) {
        this.location = location;
    }

    public Boolean getTracking() {
        return tracking;
    }

    public void setTracking(Boolean tracking) {
        this.tracking = tracking;
    }
}
