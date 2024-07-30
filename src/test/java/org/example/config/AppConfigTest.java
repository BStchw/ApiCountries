package org.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the {@link AppConfig} class to ensure that the Spring application context
 * is correctly loading the configuration and creating necessary beans.
 * <p>
 * This test verifies that the {@link RestTemplate} bean is properly created and available
 * in the application context.
 * </p>
 */
@SpringBootTest(classes = AppConfig.class) // Indicates the configuration class to test
public class AppConfigTest {

    @Autowired
    private ApplicationContext applicationContext; // Inject the ApplicationContext to access beans

    /**
     * Tests that the {@link RestTemplate} bean is created and available in the application context.
     * <p>
     * This test ensures that the {@link RestTemplate} bean, defined in {@link AppConfig}, is properly
     * instantiated and can be retrieved from the application context. It uses {@link assertNotNull}
     * to assert that the bean is not null.
     * </p>
     */
    @Test
    public void testRestTemplateBean() {
        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);
        assertNotNull(restTemplate, "RestTemplate bean should be created");
    }
}
