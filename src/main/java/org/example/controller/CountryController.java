package org.example.controller;

import org.example.model.CountryResponse;
import org.example.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/countries")
    public CountryResponse getCountriesByRegion(@RequestParam String region) {
        return countryService.getCountriesByRegion(region);
    }
}
