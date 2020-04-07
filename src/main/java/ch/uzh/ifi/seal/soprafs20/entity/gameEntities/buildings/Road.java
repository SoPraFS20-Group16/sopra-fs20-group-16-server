package ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ROAD")
public class Road extends Building {

    private static final long serialVersionUID = 1L;

    public Road() {
        super.setType(BuildingType.ROAD);
    }


    @Override
    public int getVictoryPoints() {
        return 0;
    }
}
