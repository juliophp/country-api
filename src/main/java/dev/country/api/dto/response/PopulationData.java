package dev.country.api.dto.response;

import java.util.ArrayList;

public class PopulationData {
    public String country;
    public String code;
    public String iso3;
    public ArrayList<PopulationCount> populationCounts;
}
