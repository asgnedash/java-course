package org.example.fintech.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherApiService {

    private WebClient webClient;
    private String apiKey;
    private RateLimiter rateLimiter;

    public WeatherApiService(@Qualifier("weatherApi") WebClient.Builder webClientBuilder,
                             @Value("${weatherApi.base-url}") String baseUrl,
                             @Value("${weatherApi.api-key}") String apiKey,
                             @Qualifier("rateLimiter") RateLimiter rateLimiter) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.rateLimiter = rateLimiter;
    }

    public ResponseEntity<String> getCurrentWeather(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/current.json")
                        .queryParam("key", apiKey)
                        .queryParam("q", city)
                        .build())
                .retrieve()
                .toEntity(String.class)
                .block();
    }

}
