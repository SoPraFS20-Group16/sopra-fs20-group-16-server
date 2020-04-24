package ch.uzh.ifi.seal.soprafs20.rest.dto.move;

import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;

public class CardMoveDTO extends MoveDTO {

    private DevelopmentCard developmentCard;

    public void setDevelopmentCard(DevelopmentCard developmentCard) {
        this.developmentCard = developmentCard;
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }
}