package ch.uzh.ifi.seal.soprafs20.entity.game.cards;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;


@Entity
@Table(name = "DEVELOPMENT_CARD")
public class DevelopmentCard extends Card {


    private static final long serialVersionUID = 1L;


    @Column(updatable = false, nullable = false)
    private DevelopmentType developmentType;

    //getters and setters
    public DevelopmentType getDevelopmentType() {
        return developmentType;
    }

    public void setDevelopmentType(DevelopmentType developmentType) {
        this.developmentType = developmentType;
    }

    public List<ResourceCard> getPrice() {
        //TODO: IMPLEMENT getPrice in DEV CARD
        return null;
    }


}