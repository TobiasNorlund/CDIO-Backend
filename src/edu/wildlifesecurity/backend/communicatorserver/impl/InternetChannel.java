package edu.wildlifesecurity.backend.communicatorserver.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import edu.wildlifesecurity.backend.communicatorserver.impl.InternetChannel.TrapDeviceConnection;
import edu.wildlifesecurity.framework.EventDispatcher;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ILogger;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.communicatorserver.ConnectEvent;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;

/**
 * Connects multiple trapdevices using the internet to exchange messages
 * @author Tobias
 *
 */
public class InternetChannel extends AbstractChannel {
	
	private Thread listeningThread;
	private Map<Integer, TrapDeviceConnection> trapDeviceConnections;
	private AsynchronousServerSocketChannel server;
	private EventDispatcher<MessageEvent> messageEventDispatcher = new EventDispatcher<MessageEvent>();
	private EventDispatcher<ConnectEvent> connectEventDispatcher = new EventDispatcher<ConnectEvent>();




	public InternetChannel(ILogger logger, Map<String, Object> config) {
		super(logger, config);
	}

	@Override
	public ISubscription addMessageEventHandler(EventType type, IEventHandler<MessageEvent> handler) {
		return messageEventDispatcher.addEventHandler(type, handler);
	}
	@Override
	public ISubscription addConnectEventHandler(EventType type, IEventHandler<ConnectEvent> handler) {
		return connectEventDispatcher.addEventHandler(type, handler);
	}
	

	@Override
	public void sendMessage(Message message) {
		trapDeviceConnections.get(message.getReceiver()).sendMessage(message.getMessage());
	}

	/**
	 * Starts a TCP server
	 */
	@Override
	public void startListen() {
		
		listeningThread = new Thread(new Runnable(){

			@Override
			public void run() {
				
				try {
					trapDeviceConnections =  new HashMap<Integer,TrapDeviceConnection>();
					server = AsynchronousServerSocketChannel.open();
					server.bind(new InetSocketAddress(Integer.parseInt(configuration.get("CommunicatorServer_Port").toString())));
					
					log.info("TCP server started on port " + configuration.get("CommunicatorServer_Port") + ". Waiting for connections...");
					
				} catch (IOException e) {
					e.printStackTrace();
					log.error("Error in InternetChannel: Could not start socket server" + e.getMessage());
					return;
				} 
				
				int trapDeviceCounter = 1;
				
				while(true){
					
					try {
						int id = trapDeviceCounter++;
						TrapDeviceConnection connection = new TrapDeviceConnection(id, server.accept().get(), messageEventDispatcher);
						
						trapDeviceConnections.put(id, connection);
						
					} catch (Exception e) {
						e.printStackTrace();
						log.error("Error in InternetChannel: Could not establish connection to trap device: " + e.getMessage());
						return;
					}
					
				}
			}
			
		});
		
		listeningThread.start();
		
	}
	
	@Override
	List<TrapDevice> getConnectedTrapDevices() {
		List<TrapDevice> list = new LinkedList<TrapDevice>();
		for(Entry<Integer,TrapDeviceConnection> tdc : trapDeviceConnections.entrySet())
			list.add(tdc.getValue().getTrapDevice());
		
		return list;
	}
	
	public class TrapDeviceConnection implements CompletionHandler<Integer, ByteBuffer> {
		
		private TrapDevice trapDevice;
		private EventDispatcher<MessageEvent> eventDispatcher;
		private AsynchronousSocketChannel connection;
		private ByteBuffer incomingBuffer = ByteBuffer.allocateDirect(2048);
		
		private String currentMessage = "";
		
		private TrapDeviceConnection(int id, AsynchronousSocketChannel conn, EventDispatcher<MessageEvent> eventDispatcher) throws IOException{
			this.trapDevice = new TrapDevice();
			this.trapDevice.id = id;
			this.connection = conn;
			this.eventDispatcher = eventDispatcher;

			connection.read(incomingBuffer, incomingBuffer, this);
		}
		
		public void completed(Integer result, ByteBuffer buffer)
        {
			if(result == -1)
			{
				try {
					connection.close();
					log.warn("Connection to trap device " + trapDevice.id + " was lost");
					connectEventDispatcher.dispatch(new ConnectEvent(ConnectEvent.DISCONNECT_TRAPDEVICE, this.trapDevice));
				} catch (IOException e) {
					log.error("Could not close connection to trap device " + trapDevice.id);
				}
				
				//remove trap device that disconnect
				//connectEventDispatcher.dispatch(new ConnectEvent(ConnectEvent.DISCONNECT_TRAPDEVICE,new TrapDevice(1,"Hemma")));

				Collection<TrapDeviceConnection> set = trapDeviceConnections.values();
				Iterator<TrapDeviceConnection> itr = set.iterator();
			    while (itr.hasNext())
			    {
			    	TrapDeviceConnection trapConnection = itr.next();
			        if (trapConnection.getTrapDevice().id==trapDevice.id) {
			        	itr.remove();
			        }
			    }
				
				return;
			}
			buffer.flip();
			List<String> fullMessages = new LinkedList<String>();
			while(buffer.hasRemaining()){
				Byte b = buffer.get();
				// If newline character, split messages
				if(b == 0x0A){
					fullMessages.add(currentMessage);
					currentMessage = "";
					continue;
				}
				// Decode byte to string and add to current message
				try {
					currentMessage += new String(new byte[] {b}, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println("Fel! " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			for(String message : fullMessages){
	            //log.info("TrapDevice " + trapDevice.id + ": " + message);
	            eventDispatcher.dispatch(
	            		new MessageEvent(MessageEvent.getEventType(message.split(",")[0]), 
	            				new Message(message, trapDevice.id)));
			}
			buffer.clear();
			connection.read(incomingBuffer, incomingBuffer, this);
        }
        public void failed(Throwable exc, ByteBuffer buffer)
        {
            log.error("Error in InternetChannel. Could not read from TCP connection: " + exc.getMessage());
        }
 
        public void sendMessage(String message){
        	try {
				connection.write(ByteBuffer.wrap((message + "\n").getBytes())).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				log.error("Error in InternetChannel. Could not send data to client: " + e.getMessage());
			}
        }
        
        public TrapDevice getTrapDevice(){
        	return trapDevice;
        }

	}




}
