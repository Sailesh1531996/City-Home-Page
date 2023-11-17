package com.mysite.core.schedulers;


import com.mysite.core.config.WeatherSchedulerConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

//scheduler to call the api and store the data in a node
@Component(immediate = true,service=WeatherScheduler.class)
@Designate(ocd = WeatherSchedulerConfiguration.class)
public class WeatherScheduler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherScheduler.class);
    private String nodePath ;

    private String nodeName;

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Activate
    protected void activate(WeatherSchedulerConfiguration config) {
        nodePath = config.p();
        nodeName = config.n();
        scheduleAdd(config);
    }

    @Modified
    protected void modified(WeatherSchedulerConfiguration config) {
        scheduleRemove();
        scheduleAdd(config);
    }


    @Deactivate
    protected void deactivate(WeatherSchedulerConfiguration config) {
        scheduleRemove();
    }


    private void scheduleAdd(WeatherSchedulerConfiguration config){
        ScheduleOptions scheduleOptions = scheduler.EXPR(config.scheduler_expression());
        scheduleOptions.name("WeatherScheduler");
        scheduler.schedule(this, scheduleOptions);
        //to call the method at the time when the code is build
        ScheduleOptions scheduleNow = scheduler.NOW();
        scheduler.schedule(this,scheduleNow);
    }

    private void scheduleRemove() {
        scheduler.unschedule("WeatherScheduler");
    }

    //api has been called at regular interval of time
    @Override
    public void run() {
        LOG.info("Scheduler started working ");
        String apiKey = "68e44968bd685b263fc5a27a19538a3b";
        String cityIds = "2950158,2867714,2911298,6548737,6551127,2925533,2907911,2825297,2934246,2944387,2849483,2886242";
        String apiUrl = "http://api.openweathermap.org/data/2.5/group?id=" + cityIds + "&appid=" + apiKey;

        try {
            URI uri = new URI(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String responseData = IOUtils.toString(inputStream, "UTF-8");
                JSONObject jsonResponse = new JSONObject(responseData);
                JSONArray cityWeatherData = jsonResponse.getJSONArray("list");
                storeWeatherDataInJCR(cityWeatherData);
            } else {
                LOG.error("HTTP request failed : {}", responseCode);
            }
            LOG.info("Weather  request successful");
        } catch (Exception e) {
            LOG.error("Weather  request failed", e);
        }

    }

    //to store the data in a path which can be used by the service
    private void storeWeatherDataInJCR(JSONArray weatherData) {
        ResourceResolver resourceResolver =null;
        try  {

            resourceResolver = getServiceResourceResolver();
            Resource parentResource = resourceResolver.getResource(nodePath);
            Node parentResourceNode = parentResource != null ? parentResource.adaptTo(Node.class) : null;
            if(!(parentResourceNode != null && parentResourceNode.hasNode(nodeName))) {
                Node newNode = parentResourceNode != null ? parentResourceNode.addNode(nodeName, "nt:unstructured") : null;
                if (newNode != null) {
                    newNode.setProperty("weatherData", weatherData.toString());
                }
            }
            else {
                Node newNode = parentResourceNode.getNode(nodeName);
                newNode.setProperty("weatherData", weatherData.toString());
            }
            assert parentResourceNode != null;
            parentResourceNode.getSession().save();
            resourceResolver.commit();
            LOG.info("Weather data stored successfully.");
        } catch (Exception e) {
            LOG.error("Error  weather data {}", e.getMessage());

        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }

    private ResourceResolver getServiceResourceResolver() throws LoginException {
        String userName ="aemprojectserviceuser";
        Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, userName);
        return resolverFactory.getServiceResourceResolver(params);
    }

}
