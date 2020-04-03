package ch.uzh.ifi.seal.soprafs20.rest.dto;

/**
 * The type MoveDTO holds the moveId and the userToken
 * <p>
 * the permission can be verified via the toke if it matches the Moves playerId
 * the MoveDTO can identify the corresponding Move with the moveId
 */
public class MovePostDTO {

    private Long moveId;
    private String token;

    public Long getMoveId() {
        return moveId;
    }

    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
