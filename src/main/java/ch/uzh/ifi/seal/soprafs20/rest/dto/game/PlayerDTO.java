package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;

import java.util.List;

public class PlayerDTO {

    private String username;

    private Long userId;

    private ResourceDTO resources;

    private List<DevelopmentCard> developmentCards;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ResourceDTO getResources() {
        return resources;
    }

    public void setResources(ResourceDTO resources) {
        this.resources = resources;
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(List<DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }
}
