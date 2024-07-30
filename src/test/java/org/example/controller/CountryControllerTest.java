package org.example.controller;

import org.example.model.CountryResponse;
import org.example.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Unit tests for the {@link CountryController} class.
 * <p>
 * This class contains tests for the endpoints defined in the {@link CountryController} class.
 * It uses the Spring MockMvc framework to simulate HTTP requests and verify the behavior of the controller
 * under different scenarios. The tests ensure that the controller correctly handles requests for countries
 * by region and subregion, both in JSON and CSV formats.
 * </p>
 */
public class CountryControllerTest {

    @Mock
    private CountryService countryService; // Mocked CountryService dependency

    @InjectMocks
    private CountryController countryController; // Controller instance with injected mocks

    private MockMvc mockMvc; // MockMvc instance for simulating HTTP requests

    /**
     * Sets up the test environment before each test method.
     * <p>
     * Initializes Mockito annotations and sets up the MockMvc instance for the {@link CountryController}.
     * </p>
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    /**
     * Tests the {@code GET /countries/region} endpoint with JSON format.
     * <p>
     * This test verifies that the controller correctly responds with a JSON representation of countries
     * when requested for a specific region. It mocks the {@link CountryService} to return a predefined
     * {@link CountryResponse} and asserts that the JSON response matches the expected structure and content.
     * </p>
     *
     * @throws Exception if an error occurs while performing the request or asserting the response
     */
    @Test
    public void testGetCountriesByRegion_JSON() throws Exception {
        // Arrange
        String region = "Asia";

        // Create Country objects using setters
        CountryResponse.Country country1 = new CountryResponse.Country();
        country1.setName("Afghanistan");
        country1.setCapital("Kabul");
        country1.setRegion("Asia");
        country1.setSubRegion("Southern Asia");
        country1.setPopulation(2837743);
        country1.setArea(652230);

        CountryResponse.Country country2 = new CountryResponse.Country();
        country2.setName("Sudan");
        country2.setCapital("Khartoum");
        country2.setRegion("Africa");
        country2.setSubRegion("Northern Africa");
        country2.setPopulation(43849269);
        country2.setArea(1886068);

        // Create CountryResponse object and set the list of countries
        CountryResponse response = new CountryResponse();
        response.setCountries(Arrays.asList(country1, country2));

        // Mock the service response
        when(countryService.getCountriesByRegion(region)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/countries/region")
                        .param("region", region)
                        .param("format", "json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].name").value("Afghanistan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].capital").value("Kabul"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].region").value("Asia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].subRegion").value("Southern Asia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].population").value(2837743))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].area").value(652230))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[1].name").value("Sudan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[1].capital").value("Khartoum"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[1].region").value("Africa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[1].subRegion").value("Northern Africa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[1].population").value(43849269))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[1].area").value(1886068));
    }

    /**
     * Tests the {@code GET /countries/region} endpoint with CSV format.
     * <p>
     * This test verifies that the controller correctly responds with a CSV representation of countries
     * when requested for a specific region. It mocks the {@link CountryService} to return a predefined
     * {@link CountryResponse} and checks that the CSV response matches the expected content.
     * </p>
     *
     * @throws Exception if an error occurs while performing the request or asserting the response
     */
    @Test
    public void testGetCountriesByRegion_CSV() throws Exception {
        // Arrange
        String region = "Europe";
        CountryResponse response = new CountryResponse();
        // Set up the mock response here
        when(countryService.getCountriesByRegion(region)).thenReturn(response);
        when(countryService.convertToCSV(response.getCountries())).thenReturn("name,capital,region\nGermany,Berlin,Europe");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/countries/region")
                        .param("region", region)
                        .param("format", "csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/csv")))
                .andExpect(content().string("name,capital,region\nGermany,Berlin,Europe"))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=countries.csv"));
    }

    /**
     * Tests the {@code GET /countries/subregion} endpoint with JSON format.
     * <p>
     * This test verifies that the controller correctly responds with a JSON representation of countries
     * for a specific subregion. It mocks the {@link CountryService} to return a predefined
     * {@link CountryResponse} and asserts that the JSON response matches the expected structure and content.
     * </p>
     *
     * @throws Exception if an error occurs while performing the request or asserting the response
     */
    @Test
    public void testGetCountriesBySubRegion_JSON() throws Exception {
        String subregion = "Southern Asia";
        CountryResponse.Country country1 = new CountryResponse.Country();
        country1.setName("Afghanistan");
        country1.setCapital("Kabul");
        country1.setRegion("Asia");
        country1.setSubRegion("Southern Asia");
        country1.setPopulation(2837743);
        country1.setArea(652230);

        CountryResponse response = new CountryResponse();
        response.setCountries(Collections.singletonList(country1));

        when(countryService.getCountriesBySubRegion(subregion)).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/countries/subregion")
                        .param("subregion", subregion)
                        .param("format", "json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Debugging: Print the actual JSON response
        String content = result.getResponse().getContentAsString();
        System.out.println("Response JSON: " + content);

        // Assertions
        mockMvc.perform(MockMvcRequestBuilders.get("/countries/subregion")
                        .param("subregion", subregion)
                        .param("format", "json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].name").value("Afghanistan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].capital").value("Kabul"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].region").value("Asia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].subRegion").value("Southern Asia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].population").value(2837743))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countries[0].area").value(652230));
    }

    /**
     * Tests the {@code GET /countries/subregion} endpoint with CSV format.
     * <p>
     * This test verifies that the controller correctly responds with a CSV representation of countries
     * for a specific subregion. It mocks the {@link CountryService} to return a predefined
     * {@link CountryResponse} and checks that the CSV response matches the expected content.
     * </p>
     *
     * @throws Exception if an error occurs while performing the request or asserting the response
     */
    @Test
    public void testGetCountriesBySubRegion_CSV() throws Exception {
        // Arrange
        String subregion = "Western Europe";
        CountryResponse response = new CountryResponse();
        // Set up the mock response here
        when(countryService.getCountriesBySubRegion(subregion)).thenReturn(response);
        when(countryService.convertToCSV(response.getCountries())).thenReturn("name,capital,region\nGermany,Berlin,Western Europe");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/countries/subregion")
                        .param("subregion", subregion)
                        .param("format", "csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/csv")))
                .andExpect(content().string("name,capital,region\nGermany,Berlin,Western Europe"))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=countries.csv"));
    }
}
