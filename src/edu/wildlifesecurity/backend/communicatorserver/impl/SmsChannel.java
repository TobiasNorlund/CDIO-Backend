package edu.wildlifesecurity.backend.communicatorserver.impl;

import java.util.List;
import java.util.Map;

import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ILogger;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.communicatorserver.ConnectEvent;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;

/**
 * Sends and recieves sms messages from and to trapdevices
 * @author Tobias
 *
 */
public class SmsChannel extends AbstractChannel {

	protected SmsChannel(ILogger logger, Map<String, Object> config) {
		super(logger, config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ISubscription addMessageEventHandler(EventType type,
			IEventHandler<MessageEvent> handler) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void sendMessage(Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startListen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	List<TrapDevice> getConnectedTrapDevices() {
		// TODO Auto-generated method stub
		return null;
	}



}
