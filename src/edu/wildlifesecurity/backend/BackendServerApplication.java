package edu.wildlifesecurity.backend;

import java.util.LinkedList;
import java.util.List;

import javafx.concurrent.Task;
import edu.wildlifesecurity.backend.communicatorserver.impl.Communicator;
import edu.wildlifesecurity.backend.repository.impl.FileRepository;
import edu.wildlifesecurity.backend.sysinterface.WebApiInterface;
import edu.wildlifesecurity.backend.sysinterface.gui.JavaFXGUI;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.SurveillanceServerManager;
import edu.wildlifesecurity.framework.Message.Commands;
import edu.wildlifesecurity.framework.actuator.impl.DefaultActuator;
import edu.wildlifesecurity.framework.tracking.impl.SerializableCapture;

public class BackendServerApplication {

	public static void main(String[] args) {

		/// Entry point for backend server application
		
		// --------------------------------------
		
		// Inject dependencies
		SerializableCapture.decoder = new PngDecoder();
		
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
		systemInterfaces.add(new JavaFXGUI());
		
		
		// Link System Interfaces (GUI)
		for(ISystemInterface sysInt : systemInterfaces)
			sysInt.link(repository, communicator, actuator, manager);

		
		System.out.println("Server is running...");
	}

}
