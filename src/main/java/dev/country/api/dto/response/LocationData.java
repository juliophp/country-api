package dev.country.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationData {
    public String name;
    public String iso2;
    @JsonProperty("long")
    public int mylong;
    public int lat;
}
