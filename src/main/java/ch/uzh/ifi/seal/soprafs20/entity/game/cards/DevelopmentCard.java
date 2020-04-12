package ch.uzh.ifi.seal.soprafs20.entity.game.cards;

import ch.uzh.ifi.seal.soprafs20.constant.CardType;
import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "DEVELOPMENT_CARD")
public class DevelopmentCard extends Card {


    private static final long serialVersionUID = 1L;


    @Column(updatable = false, nullable = false)
    private DevelopmentType developmentType;


    public DevelopmentCard() {
        super.setCardType(CardType.DEVELOPMENT);
    }

    //getters and setters
    public DevelopmentType getDevelopmentType() {
        return developmentType;
    }

    public void setDevelopmentType(DevelopmentType developmentType) {
        this.developmentType = developmentType;
    }


}