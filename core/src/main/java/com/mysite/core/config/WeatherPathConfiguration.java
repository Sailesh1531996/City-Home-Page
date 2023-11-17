package com.mysite.core.config;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Weather Path Configuration" ,
                       description = "Configuration for changing the path of Weather node ")

public @interface WeatherPathConfiguration {

    @AttributeDefinition(name = "Node Path for the weather",
                         description = "",
                         type = AttributeType.STRING)
    String p() default "/content/usergenerated/content/weather";


}
