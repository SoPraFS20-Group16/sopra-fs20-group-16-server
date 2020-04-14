package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;

import java.util.List;

public class PlayerDTO {

    private String username;

    private Long userId;

    private List<ResourceCard> resourceCards;

    private List<DevelopmentCard> developmentCards;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ResourceCard> getResourceCards() {
        return resourceCards;
    }

    public void setResourceCards(List<ResourceCard> resourceCards) {
        this.resourceCards = resourceCards;
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(List<DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
