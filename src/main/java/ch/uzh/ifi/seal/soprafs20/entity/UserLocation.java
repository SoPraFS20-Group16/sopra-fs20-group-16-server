package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "USER_LOCATION")
public class UserLocation implements Serializable {

    @Id
    @Column
    Long userId;

    @Column
    private boolean userAllowsTracking;

    @Column
    private String countryName;

    @Column
    private String city;

    @Column
    private Integer zipCode;

    @Column
    private Float longitude;

    @Column
    private Float latitude;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public boolean isUserAllowsTracking() {
        return userAllowsTracking;
    }

    public void setUserAllowsTracking(boolean userAllowsTracking) {
        this.userAllowsTracking = userAllowsTracking;
    }
}