package ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SETTLEMENT")
public class Settlement extends Building {

    private static final long serialVersionUID = 1L;


    public Settlement() {
        super.setType(BuildingType.SETTLEMENT);
    }

    @Override
    public int getVictoryPoints() {
        return 1;
    }
}
