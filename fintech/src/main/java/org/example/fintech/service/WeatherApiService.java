package org.example.fintech.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherApiService {

    private final WebClient webClient;
    private final String apiKey;

    public WeatherApiService(WebClient.Builder webClientBuilder,
                             @Value("${weatherapi.base-url}") String baseUrl,
                             @Value("${weatherapi.api-key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
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
