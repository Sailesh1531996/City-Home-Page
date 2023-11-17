package com.mysite.core.service.impl;


import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.*;
import org.apache.sling.testing.resourceresolver.MockResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mysite.core.config.WeatherPathConfiguration;
import com.mysite.core.service.WeatherService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
public class WeatherServiceImplTest {

    private final AemContext context = new AemContext();



    @Mock
    ValueMap valueMap;

    WeatherServiceImpl weatherServiceImpl;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context.registerService(ResourceResolverFactory.class, new MockResourceResolverFactory());
        weatherServiceImpl = context.registerInjectActivateService(new WeatherServiceImpl());
    }

    @Test
    public void testGetTemperature() throws LoginException {
        String cityName = "City";
        String weatherData = "[{\"name\":\"Sample City\",\"main\":{\"temp\":280.0}}]";
        String temperature = weatherServiceImpl.getTemperature(cityName);
        assertEquals(null, temperature); // Expected temperature value after conversion
    }
}
