
import java.util.HashMap;
import java.util.Map;

import edu.wildlifesecurity.backend.repository.impl.FileRepository;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.LogEvent;


public class FileRepositoryTest {

	public static void main(String[] args) {
		FileRepository fr = new FileRepository();
		fr.init();
		
		Map<String, Object> config = new HashMap<String, Object>();
		fr.loadConfiguration(config);
		System.out.println(config.toString());
		
		fr.addEventHandler(LogEvent.INFO, new IEventHandler<LogEvent>(){

			@Override
			public void handle(LogEvent event) {
				System.out.println("Nytt info-event: " + event.getMessage());
			}
			
		});
		
		fr.info("Första loggmeddelandet!!");
		fr.dispose();
	}
}
