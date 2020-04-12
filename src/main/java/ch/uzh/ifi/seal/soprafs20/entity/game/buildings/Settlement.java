package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.entity.game.cards.ResourceCard;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "SETTLEMENT")
public class Settlement extends Building {

    private static final long serialVersionUID = 1L;


    @OneToOne
    private Coordinate coordinate;

    public Settlement() {
        super.setType(BuildingType.SETTLEMENT);
    }

    @Override
    public int getVictoryPoints() {
        return 1;
    }

    @Override
    public List<ResourceCard> getPrice() {

        //TODO: FIX GET PRICE!
        return null;
    }
}
