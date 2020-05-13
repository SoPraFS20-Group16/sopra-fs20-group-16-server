package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

public class AbstractGameDTO {

    private String dtoType;

    public AbstractGameDTO(Class<? extends AbstractGameDTO> dtoClass) {
        this.dtoType = dtoClass.getSimpleName();
    }

    public String getDtoType() {
        return dtoType;
    }
}
