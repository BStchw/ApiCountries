package org.example.model;

import java.util.List;

public class CountryResponse {
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public static class Country {
        private String name;
        private String capital;
        private String region;
        private String subRegion;
        private long population;
        private long area;
        private List<String> borders;

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

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public long getArea() {
            return area;
        }

        public void setArea(long area) {
            this.area = area;
        }

        public String getSubRegion() {
            return subRegion;
        }

        public void setSubRegion(String subRegion) {
            this.subRegion = subRegion;
        }

        public List<String> getBorders() {
            return borders;
        }

        public void setBorders(List<String> borders) {
            this.borders = borders;
        }
    }
}
