package ru.lanwen.junit;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import java.util.logging.Level;

public class MainApplication extends ResourceConfig {

    public MainApplication() {
        this(new Class[0]);
    }

    public MainApplication(Class<?>... classes) {
        property(ServerProperties.APPLICATION_NAME, "Application");
        
        register(RequestContextFilter.class);
        register(JacksonFeature.class);
        register(MultiPartFeature.class);
        register(new LoggingFeature(null, Level.SEVERE, LoggingFeature.Verbosity.PAYLOAD_ANY, null));
        registerClasses(classes);
    }

}
