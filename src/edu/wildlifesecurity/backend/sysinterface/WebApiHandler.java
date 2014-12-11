package edu.wildlifesecurity.backend.sysinterface;


import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.Message.Commands;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;
import edu.wildlifesecurity.framework.tracking.Capture;

@Path("/api")
public class WebApiHandler {
		
	@GET @Path("/config")
	@Produces("text/xml")
	public String getConfig(@DefaultValue("0") @QueryParam("trap") int trap){
		NoNameCoder nameCoder = new NoNameCoder(); 
		XStream magicApi = new XStream(new StaxDriver(nameCoder));
		magicApi.registerConverter(new edu.wildlifesecurity.backend.MapEntryConverter());
		magicApi.alias("configuration", Map.class);

		Map<String, Object> map;
		if(trap == 0){
			map = new HashMap<String, Object>();
			WebApiInterface.getInstance().getRepository().loadConfiguration(map);
		}else{
			map = WebApiInterface.getInstance().getManager().getTrapDeviceConfiguration(trap);
		}
		return magicApi.toXML(map);
		
	}
	
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
    @Produces("text/xml")
    public String getTrapDevices() {
    	XStream magicApi = new XStream();
		magicApi.alias("TrapDevices", LinkedList.class);
		magicApi.alias("TrapDevice", TrapDevice.class);
        return magicApi.toXML(WebApiInterface.getInstance().getCommunicator().getConnectedTrapDevices());
    }
    
    @GET @Path("captures")
    @Produces("text/plain")
    public String getCaptures() {
    	XStream magicApi = new XStream();
		magicApi.alias("Captures", LinkedList.class);
		magicApi.alias("Capture", Capture.class);
		magicApi.denyTypes(new Class[] { Mat.class });
        return magicApi.toXML(WebApiInterface.getInstance().getRepository().getCaptureDefinitions());
    }
    
    @GET @Path("capture-image")
    @Produces("image/jpeg")
    public Response getCaptureImage(@QueryParam("id") int id) {
    	MatOfByte mob = new MatOfByte();
    	Highgui.imencode(".jpg", WebApiInterface.getInstance().getRepository().getCaptureImage(id), mob);
    	
    	return Response.ok(new ByteArrayInputStream(mob.toArray())).build();
    }
}