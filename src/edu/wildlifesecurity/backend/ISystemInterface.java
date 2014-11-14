package edu.wildlifesecurity.backend;

import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;
import edu.wildlifesecurity.framework.repository.IRepository;

public interface ISystemInterface {

	void link(IRepository repository, ICommunicatorServer communicator);
	
}
