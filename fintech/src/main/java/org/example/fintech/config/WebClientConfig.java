package org.example.fintech.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for setting up a WebClient using Spring WebFlux.
 * This class provides a bean for creating a WebClient.Builder instance.
 */

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("weatherApi")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}