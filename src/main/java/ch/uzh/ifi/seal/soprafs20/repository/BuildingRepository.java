package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.BuildingType;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.buildings.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("buildingRepository")
public interface BuildingRepository extends JpaRepository<Building, Long> {

    List<Building> findAllByType(BuildingType type);
}
