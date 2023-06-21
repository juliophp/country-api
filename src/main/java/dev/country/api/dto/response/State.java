package dev.country.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class State {
    public String name;
    public String state_code;

    public ArrayList<String> cities = new ArrayList<>();

}
