package com.mysite.core.service.impl;

import com.mysite.core.config.WeatherPathConfiguration;
import com.mysite.core.schedulers.WeatherScheduler;
import com.mysite.core.service.WeatherService;
import org.apache.sling.api.resource.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(service = WeatherService.class,immediate = true)
@Designate(ocd = WeatherPathConfiguration.class)
public class WeatherServiceImpl implements WeatherService{
    private static final Logger LOG = LoggerFactory.getLogger(WeatherScheduler.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;


    private String nodePath ;

    @Activate
    protected void activate(WeatherPathConfiguration config) {
        nodePath =config.p();
    }

    //method to get the temperature by passing the city name
    public String getTemperature(String cityName) {
        String temperature = null;
        ResourceResolver resourceResolver = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(ResourceResolverFactory.SUBSERVICE, "aemprojectserviceuser");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(params);
            Resource resource = resourceResolver.getResource(nodePath);
            if (!(resource == null)) {
                String propertyName = "weatherData";
                ValueMap properties = resource.adaptTo(ValueMap.class);
                String valueArray = properties.get(propertyName, String.class);
                JSONArray cityWeatherData = new JSONArray(valueArray);
                for (int i = 0; i < cityWeatherData.length(); i++) {
                    JSONObject cityData = cityWeatherData.getJSONObject(i);
                    String cityNameData = cityData.getString("name");
                    if(cityNameData.equalsIgnoreCase(cityName)) {
                        JSONObject mainData = cityData.getJSONObject("main");
                        Double value = mainData.getDouble("temp") - 273.15;
                        temperature =Integer.toString((int) Math.round(value));
                        break;
                    }
                }
            } else {
                LOG.error("Error : no resource ");
            }
        }
        catch (JSONException |LoginException e) {
            throw new RuntimeException(e);
        }

         return temperature;
    }
}

