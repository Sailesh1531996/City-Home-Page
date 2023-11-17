package com.mysite.core.service;

// Interface of the service to the temperature by passing city name as parameter
public interface WeatherService {
    String getTemperature(String cityName);
}
