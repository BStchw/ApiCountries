package org.example.service;

import org.example.model.CountryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CountryService} class.
 * <p>
 * This class contains tests for the methods of the {@link CountryService} class,
 * specifically focusing on the {@link CountryService#getCountriesByRegion(String)} method.
 * </p>
 */
public class CountryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountryService countryService;

    /**
     * Initializes mock objects and sets up the test environment.
     * <p>
     * This method is executed before each test and prepares the necessary mocks.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link CountryService#getCountriesByRegion(String)} method when a valid response is returned.
     * <p>
     * This test verifies that the method correctly processes a valid JSON response and populates
     * the {@link CountryResponse} object with the expected data.
     * </p>
     *
     * @throws Exception if there is an error during the test execution
     */
    @Test
    public void testGetCountriesByRegion_Success() throws Exception {
        // Ustal dane wejściowe
        String region = "Europe";
        String url = "https://countryapi.io/api/region/" + region;
        String jsonResponse = """
            {
                "DEU": {
                    "name": "Germany",
                    "capital": "Berlin",
                    "region": "Europe",
                    "subregion": "Western Europe",
                    "population": 83000000,
                    "area": 357022,
                    "borders": ["AUT", "BEL", "CZE", "DNK", "FRA", "LUX", "NLD", "POL", "CHE"]
                },
                "FRA": {
                    "name": "France",
                    "capital": "Paris",
                    "region": "Europe",
                    "subregion": "Western Europe",
                    "population": 67000000,
                    "area": 640679,
                    "borders": ["AND", "BEL", "DEU", "ITA", "LUX", "MCO", "ESP", "CHE"]
                }
            }
        """;

        // Ustal zachowanie moków
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "aBJns6RncdtFogHf73stUIhfTPZuqdNV1JunNWzZ");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(jsonResponse);
        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);

        // Testuj metodę
        CountryResponse result = countryService.getCountriesByRegion(region);

        // Weryfikuj wyniki
        assertNotNull(result);
        assertEquals(2, result.getCountries().size());
        assertEquals("France", result.getCountries().get(0).getName());
        assertEquals("Germany", result.getCountries().get(1).getName());
    }

    /**
     * Tests the {@link CountryService#getCountriesByRegion(String)} method when the response is empty.
     * <p>
     * This test verifies that the method correctly handles an empty JSON response and returns
     * an empty list of countries.
     * </p>
     *
     * @throws Exception if there is an error during the test execution
     */
    @Test
    public void testGetCountriesByRegion_EmptyResponse() throws Exception {
        // Ustal dane wejściowe
        String region = "Europe";
        String url = "https://countryapi.io/api/region/" + region;
        String jsonResponse = "{}";

        // Ustal zachowanie moków
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "aBJns6RncdtFogHf73stUIhfTPZuqdNV1JunNWzZ");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(jsonResponse);
        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);

        // Testuj metodę
        CountryResponse result = countryService.getCountriesByRegion(region);

        // Weryfikuj wyniki
        assertNotNull(result);
        assertTrue(result.getCountries().isEmpty());
    }

    /**
     * Tests the {@link CountryService#getCountriesByRegion(String)} method when an exception is thrown.
     * <p>
     * This test verifies that the method correctly handles exceptions thrown by the RestTemplate
     * and propagates them as runtime exceptions.
     * </p>
     */
    @Test
    public void testGetCountriesByRegion_Exception() {
        // Ustal dane wejściowe
        String region = "Europe";
        String url = "https://countryapi.io/api/region/" + region;

        // Ustal zachowanie moków
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "aBJns6RncdtFogHf73stUIhfTPZuqdNV1JunNWzZ");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class))
                .thenThrow(new RuntimeException("API error"));

        // Testuj metodę i oczekuj wyjątku
        assertThrows(RuntimeException.class, () -> countryService.getCountriesByRegion(region));
    }

    // Możesz dodać więcej testów dla innych metod, takich jak getCountriesBySubRegion, convertToCSV itp.
}
