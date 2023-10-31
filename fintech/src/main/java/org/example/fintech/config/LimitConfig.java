package org.example.fintech.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration class for setting up a rate limiter using Resilience4j library.
 * This class provides beans for configuring and creating a rate limiter instance
 * to limit the number of requests made to weather API.
 */

@Configuration
public class LimitConfig {

    int limitForPeriod = 1_000_000;     // Maximum number of requests.

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.of(
                RateLimiterConfig.custom()
                        .limitForPeriod(limitForPeriod)
                        .limitRefreshPeriod(Duration.ofDays(30))
                        .build()
        );
    }

    @Bean
    @Qualifier("rateLimiter")
    public RateLimiter rateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        return rateLimiterRegistry.rateLimiter("limitWeatherApi");
    }
}
