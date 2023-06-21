package dev.country.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    @JsonProperty("long")
    public int mylong;
    public int lat;

    public Location(int mylong, int lat) {
        this.mylong = mylong;
        this.lat = lat;
    }
}
