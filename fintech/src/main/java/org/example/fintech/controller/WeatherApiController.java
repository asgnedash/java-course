package org.example.fintech.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.fintech.service.WeatherApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
public class WeatherApiController {

    private final WeatherApiService weatherApiService;

    @Autowired
    public WeatherApiController(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
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
    public Mono<String> getWeather(@PathVariable String city) {
        return weatherApiService.getCurrentWeather(city);
    }
}