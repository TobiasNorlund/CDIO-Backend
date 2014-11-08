
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.thoughtworks.xstream.XStream;

import edu.wildlifesecurity.backend.repository.impl.FileRepository;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.LogEvent;
import edu.wildlifesecurity.framework.tracking.Capture;


public class FileRepositoryTest {

	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
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

		Capture c = new Capture();
		c.captureId = 2;
		c.trapDeviceId = 2;
		c.position = "Hemma";
		c.timeStamp = new Date();
		c.image = new Mat(1,2,3);
		
		fr.storeCapture(c);
		
		fr.dispose();
	}
}
