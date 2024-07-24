package org.example.service;

import org.example.model.CountryResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CountryService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    private static final String API_KEY = "aBJns6RncdtFogHf73stUIhfTPZuqdNV1JunNWzZ";

    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public CountryResponse getCountriesByRegion(String region) {
        String url = "https://countryapi.io/api/region/" + region;
        try {
            logger.info("Fetching countries for region: {}", region);

            // Utwórz nagłówki z Bearer Token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            String jsonResponse = response.getBody();
            logger.debug("API response: {}", jsonResponse);

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<CountryResponse.Country> countries = new ArrayList<>();

            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode countryNode = field.getValue();

                CountryResponse.Country country = new CountryResponse.Country();
                country.setName(countryNode.get("name").asText());
                country.setPopulation(countryNode.get("population").asLong());

                countries.add(country);
            }

            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setCountries(countries);

            logger.info("Fetched {} countries for region: {}", countries.size(), region);
            return countryResponse;
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching countries from API: {} {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error processing response: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
