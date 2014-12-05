package edu.wildlifesecurity.backend.sysinterface;


import java.io.ByteArrayInputStream;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.Message.Commands;

@Path("/api")
public class WebApiHandler {
	
/*
	@GET @Path("/config")
	@Produces("text/xml")*/
	
    @GET @Path("/config-option")
    @Produces("text/plain")
    public String getConfigOption(@QueryParam("o") String option) {
        return WebApiInterface.getInstance().getRepository().getConfigOption(option).toString();
    }
    
    @PUT @Path("/config-option")
    public Response setConfigOption(@QueryParam("o") String option, @QueryParam("v") String value) {
        WebApiInterface.getInstance().getRepository().setConfigOption(option, value);
        return Response.status(Status.ACCEPTED).build();
    }
    
    @PUT @Path("/config-trap-option")
    public Response setTrapConfigOption(@QueryParam("o") String option, @QueryParam("v") String value, @QueryParam("trap") int trapDeviceId) {

    	switch(option.split("_")[0]){
    	case "CommunicatorServer":
    		WebApiInterface.getInstance().getCommunicator().setConfigOption(option, value);
    		break;
    	case "Actuator":
    		WebApiInterface.getInstance().getActuator().setConfigOption(option, value);
    		break;
    	case "Repository":
    		WebApiInterface.getInstance().getRepository().setConfigOption(option, value);
    		break;
    	default:
    		WebApiInterface.getInstance().getCommunicator().sendMessage(new Message(trapDeviceId, Commands.SET_CONFIG + "," + option + "," + value));    	
    	}
    	
        return Response.status(Status.ACCEPTED).build();
    }
    
    @GET @Path("log")
    @Produces("text/plain")
    public String getConfigOption(@QueryParam("t_start") Date startTime) {
        return WebApiInterface.getInstance().getRepository().getLog(startTime);
    }

    @GET @Path("trap-devices")
    @Produces("text/plain")
    public String getTrapDevices() {
        return WebApiInterface.getInstance().getCommunicator().getConnectedTrapDevices().toString();
    }
    
    @GET @Path("captures")
    @Produces("text/plain")
    public String getCaptures() {
        return WebApiInterface.getInstance().getRepository().getCaptureDefinitions().toString();
    }
    
    @GET @Path("capture-image")
    @Produces("image/jpeg")
    public Response getCaptureImage(@QueryParam("id") int id) {
    	MatOfByte mob = new MatOfByte();
    	Highgui.imencode("jpg", WebApiInterface.getInstance().getRepository().getCaptureImage(id), mob);
    	
    	return Response.ok(new ByteArrayInputStream(mob.toArray())).build();
    }
}