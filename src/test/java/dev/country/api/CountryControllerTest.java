package dev.country.api;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApiApplication.class)
@AutoConfigureMockMvc

public class CountryControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    public void givenValidNumberForGetTopNCities_get200()
            throws Exception {
        mvc.perform(get("/countries/top-cities?number=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseCode").value("00"));
    }


    @Test
    public void givenValidCountry_get200Ok()
            throws Exception {

        mvc.perform( MockMvcRequestBuilders
                        .post("/countries/country-details")
                        .content("{ \"country\": \"Nigeria\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"));


    }

    @Test
    public void givenCitiesInStateForCountry_get200Ok()
            throws Exception {

        mvc.perform( MockMvcRequestBuilders
                        .post("/countries/cities-in-state")
                        .content("{ \"country\": \"Nigeria\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"));


    }
   @Test
    public void givenMissingParamCitiesInStateForCountry_get400BadRequest()
            throws Exception {

        mvc.perform( MockMvcRequestBuilders
                        .post("/countries/cities-in-state")
                        .content("{ \"county\": \"Nigeria\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseCode").value("95"));

    }

    @Test
    public void givenGetCurrencyConversions_get200Ok() throws Exception {

        mvc.perform( MockMvcRequestBuilders
                        .post("/countries/currency-conversion")
                        .content("{ \"amount\": 1000,  \"country\": \"Italy\",\"targetCurrency\": \"NGN\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"));

    }


}