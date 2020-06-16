package com.unq.dapp0.c1.comprandoencasa.model;


public class LocationBuilder{
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
