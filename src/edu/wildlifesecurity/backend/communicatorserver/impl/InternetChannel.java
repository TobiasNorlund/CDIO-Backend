package edu.wildlifesecurity.backend.communicatorserver.impl;

import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.MessageEvent;

/**
 * Connects multiple trapdevices using the internet to exchange messages
 * @author Tobias
 *
 */
public class InternetChannel implements IChannel {

	@Override
	public ISubscription addEventHandler(EventType type, IEventHandler<MessageEvent> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub

	}

}
