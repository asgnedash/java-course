package org.example.fintech.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.fintech.DTO.WeatherDTO;
import org.example.fintech.cache.WeatherCache;
import org.example.fintech.service.WeatherApiService;
import org.example.fintech.service.WeatherJdbcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;

/**
 * Controller class handling weather-related API requests.
 * This class defines endpoints for receiving weather information based on the provided city.
 * It utilizes the WeatherApiService to fetch current weather data.
 */

@RestController
@RequestMapping("/weather")
public class WeatherApiController {

    private static WeatherCache weatherCache = new WeatherCache();

    private final WeatherApiService weatherApiService;

    private final WeatherJdbcService weatherJdbcService;

    @Autowired
    public WeatherApiController(WeatherApiService weatherApiService, WeatherJdbcService weatherJdbcService) {
        this.weatherApiService = weatherApiService;
        this.weatherJdbcService = weatherJdbcService;
    }

    @Operation(summary = "Receive the weather")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeatherApiErrorController.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeatherApiErrorController.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WeatherApiErrorController.class)
                            )
                    }
            )
    })
    @GetMapping("/{city}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<String> getWeather(@PathVariable String city) {
//        if (weatherCache.get(city).isPresent()) {
//            return Mono.just(weatherCache.get(city).toString());
//        } else {
            return weatherApiService.getCurrentWeather(city)
                    .map(json->{
                        try {
                            System.out.println(1);
                            WeatherDTO dto = jsonToDTO(json);
                            System.out.println(dto.toString());
                            return json;
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to process weather data", e);
                        }
                    });
//        }
    }

    public WeatherDTO jsonToDTO(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        String name = jsonNode.get("location").get("name").asText();
        double temp_c = jsonNode.get("current").get("temp_c").asDouble();
        String text = jsonNode.get("current").get("condition").get("text").asText();
        WeatherDTO dto = new WeatherDTO(name, text, temp_c, LocalDateTime.now());
        weatherJdbcService.create(name, text, temp_c, Timestamp.valueOf(LocalDateTime.now()));
        //weatherCache.put(name, text, temp_c, LocalDateTime.now());
        return dto;
    }
}