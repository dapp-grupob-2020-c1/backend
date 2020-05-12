package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTests {

    @Test
    public void aLocationHasAFormalAddressAndTheCorrespondingCoordinates(){
        String address = "Fake st. 123";
        Double latitude = 1D;
        Double longitude = 1D;

        Location location = LocationBuilder.anyLocation()
                .withAddress(address)
                .withCoordinates(latitude, longitude)
                .build();

        assertEquals(address, location.getAddress());
        assertEquals(latitude, location.getLatitude());
        assertEquals(longitude, location.getLongitude());
    }

    @Test
    public void aLocationCanCalculateItsDistanceToAnotherLocation(){
        Location unq = LocationBuilder.anyLocation()
                .withCoordinates(-34.706272, -58.278519)
                .build();

        Location obelisco = LocationBuilder.anyLocation()
                .withCoordinates(-34.603702, -58.381575)
                .build();

        Double distance = unq.distanceTo(obelisco);

        assertEquals(14.79, distance, 0.0001);
    }

    @Test
    public void aLocationCanCalculateTheEstimatedTimeToReachAnotherLocation(){
        Location unq = LocationBuilder.anyLocation()
                .withCoordinates(-34.706272, -58.278519)
                .build();

        Location obelisco = LocationBuilder.anyLocation()
                .withCoordinates(-34.603702, -58.381575)
                .build();

        Double time = unq.timeTo(obelisco);

        assertEquals(29.58, time);
    }

}

class LocationBuilder{
    private String address;
    private Double latitude;
    private Double longitude;

    public static LocationBuilder anyLocation(){
        return new LocationBuilder();
    }

    private LocationBuilder(){
        this.address = "Any";
        this.latitude = 1D;
        this.longitude = 1D;
    }

    public LocationBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public LocationBuilder withCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    public Location build() {
        return new Location(address, latitude, longitude);
    }
}