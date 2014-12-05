package edu.wildlifesecurity.backend.sysinterface;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

import edu.wildlifesecurity.backend.ISystemInterface;
import edu.wildlifesecurity.framework.SurveillanceServerManager;
import edu.wildlifesecurity.framework.actuator.IActuator;
import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;
import edu.wildlifesecurity.framework.repository.IRepository;

public class WebApiInterface implements ISystemInterface {
	
	private static WebApiInterface instance;
	public static WebApiInterface getInstance(){
		return instance;
	}
	
	private IRepository repository;
	private ICommunicatorServer communicator;
	private IActuator actuator;
	private SurveillanceServerManager manager;

	@Override
	public void link(IRepository repository, ICommunicatorServer communicator, IActuator actuator, SurveillanceServerManager manager ) {
		instance = this;
		
		this.repository = repository;
		this.communicator = communicator;
		this.actuator = actuator;
		this.manager = manager;
		
		// Start web service
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
		ResourceConfig config = new WebApiResourceConfig();
		HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
		
	}

	public IRepository getRepository(){
		return repository;
	}
	
	public ICommunicatorServer getCommunicator(){
		return communicator;
	}
	
	public IActuator getActuator(){
		return actuator;
	}
}
