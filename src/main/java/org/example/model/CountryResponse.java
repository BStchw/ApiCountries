package org.example.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CountryResponse {
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
    public List<Country> getTop10CountriesByArea() {
        if (countries == null) {
            return List.of();
        }

        return countries.stream()
                .sorted(Comparator.comparingDouble(Country::getArea).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public static class Country {
        private String name;
        private String capital;
        private String region;
        private String subRegion;
        private long population;
        private long area;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPopulation() {
            return population;
        }

        public void setPopulation(long population) {
            this.population = population;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getRegion() {
            return region;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public void setArea(long area) {
            this.area = area;
        }

        public long getArea() {
            return area;
        }

        public String getSubRegion() {
            return subRegion;
        }

        public void setSubRegion(String subRegion) {
            this.subRegion = subRegion;
        }
    }
}
