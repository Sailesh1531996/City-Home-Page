package com.mysite.core.models.impl;


import com.mysite.core.models.WeatherComponent;
import com.mysite.core.schedulers.WeatherScheduler;
import com.mysite.core.service.WeatherService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Model(adaptables = Resource.class,adapters = WeatherComponent.class ,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class WeatherComponentImpl implements WeatherComponent{
    private static final Logger LOG = LoggerFactory.getLogger(WeatherScheduler.class);

    @OSGiService
    WeatherService weatherService ;

    @Inject()
    String cityName;


    private Double value ;


    @Override
    public String getCityName() {
        return cityName;
    }
   //implementation of the getValue method
    public String getValue() {
        return weatherService.getTemperature(cityName);
    }

}
