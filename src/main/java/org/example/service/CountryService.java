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

import java.util.*;
import java.util.stream.Collectors;

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
                country.setCapital(countryNode.get("capital").asText());
                country.setRegion(countryNode.get("region").asText());
                country.setSubRegion(countryNode.get("subregion").asText());
                country.setPopulation(countryNode.get("population").asLong());
                country.setArea(countryNode.get("area").asLong());

                if (countryNode.has("borders")) {
                    List<String> borders = new ArrayList<>();
                    countryNode.get("borders").forEach(borderNode -> borders.add(borderNode.asText()));
                    country.setBorders(borders);
                }

                countries.add(country);
            }

            // Sortowanie krajów według powierzchni i pobieranie top 10
            List<CountryResponse.Country> top10CountriesByArea = countries.stream()
                    .sorted(Comparator.comparingLong(CountryResponse.Country::getArea).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setCountries(top10CountriesByArea);  // Ustaw tylko top 10 krajów według powierzchni

            logger.info("Fetched top 10 countries for region: {}", region);
            return countryResponse;
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching countries from API: {} {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error processing response: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public CountryResponse getCountriesBySubRegion(String subregion) {
        String url = "https://countryapi.io/api/all/";
        try {

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
                country.setCapital(countryNode.get("capital").asText());
                country.setRegion(countryNode.get("region").asText());
                country.setSubRegion(countryNode.get("subregion").asText());
                country.setPopulation(countryNode.get("population").asLong());
                country.setArea(countryNode.get("area").asLong());

                if (countryNode.has("borders")) {
                    List<String> borders = new ArrayList<>();
                    countryNode.get("borders").forEach(borderNode -> borders.add(borderNode.asText()));
                    country.setBorders(borders);
                }

                List<String> countryBorders = country.getBorders();

                if(countryBorders.size()>3 && country.getSubRegion().equals(subregion))    {
                    countries.add(country);
                }
            }

            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setCountries(countries);

            logger.info("Fetched {} countries for region: {}", countries.size());
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
