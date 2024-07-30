package org.example.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for setting up caching in the application.
 * <p>
 * This class configures the caching mechanism using Caffeine, a high-performance caching library.
 * It enables caching and defines the configuration for Caffeine-based caches.
 * </p>
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates a {@link CaffeineCacheManager} bean.
     * <p>
     * The {@link CaffeineCacheManager} manages the Caffeine caches, which are
     * configured by the {@link #caffeineCacheBuilder()} method.
     * </p>
     *
     * @return a configured {@link CaffeineCacheManager} instance
     */
    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    /**
     * Creates a {@link Caffeine} cache builder bean.
     * <p>
     * Configures the properties of the Caffeine cache, including the initial capacity,
     * maximum size, expiration time, weak keys, and statistics recording.
     * </p>
     *
     * @return a configured {@link Caffeine} cache builder
     */
    @Bean
    public Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .weakKeys()
                .recordStats();
    }
}
