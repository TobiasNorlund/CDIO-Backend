package edu.wildlifesecurity.backend.sysinterface;


import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api")
public class WebApiHandler {
	
    @GET @Path("/config-option")
    @Produces("text/plain")
    public String getConfigOption(@QueryParam("o") String option) {
        return WebApiInterface.getInstance().getRepository().getConfigOption(option).toString();
    }
    
    @PUT @Path("/config-option")
    public Response getConfigOption(@QueryParam("o") String option, @QueryParam("v") String value) {
        WebApiInterface.getInstance().getRepository().setConfigOption(option, value);
        return Response.status(Status.ACCEPTED).build();
    }
    
    /*@GET @Path("log")
    @Produces("text/plain")
    public String getConfigOption(@QueryParam("t_start") Date startTime) {
        //return WebApiInterface.getInstance().getRepository().
    }*/

}