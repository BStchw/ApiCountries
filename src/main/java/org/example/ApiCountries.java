package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the API Countries service.
 * <p>
 * This class serves as the entry point for the Spring Boot application.
 * It contains the main method which launches the application.
 * </p>
 */
@SpringBootApplication
public class ApiCountries {

    /**
     * The main method that launches the Spring Boot application.
     * <p>
     * This method initializes the Spring application context, setting up
     * all the necessary components for the application.
     * </p>
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiCountries.class, args);
    }
}
