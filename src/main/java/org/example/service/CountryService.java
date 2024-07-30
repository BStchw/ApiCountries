package org.example.service;

import org.example.model.CountryResponse;
import org.springframework.cache.annotation.Cacheable;
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

/**
 * Service class for retrieving and processing country data.
 * This class provides methods for fetching country information from an external API,
 * converting data to CSV format, and calculating population statistics.
 */
@Service
public class CountryService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    private static final String API_KEY = "aBJns6RncdtFogHf73stUIhfTPZuqdNV1JunNWzZ";

    /**
     * Constructs a new {@code CountryService} with the specified {@link RestTemplate}.
     *
     * @param restTemplate the {@code RestTemplate} to use for making HTTP requests
     */
    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves the top 10 countries by area in the specified region.
     * The results are cached to improve performance.
     *
     * @param region the region to fetch countries from
     * @return a {@link CountryResponse} containing the top 10 countries by area
     */
    @Cacheable("countriesByRegion")
    public CountryResponse getCountriesByRegion(String region) {
        String url = "https://countryapi.io/api/region/" + region;
        try {
            logger.info("Fetching countries for region: {}", region);

            // Create headers with Bearer Token
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

            // Sort countries by area and retrieve top 10
            List<CountryResponse.Country> top10CountriesByArea = countries.stream()
                    .sorted(Comparator.comparingLong(CountryResponse.Country::getArea).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setCountries(top10CountriesByArea);

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

    /**
     * Retrieves countries from the specified subregion that have more than three borders.
     * The results are cached to improve performance.
     *
     * @param subregion the subregion to fetch countries from
     * @return a {@link CountryResponse} containing the list of countries
     */
    @Cacheable("countriesBySubRegion")
    public CountryResponse getCountriesBySubRegion(String subregion) {
        String url = "https://countryapi.io/api/all/";
        try {
            // Create headers with Bearer Token
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

                if (countryBorders.size() > 3 && country.getSubRegion().equals(subregion)) {
                    countries.add(country);
                }
            }

            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setCountries(countries);

            logger.info("Fetched {} countries for subregion: {}", countries.size(), subregion);
            return countryResponse;
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching countries from API: {} {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error processing response: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a list of countries to a CSV format.
     *
     * @param countries the list of countries to convert
     * @return a CSV string representation of the countries
     */
    public String convertToCSV(List<CountryResponse.Country> countries) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("name,capital,region,subRegion,population,area,borders\n");

        for (CountryResponse.Country country : countries) {
            csvBuilder.append(country.getName()).append(",");
            csvBuilder.append(country.getCapital()).append(",");
            csvBuilder.append(country.getRegion()).append(",");
            csvBuilder.append(country.getSubRegion()).append(",");
            csvBuilder.append(country.getPopulation()).append(",");
            csvBuilder.append(country.getArea()).append(",");
            csvBuilder.append(String.join(";", country.getBorders())).append("\n");
        }

        return csvBuilder.toString();
    }

    /**
     * Retrieves the sum of the population of all countries in the specified subregion.
     *
     * @param subregion the subregion to calculate the population sum for
     * @return the total population of the subregion
     */
    public long getSumOfPopulationOfSubregion(String subregion) {
        String url = "https://countryapi.io/api/all/";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            String jsonResponse = response.getBody();
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

                if (country.getSubRegion().equalsIgnoreCase(subregion)) {
                    countries.add(country);
                }
            }

            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setCountries(countries);

            logger.info("Fetched {} countries for subregion: {}", countries.size(), subregion);
            return countryResponse.getSumOfPopulation();
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching countries from API: {} {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error processing response: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
