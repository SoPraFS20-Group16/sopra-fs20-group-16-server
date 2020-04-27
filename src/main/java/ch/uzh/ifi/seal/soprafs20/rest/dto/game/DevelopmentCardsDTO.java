package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;

import java.util.EnumMap;
import java.util.List;

public class DevelopmentCardsDTO extends EnumMap<DevelopmentType, Integer> {


    public DevelopmentCardsDTO(List<DevelopmentCard> cards) {
        super(DevelopmentType.class);
        for (DevelopmentType type : DevelopmentType.values()) {
            this.put(type, 0);
        }
        this.mapCards(cards);
    }

    private void mapCards(List<DevelopmentCard> cards) {
        for (DevelopmentCard card : cards) {
            int previous = get(card.getDevelopmentType());
            put(card.getDevelopmentType(), (previous + 1));
        }
    }
}
