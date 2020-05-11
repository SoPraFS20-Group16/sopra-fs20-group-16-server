package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userLocationRepository")
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    UserLocation findByUserId(Long userId);
}