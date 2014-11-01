package edu.wildlifesecurity.backend.communicatorserver.impl;

import java.util.Map;

import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ILogger;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;

/**
 * Represents a communication channel. A channel could be for example sms or internet.
 */
public abstract class AbstractChannel {
	
	protected ILogger log;
	protected Map<String, Object> configuration;
	
	protected AbstractChannel(ILogger logger, Map<String, Object> config){
		this.log = logger;
		this.configuration = config;
	}
	
	/**
	 * Starts listen for trap devices
	 */
	abstract void startListen();
	
	/**
	 * Adds support for receiving events when messages arrives from TrapDevices
	 */
	abstract ISubscription addEventHandler(EventType type, IEventHandler<MessageEvent> handler);
	
	/**
	 * Sends a string message through the channel to the TrapDevice that is contained in the Message instance.
	 */
	abstract void sendMessage(Message message);

}
