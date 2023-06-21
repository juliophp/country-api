package dev.country.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;


@ApiModel(description = "Class representing name of country to fetch details for")
@Value
@Builder
public class CountryStateRequest {
    @ApiModelProperty(required = true, example = "Norway")
    @NotEmpty
    public String country;

    @ApiModelProperty(required = true, example = "Akhersus")
    @NotEmpty
    public String state;

}
