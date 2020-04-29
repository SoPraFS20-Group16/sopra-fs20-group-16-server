package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

public class KnightMoveDTO extends MoveDTO {

    private Long tileId;

    public Long getTileId() {
        return tileId;
    }

    public void setTileId(Long tileId) {
        this.tileId = tileId;
    }
}
