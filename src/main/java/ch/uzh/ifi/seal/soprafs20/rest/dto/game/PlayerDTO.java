package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

public class PlayerDTO {

    private String username;

    private Long userId;

    private ResourceDTO resources;

    private DevelopmentCardsDTO developmentCards;


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

    public DevelopmentCardsDTO getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(DevelopmentCardsDTO developmentCards) {
        this.developmentCards = developmentCards;
    }
}
