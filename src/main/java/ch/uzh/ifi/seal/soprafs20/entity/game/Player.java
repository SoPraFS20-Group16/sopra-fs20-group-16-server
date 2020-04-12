package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ResourceCard> resourceCards;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DevelopmentCard> developmentCards;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Road> roads;

    @OneToMany(cascade = CascadeType.ALL)
    private List<City> cities;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Settlement> settlements;

    @Column
    private int victoryPoints;


    public Player() {

        //Set up empty arrays
        resourceCards = new ArrayList<>();
        developmentCards = new ArrayList<>();
        roads = new ArrayList<>();
        cities = new ArrayList<>();
        settlements = new ArrayList<>();

    }

    //Getter and setters

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public List<ResourceCard> getResourceCards() {
        return resourceCards;
    }

    public void setResourceCards(List<ResourceCard> cards) {
        this.resourceCards = cards;
    }

    public void addResourceCard(ResourceCard resourceCard) {
        resourceCards.add(resourceCard);
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(List<DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public void addDevelopmentCard(DevelopmentCard developmentCard) {
        developmentCards.add(developmentCard);
    }

    public List<Road> getRoads() {
        return roads;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public void addRoad(Road road) {
        this.roads.add(road);
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public List<Settlement> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<Settlement> settlements) {
        this.settlements = settlements;
    }

    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
