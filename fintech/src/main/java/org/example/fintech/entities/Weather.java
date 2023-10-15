package org.example.fintech.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "Weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "weather_type_id", nullable = false)
    private WeatherType weatherType;

    @Column(name = "temperature", nullable = false)
    private Float temperature;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;
}
