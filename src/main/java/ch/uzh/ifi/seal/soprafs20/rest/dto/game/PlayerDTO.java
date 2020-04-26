package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;

import java.util.List;

public class PlayerDTO {

    private String username;

    private Long userId;

    private ResourceWallet resourceWallet;

    private List<DevelopmentCard> developmentCards;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ResourceWallet getResourceWallet() {
        return resourceWallet;
    }

    public void setResourceWallet(ResourceWallet resourceWallet) {
        this.resourceWallet = resourceWallet;
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
