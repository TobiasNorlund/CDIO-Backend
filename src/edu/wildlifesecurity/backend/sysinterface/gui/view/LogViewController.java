package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import edu.wildlifesecurity.backend.sysinterface.gui.MainApp;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableCapture;

public class LogViewController {
	
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;
	
    private MainApp mainApp;
    
    private String log="";
    
    @FXML
    private TextArea logView;
    
    private Date logStart;
    
    private Date logEnd;

    public LogViewController() {
    }

   
    @FXML
    private void initialize() {
    	logEnd=new Date();
    	logStart=new Date(0);
    }
    
    @FXML
    void reloadLog(){
    	try{
    		log=mainApp.repository.getLog(logStart, logEnd);
    	}catch(Exception e){
    		
    	}
    	
    	if (log!=null)
    	{
    		logView.setText(log);
    	}
    	
    }
    
    @FXML
    void handleStartChange(){
    	logStart = Date.from(startDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	reloadLog();
    }
    
    @FXML
    void handleEndChange(){
    	logEnd = Date.from(endDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    	reloadLog();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    	reloadLog();

     
    }
    
}