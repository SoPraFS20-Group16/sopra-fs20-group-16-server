package ch.uzh.ifi.seal.soprafs20.entity.game.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CITY")
public class City extends Building {

    private static final long serialVersionUID = 1L;


    public City() {
        super.setType(BuildingType.CITY);
    }


    @Override
    public int getVictoryPoints() {
        return 2;
    }
}
