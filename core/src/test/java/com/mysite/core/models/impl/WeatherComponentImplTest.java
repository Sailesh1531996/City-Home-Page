package com.mysite.core.models.impl;

import static org.mockito.Mockito.*;

import com.mysite.core.service.WeatherService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
public class WeatherComponentImplTest {

    @Mock
    WeatherService weatherService;

    @Mock
    Resource resource;

    @InjectMocks
    WeatherComponentImpl weatherComponent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCityName() {
        String cityName = "Sample City";
        weatherComponent.cityName = cityName;
        String result = weatherComponent.getCityName();
        assertEquals(cityName, result);
    }
    @Test
    public void testGetValue() {
        String cityName = "Sample City";
        String temperature = "25°C"; // Replace with your expected temperature value

        when(resource.adaptTo(WeatherService.class)).thenReturn(weatherService);
        when(weatherService.getTemperature(cityName)).thenReturn(temperature);
        weatherComponent.cityName = cityName;

        String result = weatherComponent.getValue();

        assertEquals(temperature, result);
    }
}

