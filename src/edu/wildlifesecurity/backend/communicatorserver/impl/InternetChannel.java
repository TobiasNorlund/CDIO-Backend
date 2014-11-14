package edu.wildlifesecurity.backend.communicatorserver.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import edu.wildlifesecurity.framework.EventDispatcher;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ILogger;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;

/**
 * Connects multiple trapdevices using the internet to exchange messages
 * @author Tobias
 *
 */
public class InternetChannel extends AbstractChannel {
	
	private Thread listeningThread;
	private List<TrapDeviceConnection> trapDeviceConnections;
	private AsynchronousServerSocketChannel server;
	private EventDispatcher<MessageEvent> eventDispatcher = new EventDispatcher<MessageEvent>();
	
	public InternetChannel(ILogger logger, Map<String, Object> config) {
		super(logger, config);
	}

	@Override
	public ISubscription addEventHandler(EventType type, IEventHandler<MessageEvent> handler) {
		return eventDispatcher.addEventHandler(type, handler);
	}

	@Override
	public void sendMessage(Message message) {
		trapDeviceConnections.get(message.getReceiver()-1).sendMessage(message.getMessage());
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
					trapDeviceConnections =  new LinkedList<TrapDeviceConnection>();
					server = AsynchronousServerSocketChannel.open();
					server.bind(new InetSocketAddress(Integer.parseInt(configuration.get("CommunicatorServer_Port").toString())));
					
					log.info("TCP server started. Waiting for connections...");
					
				} catch (IOException e) {
					e.printStackTrace();
					log.error("Error in InternetChannel: Could not start socket server" + e.getMessage());
					return;
				} 
				
				int trapDeviceCounter = 1;
				
				while(true){
					
					try {
						TrapDeviceConnection connection = new TrapDeviceConnection(trapDeviceCounter++, server.accept().get(), eventDispatcher);
						
						trapDeviceConnections.add(connection);
						
					} catch (IOException | ExecutionException | InterruptedException e) {
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
		for(TrapDeviceConnection tdc : trapDeviceConnections)
			list.add(tdc.getTrapDevice());
		
		return list;
	}
	
	private class TrapDeviceConnection implements CompletionHandler<Integer, ByteBuffer> {
		
		private TrapDevice trapDevice;
		private EventDispatcher<MessageEvent> eventDispatcher;
		private AsynchronousSocketChannel connection;
		private ByteBuffer incomingBuffer = ByteBuffer.allocateDirect(2048);
		
		private TrapDeviceConnection(int id, AsynchronousSocketChannel conn, EventDispatcher<MessageEvent> eventDispatcher) throws IOException{
			this.trapDevice = new TrapDevice();
			this.trapDevice.id = id;
			this.connection = conn;
			this.eventDispatcher = eventDispatcher;

			connection.read(incomingBuffer, incomingBuffer, this);
		}
		
		public void completed(Integer result, ByteBuffer buffer)
        {
            buffer.flip();
            String msgReceived = Charset.defaultCharset().decode(buffer).toString().replaceAll("\\n", "");
            log.info("TrapDevice " + trapDevice.id + ": " + msgReceived);
            eventDispatcher.dispatch(
            		new MessageEvent(MessageEvent.getEventType(msgReceived.split(",")[0]), 
            				new Message(msgReceived, trapDevice.id)));
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
