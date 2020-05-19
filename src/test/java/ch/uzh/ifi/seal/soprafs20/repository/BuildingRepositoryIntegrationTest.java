package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Building;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.City;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Road;
import ch.uzh.ifi.seal.soprafs20.entity.game.buildings.Settlement;
import ch.uzh.ifi.seal.soprafs20.entity.game.coordinate.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BuildingRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("buildingRepository")
    @Autowired
    private BuildingRepository buildingRepository;

    @AfterEach
    void teardown() {
        buildingRepository.deleteAll();
    }

    @Test
    void testFindAllByType_success() {

        //given
        Coordinate coord = new Coordinate(1, 1);
        Coordinate coord2 = new Coordinate(1, 2);

        Road road = new Road();
        road.setCoordinate1(coord);
        road.setCoordinate2(coord2);
        road.setUserId(1L);

        Settlement settlement = new Settlement();
        settlement.setCoordinate(coord);
        settlement.setUserId(1L);

        City city = new City();
        city.setCoordinate(coord);
        city.setUserId(1L);

        entityManager.persist(coord);
        entityManager.persist(road);
        entityManager.persist(settlement);
        entityManager.persist(city);
        entityManager.persist(coord2);
        entityManager.flush();

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
