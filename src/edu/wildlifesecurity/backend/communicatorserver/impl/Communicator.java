package edu.wildlifesecurity.backend.communicatorserver.impl;

import edu.wildlifesecurity.framework.AbstractComponent;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;

public class Communicator extends AbstractComponent implements
		ICommunicatorServer {
	
	private SmsChannel smsChannel;
	private InternetChannel internetChannel;
	
	@Override
	public void init(){
		// TODO: Read from configuration which channels to use
		// TODO: Start listening for trap devices 
		// TODO: Add handler for sending configuration on new connection
	}

	@Override
	public ISubscription addEventHandler(EventType type, IEventHandler<MessageEvent> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
}
