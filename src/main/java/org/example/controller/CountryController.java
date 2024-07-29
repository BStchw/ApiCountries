package org.example.controller;

import org.example.model.CountryResponse;
import org.example.service.CountryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

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
}
