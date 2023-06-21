package dev.country.api.dto.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ApiModel(description = "Class representing the request class for currency conversion")
@Value
@AllArgsConstructor
public class CountryCurrencyRequest {


    @ApiModelProperty(position = 1, required = true, example = "Italy")
    @NotEmpty(message = "country field is required")
    String country;

    @ApiModelProperty(position = 2,  required = true)
    @NotNull(message = "amount must nut be null")
    Double amount;

    @ApiModelProperty(position = 3, required = true, example = "NGN")
            @NotEmpty(message = "targetCurrency must exist in the request")
    String targetCurrency;



    @Builder
    private static CountryCurrencyRequest newCurrencyRequestTo(final String country,
                                                    final Double amount,
                                                    final String targetCurrency
    ) {
        return new CountryCurrencyRequest(country, amount, targetCurrency);
    }

}
