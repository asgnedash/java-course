package org.example.fintech.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.fintech.model.Weather;
import org.example.fintech.service.WeatherService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controller class handling weather-related API requests.
 * This class defines RESTful endpoints for CRUD weather information for specific cities.
 * The class utilizes the WeatherService to process these requests and return responses.
 */

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    public static final int CREATED_CODE = 201; // The variable for using the status code 201.
    public static final int OK_CODE = 200;      // The variable for using the status code 200.

    WeatherService weatherService = new WeatherService();

    @Operation(summary = "Add a new city", responses = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{city}")
    public ResponseEntity<Object> createWeather(@PathVariable String city, @RequestParam("temperature") double temperature,
                                                @RequestParam("timestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        try {
            Weather newWeather = new Weather(city, temperature, timestamp);
            int result = weatherService.createWeather(newWeather);
            if (result == CREATED_CODE) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The weather was not added.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Get the temperature for the specified city on the current date", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{city}")
    public ResponseEntity<Object> readTemperatureByCity(@PathVariable String city) {
        try {
            Double temperature = weatherService.readTemperatureByCity(city);
            if (temperature != null) {
                return ResponseEntity.ok(temperature);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Update the weather for the specified city", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Weather.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{city}")
    public ResponseEntity<Object> updateWeather(@PathVariable String city, @RequestParam("temperature") double temperature,
                                                @RequestParam("timestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        try {
            Weather newWeather = new Weather(city, temperature, timestamp);
            int result = weatherService.updateWeather(newWeather);
            if (result == CREATED_CODE) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else if (result == OK_CODE) {
                return ResponseEntity.ok(newWeather);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The weather was not updated.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete the city", responses = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{city}")
    public ResponseEntity<Object> deleteWeather(@PathVariable String city) {
        try {
            weatherService.deleteWeather(city);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
