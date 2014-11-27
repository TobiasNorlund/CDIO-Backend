package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.time.ZoneId;
import java.util.Date;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import edu.wildlifesecurity.backend.sysinterface.gui.JavaFXGUI;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.LogEvent;

public class LogViewController {
	
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;
	
    private JavaFXGUI javaFXGUI;
    
    private String log="";
    
    @FXML
    private TextArea logView;
    
    private Date logStart;
    
    private Date logEnd;
    
    private boolean forced=false;

    public LogViewController() {
    }

   
    @FXML
    private void initialize() {
    	logEnd=new Date();
    	logStart=new Date(0);
		JavaFXGUI.repository.addEventHandler(LogEvent.INFO, new IEventHandler<LogEvent>(){
		
		        	
					@Override
					public void handle(LogEvent event) {
						Platform.runLater(new Runnable() {
						    @Override
						    public void run() {
						    	if (!forced)
						    	{
							    	System.out.println("List of log entries updated!");
							    	logEnd=new Date();
							    	reloadLog();
						    	}
						    }
						});
				
				
				
			}
        	
        });
    }
    
    @FXML
    void reloadLog(){
    	logView.clear();
    	try{
    		log=JavaFXGUI.repository.getLog(logStart, logEnd);
    	}catch(Exception e){
    		
    	}
    	
    	if (log!=null)
    	{
    		logView.setText(log);
    	}
    	logView.setScrollTop(Double.MAX_VALUE);
    	
    	
    }
    
    @FXML
    void handleStartChange(){
    	forced=true;
    	logStart = Date.from(startDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	reloadLog();
    	
    }
    
    @FXML
    void handleEndChange(){
    	forced=true;
    	logEnd = Date.from(endDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	reloadLog();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param javaFXGUI
     */
    
    public void setMainApp(JavaFXGUI javaFXGUI) {
        this.javaFXGUI = javaFXGUI;
    	

     
    }
    
}