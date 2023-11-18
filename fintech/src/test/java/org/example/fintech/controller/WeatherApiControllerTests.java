package org.example.fintech.controller;

import org.example.fintech.service.WeatherJdbcService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherApiControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetWeather() throws Exception {
        mockMvc.perform(get("/api/weather/Moscow"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(get("/api/weather/Moskow"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
