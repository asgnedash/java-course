package org.example.fintech.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class WeatherDTO {

    private String city;
    private String weatherType;
    private double temperature;
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "WeatherDTO{" +
                "city='" + city + '\'' +
                ", weatherType='" + weatherType + '\'' +
                ", temperature=" + temperature +
                ", timestamp=" + timestamp +
                '}';
    }
}
