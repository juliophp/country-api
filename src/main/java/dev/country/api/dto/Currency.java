package dev.country.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Currency {
    String targetCurrency;
    String sourceCurrency;
    Double rate;

}
