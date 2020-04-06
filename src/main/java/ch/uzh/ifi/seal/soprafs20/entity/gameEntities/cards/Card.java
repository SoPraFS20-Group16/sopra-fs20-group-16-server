package ch.uzh.ifi.seal.soprafs20.entity.gameEntities.cards;

import ch.uzh.ifi.seal.soprafs20.constant.CardType;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Card {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private CardType type;

    abstract boolean isResourceCard();

    abstract boolean isDevelopmentCard();

    //SharedMethods
    public void setCardType(CardType type) {
        this.type = type;
    }
}
