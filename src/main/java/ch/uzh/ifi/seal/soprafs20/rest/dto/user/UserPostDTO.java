package ch.uzh.ifi.seal.soprafs20.rest.dto.user;

public class UserPostDTO {

    private String username;
    private String password;

    private boolean tracking;

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
