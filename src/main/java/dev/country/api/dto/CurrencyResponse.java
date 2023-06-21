package dev.country.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


@ApiModel(description = "Class representing conversion for selected country's currency")
@Value
@AllArgsConstructor
public class CurrencyResponse {

    @ApiModelProperty(position = 1, required = true)
    String countryCurrency;

    @ApiModelProperty(position = 2, required = true)
    Double targetAmount;
 @ApiModelProperty(position = 2, required = true)
    String targetCurrency;

    @Builder
    private static CurrencyResponse newCurrencyTo(final String countryCurrency,
                                              final Double targetAmount,
                                                  final String targetCurrency

    ) {
        return new CurrencyResponse(countryCurrency, targetAmount, targetCurrency);
    }

}
