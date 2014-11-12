package edu.wildlifesecurity.backend;

import java.util.LinkedList;
import java.util.List;

import edu.wildlifesecurity.backend.actuator.impl.DefaultActuator;
import edu.wildlifesecurity.backend.communicatorserver.impl.Communicator;
import edu.wildlifesecurity.backend.repository.impl.FileRepository;
import edu.wildlifesecurity.backend.sysinterface.WebApiInterface;
import edu.wildlifesecurity.framework.SurveillanceServerManager;

public class BackendServerApplication {

	public static void main(String[] args) {

		/// Entry point for backend server application
		
		// --------------------------------------
		
		// Create components
		DefaultActuator actuator = new DefaultActuator();
		FileRepository repository = new FileRepository();
		Communicator communicator = new Communicator();
			
		// Create manager
		SurveillanceServerManager manager = new SurveillanceServerManager(actuator, repository, communicator);
		
		// Start manager!
		manager.start();
		
		// Create system interfaces ( read from args which to use? )
		List<ISystemInterface> systemInterfaces = new LinkedList<ISystemInterface>();
		systemInterfaces.add(new WebApiInterface());
		
		
		// Link System Interfaces (GUI)
		for(ISystemInterface sysInt : systemInterfaces)
			sysInt.link(repository);
		
		System.out.println("Server is running...");
	}

}
