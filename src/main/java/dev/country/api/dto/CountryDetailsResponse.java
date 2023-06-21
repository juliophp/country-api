package dev.country.api.dto;


import dev.country.api.dto.response.Location;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;


@ApiModel(description = "Class representing a response for country details")
@Value
@AllArgsConstructor
public class CountryDetailsResponse {


    @ApiModelProperty(required = true)
    @NotNull
    String population;

    @ApiModelProperty(required = true)
    String capitalCity;

    @ApiModelProperty(required = true)
    Location location;


    @ApiModelProperty(required = true)
    String currency;

    @ApiModelProperty(required = true)
    String ISO2;

    @ApiModelProperty(required = true)
    String ISO3;

    @Builder
    private static CountryDetailsResponse newResponseTo(
                                                    final String population,
                                                    final String capitalCity,
                                                    final Location location,
                                                    final String currency,
                                                    final String ISO2,
                                                    final String ISO3
    ) {
        return new CountryDetailsResponse(population, capitalCity, location, currency, ISO2, ISO3);
    }



}
