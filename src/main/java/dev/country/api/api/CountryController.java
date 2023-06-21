package dev.country.api.api;

import dev.country.api.dto.*;
import dev.country.api.dto.response.SingleCountryFilterResponse;
import dev.country.api.dto.response.State;
import dev.country.api.exception.BadRequestException;
import dev.country.api.dto.request.CountryCurrencyRequest;
import dev.country.api.services.CountryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(CountryController.PATH)
@Api(tags = "Countries")
@Slf4j
public class CountryController {
    static final String PATH = "countries";
    private final CountryService countryService;

    public CountryController(final CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/top-cities")
    public ResponseEntity<ListResponse> getTopCitiesPerListOfCountries(@RequestParam final Integer number){
        if (number == null) throw new BadRequestException("number parameter is required");
        log.debug("fetching top n cities: " + number);
        var countries = countryService.getTopNCitiesByPopulationForListOfCountries(number);
        var response = new ListResponse("00", "success", countries);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/country-details")
    public ResponseEntity<DataResponse> getDetailsForStates(@Valid @RequestBody final CountryDetailsRequest countryDetailsRequest,
                                                                      @ApiIgnore Errors errors) {
        if (errors.hasErrors()) throw new BadRequestException(errors.getAllErrors());
        log.debug("getting country details" + countryDetailsRequest);
        var details = countryService.getCountryDetailsByCountryName(countryDetailsRequest.getCountry());
        var response = new DataResponse("00", "success", details);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/cities-in-state")
    public ResponseEntity<ListResponse> getCitiesInState(@Valid @RequestBody final CountryDetailsRequest countryDetailsRequest,
                                                        @ApiIgnore Errors errors) {
        if (errors.hasErrors()) throw new BadRequestException(errors.getAllErrors());
        log.debug("getting cities in state" + countryDetailsRequest);
        var details = countryService.getAllStatesWithCitiesByCountryName(countryDetailsRequest.getCountry());
        var response = new ListResponse("00", "success", details);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/currency-conversion")
    public ResponseEntity<DataResponse> getCurrencyConversion(@Valid @RequestBody final CountryCurrencyRequest countryCurrencyRequest,
                                                                  @ApiIgnore Errors errors) {
        if (errors.hasErrors()) throw new BadRequestException(errors.getAllErrors());
        log.debug("executing currency conversion" + countryCurrencyRequest);
        var details = countryService.getConversionByCountryNameAmountAndTargetCurrency(countryCurrencyRequest.getCountry(),
                countryCurrencyRequest.getAmount(), countryCurrencyRequest.getTargetCurrency());
        var response = new DataResponse("00", "Success", details);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
