
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date start=new Date();
		//Date end=new Date();
		try {
			start=df.parse("2014-11-05 16:55:37");
			//end=df.parse("2014-11-06 00:00:00");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String test=fr.getLog(start);
		System.out.println(test);
		fr.dispose();

	}
}
