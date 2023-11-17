package com.mysite.core.config;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Weather Scheduler Configuration" ,
                       description = "Configuration for changing the time of Weather Scheduler ")

public @interface  WeatherSchedulerConfiguration {

    @AttributeDefinition(name = "Cron Expression for Scheduler",
                         description = "",
                         type = AttributeType.STRING)
    String scheduler_expression() default "0 */30 * ? * *";

    @AttributeDefinition(name = "Node Path for the weather",
            description = "",
            type = AttributeType.STRING)
    String p() default "/content/usergenerated/content";

    @AttributeDefinition(name = "Node name for the weather",
            description = "",
            type = AttributeType.STRING)
    String n() default "weather";
}
