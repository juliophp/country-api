package dev.country.api.services;

import dev.country.api.dto.CountryDetailsResponse;
import dev.country.api.dto.CurrencyResponse;
import dev.country.api.dto.response.Datum;
import dev.country.api.dto.response.SingleCountryFilterResponse;
import dev.country.api.dto.response.State;

import java.util.List;

public interface CountryService {
    List<Datum> getTopNCitiesByPopulationForListOfCountries(final Integer numberOfCities);
    CountryDetailsResponse getCountryDetailsByCountryName(final String countryName);
    List<State> getAllStatesWithCitiesByCountryName(final String countryName);

    CurrencyResponse getConversionByCountryNameAmountAndTargetCurrency(final String countryName,
                                                                       final double amount, final String targetCurrency);

}
