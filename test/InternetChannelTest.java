import java.util.HashMap;
import java.util.Map;

import edu.wildlifesecurity.backend.communicatorserver.impl.InternetChannel;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;


public class InternetChannelTest {

	public static void main(String[] args){
		
		// Test for the InternetChannel
		
		Map<String, Object> config = new HashMap<String,Object>();
		config.put("CommunicationServer_Port", 9090);
		
		InternetChannel channel = new InternetChannel(new SystemOutLogger(), config);
		
		channel.startListen();
		
		channel.addEventHandler(MessageEvent.getEventType(Message.Commands.HANDSHAKE_REQ), new IEventHandler<MessageEvent>(){

			@Override
			public void handle(MessageEvent event) {
				System.out.println("HANDSHAKE_REQ recieved!!");
			}
			
		});
		
	}
	
}
