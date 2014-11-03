package edu.wildlifesecurity.backend.communicatorserver.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.Map;

import edu.wildlifesecurity.framework.AbstractComponent;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ILogger;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;

public class Communicator extends AbstractComponent implements ICommunicatorServer {
	
	private AbstractChannel channel;
	
	@Override
	public void init(){
		
		try{
			
			// Read from configuration which channels to use
			Class<?> cl = Class.forName(configuration.get("CommunicatorServer_Channel").toString());
			Constructor<?> cons = cl.getConstructor(ILogger.class, Map.class);
			channel = (AbstractChannel) cons.newInstance(log, configuration);
			
			// Start listening for trap devices
			channel.startListen();
			
			// Add handler for sending configuration on new connection
			channel.addEventHandler(MessageEvent.getEventType(Message.Commands.HANDSHAKE_REQ), new IEventHandler<MessageEvent>(){
				@Override
				public void handle(MessageEvent event) {
					// A new Trap Device has connected! Send Handshake Acknowledge
					
					// Construct message. Serialize configuration HashMap to Base64 encoded string
					String message = Message.Commands.HANDSHAKE_ACK + ",";
					try{
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ObjectOutput out = new ObjectOutputStream(bos);   
						out.writeObject(configuration);
						message += new String(Base64.getEncoder().encode(bos.toByteArray()));
					}catch(IOException ex){
						ex.printStackTrace();
						log.error("Error in Communicator. Couldn't serialize configuration: " + ex.getMessage());
					}

					channel.sendMessage(new Message(event.getMessage().getSender(), message));
					log.info("New TrapDevice connected!");
				}
			});

		}catch(Exception e){
			log.error("Error in CommunicatorServer. Cannot instantiate channel: " + e.getMessage());
		}
	}

	@Override
	public ISubscription addEventHandler(EventType type, IEventHandler<MessageEvent> handler) {
		return channel.addEventHandler(type, handler);
	}

	@Override
	public void sendMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
}
