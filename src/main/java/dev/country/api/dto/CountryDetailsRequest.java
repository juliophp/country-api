package dev.country.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;


@ApiModel(description = "Class representing name of country to fetch details for")
@Value
@Builder
public class CountryDetailsRequest {
    @ApiModelProperty(required = true)
    @NotEmpty
    public String country;
    public String state;

}
