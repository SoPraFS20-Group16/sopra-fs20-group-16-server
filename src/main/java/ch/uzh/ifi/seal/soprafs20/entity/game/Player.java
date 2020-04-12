package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.Card;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.service.PlayerService;

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
    private List<Card> cards;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Road> roads;
    @OneToMany(cascade = CascadeType.ALL)
    private List<City> cities;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Settlement> settlements;
    @Column
    private int victoryPoints;

    private final PlayerService playerService;

    public Player() {

        //Set up empty arrays
        cards = new ArrayList<>();
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        cards.add(card);
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

    // Worker Methods

    public void buyRoad() {

        // if player can afford a new road, it will be created
        Road newRoad = playerService.RoadRequest(this.cards);

        // add new Road to inventory and place it on the board
        addRoad(newRoad);
        placeRoad();

        // increase victoryPoints
        this.victoryPoints += newRoad.getVictoryPoints();
    }

    public void buySettlement() {

        // if player can afford a new Settlement, it will be created
        Settlement newSettlement = playerService.SettlementRequest(this.cards);

        // add new Settlement to inventory and place it on the board
        addSettlement(newSettlement);
        placeSettlement();

        // increase victoryPoints
        this.victoryPoints += newSettlement.getVictoryPoints();
    }

    public void buyCity() {

        // if player can afford a new City, it will be created
        City newCity = playerService.CityRequest(this.cards);

        // add City to inventory and place it on the board
        addCity(newCity);
        placeCity();

        // increase victoryPoints
        this.victoryPoints += newCity.getVictoryPoints();

        //TODO: remove or replace settlement in inventory
        //TODO: adjust victoryPoints accordingly
    }

    public void invokeDevelopmentCard(DevelopmentCard developmentCard) {

        switch (developmentCard.getDevelopmentType()) {
            case VICTORYPOINT:
                this.victoryPoints += 1;
                break;
            case PLENTYPROGRESS:
                //TODO: add logic
                break;
            case ROADPROGRESS:
                //TODO: add logic
                break;
            case KNIGHT:
                //TODO: add logic
                break;
            case MONOPOLYPROGRESS:
                //TODO: add logic
                break;
        }

        this.cards.remove(developmentCard);

    }

    public void passOnMove() {

    }

    public void tradeWithBank() {

    }

    public void placeRoad(Road road) {

    }

    public void placeSettlement(Settlement settlement) {

    }

    public void placeCity(City city) {

    }


}
