package org.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link CacheConfig} class to ensure that the Caffeine cache configuration
 * is correctly applied and that the {@link CaffeineCacheManager} bean is properly created.
 * <p>
 * This test verifies the configuration of the cache manager and ensures that it is properly
 * initialized with no preconfigured caches.
 * </p>
 */
@SpringBootTest
public class CacheConfigTest {

    @Autowired
    private CaffeineCacheManager cacheManager; // Inject the CaffeineCacheManager bean

    /**
     * Tests the configuration of the {@link CaffeineCacheManager} bean.
     * <p>
     * This test checks that the {@link CaffeineCacheManager} bean is not null and ensures that
     * no caches are preconfigured by default. It also validates the ability to retrieve a cache
     * named "myCache" from the manager, ensuring that cache configuration is correctly applied.
     * </p>
     */
    @Test
    public void testCacheManagerConfiguration() {
        assertThat(cacheManager).isNotNull(); // Verify that the cache manager is not null
        assertThat(cacheManager.getCacheNames()).isEmpty(); // Ensure there are no preconfigured caches

        // Optionally, further validate the Caffeine configuration by checking its properties
        assertThat(cacheManager.getCache("myCache")).isNotNull(); // Ensure "myCache" can be retrieved
    }
}
