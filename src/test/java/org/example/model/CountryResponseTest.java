package org.example.model;

import org.example.model.CountryResponse.Country;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CountryResponse} class.
 * <p>
 * This class contains tests for the methods of the {@link CountryResponse} class,
 * specifically focusing on the calculation of the sum of populations and the getter/setter methods.
 * </p>
 */
public class CountryResponseTest {

    /**
     * Tests the {@link CountryResponse#getSumOfPopulation()} method when the list of countries is empty.
     * <p>
     * This test verifies that the sum of population is zero when the list of countries is empty.
     * </p>
     */
    @Test
    public void testCalculateSumOfPopulation_EmptyList() {
        CountryResponse response = new CountryResponse();
        response.setCountries(Collections.emptyList());
        assertEquals(0, response.getSumOfPopulation());
    }

    /**
     * Tests the {@link CountryResponse#getSumOfPopulation()} method when the list of countries contains data.
     * <p>
     * This test verifies that the sum of population is correctly calculated for a list of countries with given populations.
     * </p>
     */
    @Test
    public void testCalculateSumOfPopulation_NonEmptyList() {
        Country country1 = new Country();
        country1.setPopulation(50000000);
        Country country2 = new Country();
        country2.setPopulation(70000000);

        CountryResponse response = new CountryResponse();
        response.setCountries(Arrays.asList(country1, country2));
        assertEquals(120000000, response.getSumOfPopulation());
    }

    /**
     * Tests the {@link CountryResponse#getSumOfPopulation()} method when the list of countries is null.
     * <p>
     * This test verifies that the sum of population is zero when the list of countries is null.
     * </p>
     */
    @Test
    public void testCalculateSumOfPopulation_NullList() {
        CountryResponse response = new CountryResponse();
        response.setCountries(null);
        assertEquals(0, response.getSumOfPopulation());
    }

    /**
     * Tests the getter and setter methods for the {@link CountryResponse#countries} field.
     * <p>
     * This test verifies that the {@link CountryResponse#setCountries(java.util.List)} and
     * {@link CountryResponse#getCountries()} methods correctly handle setting and retrieving a list of countries.
     * </p>
     */
    @Test
    public void testGetCountriesAndSetCountries() {
        Country country = new Country();
        country.setName("TestCountry");
        CountryResponse response = new CountryResponse();
        response.setCountries(Collections.singletonList(country));

        assertNotNull(response.getCountries());
        assertEquals(1, response.getCountries().size());
        assertEquals("TestCountry", response.getCountries().get(0).getName());
    }
}
