package org.example.fintech.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.example.fintech.controller.WeatherApiErrorController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service class handling requests to the Weather API.
 * This class uses a WebClient instance to make reactive web requests to the Weather API endpoints.
 * It integrates with Resilience4j RateLimiter to control the rate of requests made to the API.
 * The service methods handle various HTTP statuses and error responses returned by the API,
 * throwing custom exceptions (WeatherApiErrorController) when specific errors occur.
 */

@Service
public class WeatherApiService {

    private final WebClient webClient;
    private final String apiKey;
    private final RateLimiter rateLimiter;

    public WeatherApiService(@Qualifier("weatherApi") WebClient.Builder webClientBuilder,
                             @Value("${weatherApi.base-url}") String baseUrl,
                             @Value("${weatherApi.api-key}") String apiKey,
                             @Qualifier("rateLimiter") RateLimiter rateLimiter) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.rateLimiter = rateLimiter;
    }

    public Mono<String> getCurrentWeather(String city) {
        return webClient.get()
                .uri(uriBuilder->uriBuilder
                        .path("/current.json")
                        .queryParam("key", apiKey)
                        .queryParam("q", city)
                        .build())
                .retrieve()
                .onStatus(
                        status->status.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse->clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody->{
                                    if (errorBody.contains("1002")) {
                                        return Mono.error(new WeatherApiErrorController(401, 1002, "API key not provided"));
                                    }
                                    if (errorBody.contains("2006")) {
                                        return Mono.error(new WeatherApiErrorController(401, 2006, "API key provided is invalid"));
                                    }
                                    return Mono.error(new WeatherApiErrorController(clientResponse.statusCode().value(), -1, errorBody));
                                })
                )
                .onStatus(
                        status->status.equals(HttpStatus.BAD_REQUEST),
                        clientResponse->clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody->{
                                    if (errorBody.contains("1003")) {
                                        return Mono.error(new WeatherApiErrorController(400, 1003, "Parameter 'q' not provided"));
                                    }
                                    if (errorBody.contains("1005")) {
                                        return Mono.error(new WeatherApiErrorController(400, 1005, "API request url is invalid"));
                                    }
                                    if (errorBody.contains("1006")) {
                                        return Mono.error(new WeatherApiErrorController(400, 1006, "No location found matching parameter 'q'"));
                                    }
                                    if (errorBody.contains("9000")) {
                                        return Mono.error(new WeatherApiErrorController(400, 9000, "Json body passed in bulk request is invalid. Please make sure it is valid json with utf-8 encoding"));
                                    }
                                    if (errorBody.contains("9001")) {
                                        return Mono.error(new WeatherApiErrorController(400, 9001, "Json body contains too many locations for bulk request. Please keep it below 50 in a single request"));
                                    }
                                    if (errorBody.contains("9999")) {
                                        return Mono.error(new WeatherApiErrorController(400, 9999, "Internal application error"));
                                    }
                                    return Mono.error(new WeatherApiErrorController(clientResponse.statusCode().value(), -1, errorBody));
                                })
                )
                .onStatus(
                        status->status.equals(HttpStatus.FORBIDDEN),
                        clientResponse->clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody->{
                                    if (errorBody.contains("2007")) {
                                        return Mono.error(new WeatherApiErrorController(403, 2007, "API key has exceeded calls per month quota"));
                                    }
                                    if (errorBody.contains("2008")) {
                                        return Mono.error(new WeatherApiErrorController(403, 2008, "API key has been disabled"));
                                    }
                                    if (errorBody.contains("2009")) {
                                        return Mono.error(new WeatherApiErrorController(403, 2009, "API key does not have access to the resource. Please check pricing page for what is allowed in your API subscription plan"));
                                    }
                                    return Mono.error(new WeatherApiErrorController(clientResponse.statusCode().value(), -1, errorBody));
                                })
                )
                .onStatus(
                        status->status.equals(HttpStatus.NOT_FOUND) || status.equals(HttpStatus.INTERNAL_SERVER_ERROR),
                        clientResponse->clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody->{
                                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                                        return Mono.error(new Exception("Not Found"));
                                    }
                                    if (clientResponse.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                                        return Mono.error(new Exception("Internal Server Error"));
                                    }
                                    return Mono.error(new WeatherApiErrorController(clientResponse.statusCode().value(), -1, errorBody));
                                })
                )
                .bodyToMono(String.class);
    }
}
