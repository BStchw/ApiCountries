package org.example.model;

import java.util.List;

/**
 * Represents a response containing a list of countries and additional metadata.
 */
public class CountryResponse {

    private List<Country> countries;
    private long sumOfPopulation;

    /**
     * Returns the list of countries in this response.
     *
     * @return the list of countries
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * Sets the list of countries in this response.
     * <p>
     * This method also calculates and sets the total population of the listed countries.
     * </p>
     *
     * @param countries the list of countries to set
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
        this.sumOfPopulation = calculateSumOfPopulation();
    }

    /**
     * Returns the sum of the population of all the countries in this response.
     *
     * @return the total population of the listed countries
     */
    public long getSumOfPopulation() {
        return sumOfPopulation;
    }

    /**
     * Calculates the total population of the countries in the list.
     *
     * @return the sum of the population of all the countries
     */
    private long calculateSumOfPopulation() {
        if (countries == null) {
            return 0;
        }
        return countries.stream()
                .mapToLong(Country::getPopulation)
                .sum();
    }

    /**
     * Represents a country with various attributes such as name, capital, region, etc.
     */
    public static class Country {
        private String name;
        private String capital;
        private String region;
        private String subRegion;
        private long population;
        private long area;
        private List<String> borders;

        /**
         * Returns the name of the country.
         *
         * @return the country's name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the country.
         *
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Returns the population of the country.
         *
         * @return the population
         */
        public long getPopulation() {
            return population;
        }

        /**
         * Sets the population of the country.
         *
         * @param population the population to set
         */
        public void setPopulation(long population) {
            this.population = population;
        }

        /**
         * Returns the region to which the country belongs.
         *
         * @return the region
         */
        public String getRegion() {
            return region;
        }

        /**
         * Sets the region to which the country belongs.
         *
         * @param region the region to set
         */
        public void setRegion(String region) {
            this.region = region;
        }

        /**
         * Returns the capital of the country.
         *
         * @return the capital city
         */
        public String getCapital() {
            return capital;
        }

        /**
         * Sets the capital of the country.
         *
         * @param capital the capital city to set
         */
        public void setCapital(String capital) {
            this.capital = capital;
        }

        /**
         * Returns the area of the country in square kilometers.
         *
         * @return the area
         */
        public long getArea() {
            return area;
        }

        /**
         * Sets the area of the country in square kilometers.
         *
         * @param area the area to set
         */
        public void setArea(long area) {
            this.area = area;
        }

        /**
         * Returns the subregion to which the country belongs.
         *
         * @return the subregion
         */
        public String getSubRegion() {
            return subRegion;
        }

        /**
         * Sets the subregion to which the country belongs.
         *
         * @param subRegion the subregion to set
         */
        public void setSubRegion(String subRegion) {
            this.subRegion = subRegion;
        }

        /**
         * Returns the list of countries that border this country.
         *
         * @return the list of bordering countries
         */
        public List<String> getBorders() {
            return borders;
        }

        /**
         * Sets the list of countries that border this country.
         *
         * @param borders the list of bordering countries to set
         */
        public void setBorders(List<String> borders) {
            this.borders = borders;
        }
    }
}
