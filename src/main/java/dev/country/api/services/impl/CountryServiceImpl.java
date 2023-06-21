package dev.country.api.services.impl;

import com.google.common.collect.Lists;
import dev.country.api.dto.*;
import dev.country.api.dto.request.FilterCityRequest;
import dev.country.api.dto.response.*;
import dev.country.api.exception.NotFoundException;
import dev.country.api.exception.ThirdPartyAPIException;
import dev.country.api.services.CountryService;
import dev.country.api.util.CsvUtil;
import dev.country.api.util.FutureUtil;
import dev.country.api.util.GenericRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    private final GenericRestClient restClient;
    private final FutureUtil futureUtil;
    private final String baseUrl = "https://countriesnow.space";
    private final String path = "api/v0.1/countries/";

    @Value("classpath:exchange_rate.csv")
    private Resource dataFile;
    public CountryServiceImpl(final GenericRestClient restClient,
                              final FutureUtil futureUtil){
        this.restClient = restClient;
        this.futureUtil = futureUtil;
    }
    @Override
    public List<Datum> getTopNCitiesByPopulationForListOfCountries(final Integer numberOfCities) {
        //create a list of countries to filter for
        var countries = List.of("Italy", "New Zealand", "Ghana");

        //function that receives individual countries as params and returns the filter response for that country
        Function<String, SingleCountryFilterResponse> postFilter = (country) -> {
            var requestBody = new FilterCityRequest(country, "population", "desc", numberOfCities);
            return restClient.post(baseUrl, path + "population/cities/filter", requestBody,
                    SingleCountryFilterResponse.class);
        };

        //for each of the countries convert the functions above to a callable
        var filterCallables =  countries.stream().map(country ->
                (Callable<SingleCountryFilterResponse>)() -> postFilter.apply(country)
        );

        //submit the list of tasks to the threadpool
        List<Future<SingleCountryFilterResponse>> result = futureUtil.invokeMultipleTask(filterCallables.toList());

        //get results from the API for each of the countries and add them to list
        return result.stream().flatMap(i -> futureUtil.getResult(i).data.stream()).toList();
    }

    @Override
    public CountryDetailsResponse getCountryDetailsByCountryName(String countryName) {
        //wrap api operations around callable and submit task to task executor
        var requestBody = CountryDetailsRequest.builder().country(countryName).build();

        Future<SingleCountryPopulationResponse> populationFuture = futureUtil.invokeSingleTask(() ->
                restClient.post(baseUrl, path+"population", requestBody, SingleCountryPopulationResponse.class));
        Future<SingleCountryAndCapitalResponse> capitalFuture = futureUtil.invokeSingleTask(() ->
                restClient.post(baseUrl, path+"capital", requestBody, SingleCountryAndCapitalResponse.class));
        Future<SingleCountryLocationResponse> locationFuture = futureUtil.invokeSingleTask(() ->
                restClient.post(baseUrl, path+"positions", requestBody, SingleCountryLocationResponse.class));
        Future<SingleCountryCurrencyResponse> currencyFuture = futureUtil.invokeSingleTask(() ->
                restClient.post(baseUrl, path+"currency", requestBody, SingleCountryCurrencyResponse.class));

        //get network results from future
        var population = futureUtil.getResult(populationFuture);
        var capital = futureUtil.getResult(capitalFuture);
        var location = futureUtil.getResult(locationFuture);
        var currency = futureUtil.getResult(currencyFuture);
        var responseBuilder =  CountryDetailsResponse.builder();

        //get the most recent population and set response using builder pattern
        var populationArraySize = population.data.populationCounts.size();
        if (!population.error && !population.data.populationCounts.isEmpty()){
            responseBuilder.population(population.data.populationCounts.get(populationArraySize-1).value);
        }
        if (!capital.error && capital.data != null){
            responseBuilder.capitalCity(capital.data.capital);
            responseBuilder.ISO2(capital.data.iso2);
            responseBuilder.ISO3(capital.data.iso3);
        }
        if (!location.error && location.data!=null){
            responseBuilder.location(new Location(location.data.mylong, location.data.lat));
        }
        if (!currency.error && currency.data!=null){
            responseBuilder.currency(currency.data.currency);
        }
        return responseBuilder.build();
    }

    @Override
    public List<State> getAllStatesWithCitiesByCountryName(String countryName) {
        var requestBody = CountryDetailsRequest.builder().country(countryName);
        var states = restClient.post(baseUrl, path+"states", requestBody.build(), SingleCountryStatesResponse.class);

        //state and city list to hold values from countries API
        List<State> statesAndCitiesResponse = new ArrayList<>();

        //function that takes in two parameters current state and function that posts API
        BiFunction<State, Function<State, CitiesInStateResponse>, State> callEndpointForState = (j, postApi) -> {
            var resp =  postApi.apply(j);
            var cityResponse = resp.data != null ? resp.data : new ArrayList<String>();
            return State.builder().name(j.name).state_code(j.state_code).cities(cityResponse).build();
        };

        //execute function declared above only if states are returned from the API and states are not empty
        if (!states.error && !states.data.states.isEmpty()){
            //split the data in chunks of 5, using google guava library
            List<List<State>> sublist = Lists.partition(states.data.states, 5);
            //call APIs in batches for each chunks
            sublist.forEach(i -> {
                Function<State, CitiesInStateResponse> post = (state) -> {
                    var request = CountryStateRequest.builder().state(state.name).country(countryName).build();
                    return restClient.post(baseUrl, path + "state/cities", request, CitiesInStateResponse.class);
                };
                var callables = i.stream().map(j -> (Callable<State>)() -> callEndpointForState.apply(j,post)).toList();
                var tasks = futureUtil.invokeMultipleTask(callables);
                var results = tasks.stream().map(futureUtil::getResult).toList();
                statesAndCitiesResponse.addAll(results);
            });
        }
        return statesAndCitiesResponse;
    }

    @Override
    public CurrencyResponse getConversionByCountryNameAmountAndTargetCurrency(String countryName, double amount, String targetCurrency) {
        var currencies  = new CsvUtil<Currency>().readFile(dataFile, Currency.class);
        var requestBody = CountryDetailsRequest.builder().country(countryName).build();
        var response = restClient.post(baseUrl, path + "currency", requestBody, SingleCountryCurrencyResponse.class);
        if (response.error) throw new ThirdPartyAPIException(response.msg);
        var currencyResponseBuilder = CurrencyResponse.builder();
        var currencyPair = currencies.stream()
                .filter(
                        i -> i.getTargetCurrency().equals(targetCurrency) && response.data.currency.equals(i.getSourceCurrency())
                ).findFirst();
        return currencyPair.map(currency -> {
            currencyResponseBuilder.countryCurrency(response.data.currency);
            currencyResponseBuilder.targetCurrency(targetCurrency);
            currencyResponseBuilder.targetAmount(currency.getRate() * amount);
            return currencyResponseBuilder.build();
        }).orElseThrow(() -> new NotFoundException("currency pair not found"));
    }


}
