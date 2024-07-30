package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application-wide beans and settings.
 * <p>
 * This class defines beans that are used throughout the application,
 * such as the {@link RestTemplate} for making HTTP requests.
 * </p>
 */
@Configuration
public class AppConfig {

    /**
     * Creates a {@link RestTemplate} bean.
     * <p>
     * The {@link RestTemplate} is a synchronous client to perform HTTP requests,
     * exposing a simple, template-method API over underlying HTTP client libraries.
     * </p>
     *
     * @return a {@link RestTemplate} instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
