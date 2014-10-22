package edu.wildlifesecurity.backend;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/resource")
public class MyResource {

    @GET @Path("/get")
    @Produces("text/plain")
    public String postMyBean() {
        return "Hello World";
    }
}