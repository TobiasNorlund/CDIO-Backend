package edu.wildlifesecurity.backend.repository.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import edu.wildlifesecurity.framework.AbstractComponent;
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
	
	
	@Override
	public void init(){
		// TODO: Open log writer
		
	}
	
	@Override
	public void loadConfiguration(Map<String, Object> configuration){
		try{
	         FileInputStream fis = new FileInputStream("configuration.xml");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         this.configuration = (Map<String, Object>) ois.readObject();
	         configuration = this.configuration;
	         ois.close();
	         fis.close();
	     }catch(IOException | ClassNotFoundException ioe){
	         ioe.printStackTrace();
	         error("Error in FileRepository. Could not load configuration file:\n" + ioe.getMessage()); 
	         return;
	     }
	}

	@Override
	public ISubscription addEventHandler(EventType type, IEventHandler<LogEvent> handler) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO: Use log writer to store the log message
		// TODO: Dispatch log event
	}

	@Override
	public void dispose() {

		// TODO: Dispose log writer
		
	}

}
