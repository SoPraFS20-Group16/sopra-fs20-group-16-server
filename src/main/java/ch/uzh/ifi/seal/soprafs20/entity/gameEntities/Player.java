package ch.uzh.ifi.seal.soprafs20.entity.gameEntities;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.cards.Card;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYER")
public class Player extends User {

    private static final long serialVersionUID = 1L;
    @OneToMany
    List<Card> cards;
    @OneToMany
    List<Road> roads;
    @OneToMany
    List<City> cities;
    @OneToMany
    List<Settlement> settlements;
    @Column
    private int victoryPoints;

    //TODO: Check if it is possible to delete a player without also destroying the user
    //User should exist forever, player should be deleted after the game
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

    // Worker Methods
    //TODO: Implement the Player functionality


}
