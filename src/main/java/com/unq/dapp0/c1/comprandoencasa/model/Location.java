package com.unq.dapp0.c1.comprandoencasa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.atan2;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.floor;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;

@Entity
@Table
public class Location {
    private final Double EARTH_RADIUS = 6371.0;
    private final Double MINUTES_PER_KM = 2.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Double latitudeRadians;

    @Column
    private Double longitudeRadians;


    public Location() {}

    public Location(String address, Double latitude, Double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;

        this.latitudeRadians = deg2rad(latitude);
        this.longitudeRadians = deg2rad(longitude);
    }

    public Long getID() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Double distanceTo(Location aLocation){
        Double dLat = deg2rad(aLocation.latitude - this.latitude);
        Double dLon = deg2rad(aLocation.longitude - this.longitude);
        Double temp1 = pow(sin(dLat/2), 2.0);
        Double temp2 = cos(deg2rad(this.latitude)) * cos(deg2rad(aLocation.latitude)) * pow(sin(dLon/2), 2.0);
        Double a = temp1 + temp2;
        Double aTan = 2 * atan2(sqrt(a), sqrt(1 - a));
        Double res = aTan * EARTH_RADIUS;
        return floor(res * 100) / 100;
    }

    private Double deg2rad(Double dec){
        return dec * (PI / 180);
    }

    public Double timeTo(Location location) {
        return this.distanceTo(location) * MINUTES_PER_KM;
    }
}
