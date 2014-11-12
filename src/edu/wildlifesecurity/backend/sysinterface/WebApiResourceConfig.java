package edu.wildlifesecurity.backend.sysinterface;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class WebApiResourceConfig extends ResourceConfig {
    public WebApiResourceConfig() {
        packages("edu.wildlifesecurity.backend");
    }
}