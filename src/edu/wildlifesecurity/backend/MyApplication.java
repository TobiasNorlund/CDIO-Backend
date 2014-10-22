package edu.wildlifesecurity.backend;

import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {
    public MyApplication() {
        packages("edu.wildlifesecurity.backend");
    }
}