package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROAD")
public class Road extends Building {

    private static final long serialVersionUID = 1L;

    @OneToOne
    Coordinate coordinate1;
    @OneToOne
    Coordinate coordinate2;
    @Id
    @GeneratedValue
    private Long id;

    @Override
    public int getVictoryPoints() {
        return 0;
    }

    @Override
    public List<ResourceCard> getPrice() {
        //TODO: FIX GET PRICE!
        return null;
    }
}
