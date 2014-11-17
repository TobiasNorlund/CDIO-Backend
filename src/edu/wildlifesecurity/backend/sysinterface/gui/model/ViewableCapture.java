package edu.wildlifesecurity.backend.sysinterface.gui.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import org.opencv.core.Mat;

import edu.wildlifesecurity.framework.tracking.Capture;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ViewableCapture {
	
	private IntegerProperty captureId;
	
	private StringProperty captureIdString;

	private ObjectProperty<LocalDate> timeStamp;
	
	private StringProperty timeStampString;
	
	private IntegerProperty trapDeviceId;
	
	private StringProperty position;
	
	private ObjectProperty<Mat> image;
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Capture rawCapture;
    /**
     * Default constructor.
     */
    public ViewableCapture() {
        this(null, null,null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param firstName
     * @param lastName
     */
    public ViewableCapture(Integer id, LocalDate time, Integer trapId, String position, Mat img, String path) {
    	this.captureId= new SimpleIntegerProperty(id);
    	this.timeStamp= new SimpleObjectProperty<LocalDate>(time);
    	this.trapDeviceId = new SimpleIntegerProperty(trapId);
    	this.position= new SimpleStringProperty(position);
    	this.image = new SimpleObjectProperty<Mat>(img);
    	this.timeStampString = new SimpleStringProperty(df.format(time));
    	this.captureIdString = new SimpleStringProperty(Integer.toString(id));
    }
    
    public ViewableCapture(Capture cap) {
    	this.captureId= new SimpleIntegerProperty(cap.captureId);
    	this.timeStamp= new SimpleObjectProperty<LocalDate>(cap.timeStamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    	this.trapDeviceId = new SimpleIntegerProperty(cap.trapDeviceId);
    	this.position= new SimpleStringProperty(cap.position);
    	this.image = new SimpleObjectProperty<Mat>(cap.image);
    	this.timeStampString = new SimpleStringProperty(df.format(cap.timeStamp));
    	this.captureIdString = new SimpleStringProperty(Integer.toString(cap.captureId));
    	this.rawCapture=cap;

    }
    
    //Position
    public String getPosition() {
        return position.get();
    }

    public void setPosition(String firstName) {
        this.position.set(firstName);
    }

    public StringProperty positionProperty() {
        return position;
    }
    
    //CaptureID
    public Integer getCaptureId() {
        return captureId.get();
    }

    public void setCaptureId(Integer id) {
        this.captureId.set(id);
    }

    public IntegerProperty captureIdProperty() {
        return captureId;
    }
    //CaptureIDSTring
    public String getCaptureIdString() {
        return captureIdString.get();
    }

    public void setCaptureIdString(String id) {
        this.timeStampString.set(id);
    }

    public StringProperty captureIdStringProperty() {
        return captureIdString;
    }
    
    //TrapDeviceID
    public Integer getTrapDeviceId() {
        return trapDeviceId.get();
    }

    public void setTrapDeviceId(Integer id) {
        this.trapDeviceId.set(id);
    }

    public IntegerProperty trapDeviceIdProperty() {
        return trapDeviceId;
    }

    
    //TimeStamp
    public LocalDate getTimeStamp() {
        return timeStamp.get();
    }

    public void setTimeStamp(LocalDate time) {
        this.timeStamp.set(time);
    }

    public ObjectProperty<LocalDate> timeStampProperty() {
        return timeStamp;
    }
    
    //TimeStampString
    public String getTimeStampString() {
        return timeStampString.get();
    }

    public void setTimeStampString(String timeString) {
        this.timeStampString.set(timeString);
    }

    public StringProperty timeStampStringProperty() {
        return timeStampString;
    }
    
    //Image
    public Mat getImage() {
        return image.get();
    }

    public void setImage(Mat img) {
        this.image.set(img);
    }

    public ObjectProperty<Mat> imageProperty() {
        return image;
    }
    
    //Image
    public Capture getRawCapture() {
        return this.rawCapture;
    }

    
    }