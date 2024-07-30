package org.example.controller;

import org.example.model.CountryResponse;
import org.example.service.CountryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling requests related to country information.
 * <p>
 * This controller provides endpoints to retrieve country data by region and subregion.
 * </p>
 */
@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    /**
     * Constructs a new {@link CountryController} with the specified {@link CountryService}.
     *
     * @param countryService the service to be used for retrieving country data
     */
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Retrieves a list of countries by region.
     * <p>
     * This endpoint returns country data in either JSON or CSV format, based on the
     * specified format parameter.
     * </p>
     *
     * @param region the region to filter countries by
     * @param format the response format (either "json" or "csv"), default is "json"
     * @return a {@link ResponseEntity} containing the list of countries in the specified format
     */
    @GetMapping("/region")
    public ResponseEntity<?> getCountriesByRegion(
            @RequestParam String region,
            @RequestParam(required = false, defaultValue = "json") String format) {

        CountryResponse countryResponse = countryService.getCountriesByRegion(region);

        if ("csv".equalsIgnoreCase(format)) {
            String csvResponse = countryService.convertToCSV(countryResponse.getCountries());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=countries.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvResponse);
        } else {
            return ResponseEntity.ok(countryResponse);
        }
    }

    /**
     * Retrieves a list of countries by subregion.
     * <p>
     * This endpoint returns country data in either JSON or CSV format, based on the
     * specified format parameter.
     * </p>
     *
     * @param subregion the subregion to filter countries by
     * @param format the response format (either "json" or "csv"), default is "json"
     * @return a {@link ResponseEntity} containing the list of countries in the specified format
     */
    @GetMapping("/subregion")
    public ResponseEntity<?> getCountriesBySubRegion(
            @RequestParam String subregion,
            @RequestParam(required = false, defaultValue = "json") String format) {

        CountryResponse countryResponse = countryService.getCountriesBySubRegion(subregion);

        if ("csv".equalsIgnoreCase(format)) {
            String csvResponse = countryService.convertToCSV(countryResponse.getCountries());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=countries.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvResponse);
        } else {
            return ResponseEntity.ok(countryResponse);
        }
    }

    /**
     * Retrieves the sum of population for countries in a specific subregion.
     * <p>
     * This endpoint returns the total population of all countries in the specified subregion.
     * </p>
     *
     * @param subregion the subregion to calculate the population sum for
     * @return a {@link ResponseEntity} containing the sum of population for the specified subregion
     */
    @GetMapping("/subregion/population")
    public ResponseEntity<Long> getSumOfPopulationBySubregion(
            @RequestParam String subregion) {

        try {
            long sumOfPopulation = countryService.getSumOfPopulationOfSubregion(subregion);
            return ResponseEntity.ok(sumOfPopulation);
        } catch (Exception e) {
            // Log the error and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
