package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.Coordinate;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.Settlement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BuildingRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("buildingRepository")
    @Autowired
    private BuildingRepository buildingRepository;

    @Test
    public void testFindAllByType_success() {

        //given
        Coordinate coord = new Coordinate();

        Road road = new Road();
        road.setCoordinate(coord);
        Settlement settlement = new Settlement();
        settlement.setCoordinate(coord);
        City city = new City();
        city.setCoordinate(coord);

        entityManager.persist(coord);
        entityManager.persist(road);
        entityManager.persist(settlement);
        entityManager.persist(city);

        List<Building> rResult = buildingRepository.findAllByType(BuildingType.ROAD);
        List<Building> sResult = buildingRepository.findAllByType(BuildingType.SETTLEMENT);
        List<Building> cResult = buildingRepository.findAllByType(BuildingType.CITY);

        assertEquals(1, rResult.size(), "The array should be of size 1!");
        assertEquals(1, sResult.size(), "The array should be of size 1!");
        assertEquals(1, cResult.size(), "The array should be of size 1!");

        assertEquals(Road.class, rResult.get(0).getClass(), "The element should be of class Road!");
        assertEquals(Settlement.class, sResult.get(0).getClass(), "The element should be of class Settlement!");
        assertEquals(City.class, cResult.get(0).getClass(), "The element should be of class City!");
    }
}
