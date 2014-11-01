package edu.wildlifesecurity.backend.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import edu.wildlifesecurity.framework.AbstractComponent;
import edu.wildlifesecurity.framework.EventDispatcher;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.ISubscription;
import edu.wildlifesecurity.framework.LogEvent;
import edu.wildlifesecurity.framework.repository.IRepository;

/**
 * Implements a repository which uses files to store configuration and logging
 * @author Tobias
 *
 */
public class FileRepository extends AbstractComponent implements IRepository {
	
	private Map<String, Object> configuration;
	private FileWriter logWriter;
	private EventDispatcher<LogEvent> logEventDispatcher = new EventDispatcher<LogEvent>();
	
	@Override
	public void init(){
		// Open log file
		try {
			logWriter = new FileWriter("system.log", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadConfiguration(Map<String, Object> configuration){
		try{
	         XStream magicApi = new XStream();
	         magicApi.registerConverter(new MapEntryConverter());
	         magicApi.alias("configuration", Map.class);
	         
	         this.configuration = configuration;
	         this.configuration.putAll((Map<String, Object>) magicApi.fromXML(
	        		 new String(Files.readAllBytes(Paths.get("configuration.xml")))));
	         configuration = this.configuration;
	     }catch(IOException ioe){
	         ioe.printStackTrace();
	         error("Error in FileRepository. Could not load configuration file:\n" + ioe.getMessage()); 
	     }
	}

	@Override
	public ISubscription addEventHandler(EventType type, IEventHandler<LogEvent> handler) {
		return logEventDispatcher.addEventHandler(type, handler);
	}

	@Override
	public void error(String msg) {
		log("ERROR", msg);
	}

	@Override
	public void info(String msg) {
		log("INFO", msg);
	}

	@Override
	public void warn(String msg) {
		log("WARNING", msg);
	}
	
	private void log(String prio, String msg){
		// Use log writer to store the log message
		try{
			String logEntry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\t" + prio + "\t" + msg + "\n";
			logWriter.append(logEntry);
			
			// Dispatch log event
			EventType type; 
			switch(prio){
			case "ERROR":
				type = LogEvent.ERROR;
				break;
			case "WARN":
				type = LogEvent.WARN;
				break;
			default:
				type = LogEvent.INFO;	
			}
			logEventDispatcher.dispatch(new LogEvent(type, logEntry));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void dispose() {

		// Dispose log writer
		try {
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class MapEntryConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                writer.setValue(entry.getValue().toString());
                writer.endNode();
            }

        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, String> map = new HashMap<String, String>();

            while(reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName(); // nodeName aka element's name
                String value = reader.getValue();
                map.put(key, value);

                reader.moveUp();
            }

            return map;
        }

    }

}
