package edu.wildlifesecurity.backend.communicatorserver.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import edu.wildlifesecurity.backend.sysinterface.gui.JavaFXGUI;
import edu.wildlifesecurity.framework.AbstractComponent;
import edu.wildlifesecurity.framework.EventDispatcher;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ILogger;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.communicatorserver.ConnectEvent;
import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;

public class Communicator extends AbstractComponent implements ICommunicatorServer {
	
	private AbstractChannel channel;
	private EventDispatcher<ConnectEvent> connectEventDispatcher = new EventDispatcher<ConnectEvent>();
	
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
			channel.addMessageEventHandler(MessageEvent.getEventType(Message.Commands.HANDSHAKE_REQ), new IEventHandler<MessageEvent>(){
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
					connectEventDispatcher.dispatch(new ConnectEvent(ConnectEvent.NEW_TRAPDEVICE,new TrapDevice(1,"Hemma"))); // TODO: Fix!
					log.info("New TrapDevice connected!");
				}
			});
			
	    	channel.addConnectEventHandler(ConnectEvent.DISCONNECT_TRAPDEVICE, new IEventHandler<ConnectEvent>(){

				@Override
				public void handle(ConnectEvent arg0) {
					connectEventDispatcher.dispatch(new ConnectEvent(ConnectEvent.DISCONNECT_TRAPDEVICE,arg0.getTrapDevice()));
					
				}
			});

		}catch(Exception e){
			log.error("Error in CommunicatorServer. Cannot instantiate channel: " + e.getMessage());
		}
	}

	@Override
	public ISubscription addMessageEventHandler(EventType type, IEventHandler<MessageEvent> handler) {
		return channel.addMessageEventHandler(type, handler);
	}

	@Override
	public void sendMessage(Message msg) {
		channel.sendMessage(msg);
	}

	@Override
	public List<TrapDevice> getConnectedTrapDevices() {
		return channel.getConnectedTrapDevices();
	}


	@Override
	public ISubscription addConnectEventHandler(EventType type,
			IEventHandler<ConnectEvent> handler) {
		return connectEventDispatcher.addEventHandler(type, handler);
	}
	
}
