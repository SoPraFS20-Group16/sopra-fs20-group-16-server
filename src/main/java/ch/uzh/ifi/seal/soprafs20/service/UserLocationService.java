package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.api.IpStackRequest;
import ch.uzh.ifi.seal.soprafs20.entity.UserLocation;
import ch.uzh.ifi.seal.soprafs20.repository.UserLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * The type User location service.
 */
@Service
@Transactional
public class UserLocationService {

    private final UserLocationRepository userLocationRepository;

    /**
     * Instantiates a new User location service.
     *
     * @param userLocationRepository the user location repository
     */
    @Autowired
    public UserLocationService(@Qualifier("userLocationRepository") UserLocationRepository userLocationRepository) {

        this.userLocationRepository = userLocationRepository;
    }

    /**
     * Create user location
     *
     * @param userId         the user id
     * @param allowsTracking the allows tracking
     * @param ipAddress      the ip address
     * @return the user location
     */
    public UserLocation createUserLocation(Long userId, boolean allowsTracking, String ipAddress) {

        UserLocation location = new UserLocation();
        location.setUserId(userId);
        location.setUserAllowsTracking(allowsTracking);

        save(location);

        return updateUserLocation(ipAddress, userId);
    }

    /**
     * Save user location.
     *
     * @param userLocation the user location
     * @return the user location
     */
    public UserLocation save(UserLocation userLocation) {
        return userLocationRepository.saveAndFlush(userLocation);
    }

    /**
     * Update user location for given user with the given Address.
     *
     * @param ipAddress the ip address
     * @param userId    the user id
     * @return the user location
     */
    public UserLocation updateUserLocation(String ipAddress, Long userId) {

        UserLocation location = findByUserId(userId);

        //If user does not allow tracking do not make call to external API
        if (!location.isUserAllowsTracking()) {
            return location;
        }

        // test ip address "2a02:1206:4544:3000:994:4ed3:5028:ae1f"

        // get geolocation information from external API, based on IP address
        IpStackRequest ipStackRequest = new IpStackRequest(ipAddress);
        ipStackRequest.makeRequest();

        if (!ipStackRequest.isSuccess()) {
            return location;
        }

        // set fields of location object
        location.setZipCode(ipStackRequest.getZipCode());
        location.setCity(ipStackRequest.getCity());
        location.setCountryName(ipStackRequest.getCountry());
        location.setLongitude(ipStackRequest.getLongitude());
        location.setLatitude(ipStackRequest.getLatitude());

        return save(location);
    }

    /**
     * Find the UserLocation for the given user
     *
     * @param userId the userId
     * @return the found user location
     */
    private UserLocation findByUserId(Long userId) {
        return userLocationRepository.findByUserId(userId);
    }
}