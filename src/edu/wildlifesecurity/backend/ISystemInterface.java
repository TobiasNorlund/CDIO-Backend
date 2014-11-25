package edu.wildlifesecurity.backend;

import edu.wildlifesecurity.framework.actuator.IActuator;
import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;
import edu.wildlifesecurity.framework.repository.IRepository;

public interface ISystemInterface {

	void link(IRepository repository, ICommunicatorServer communicator, IActuator actuator);
	
}
