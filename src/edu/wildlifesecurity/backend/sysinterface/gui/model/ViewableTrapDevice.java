package edu.wildlifesecurity.backend.sysinterface.gui.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import org.opencv.core.Mat;

import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;
import edu.wildlifesecurity.framework.tracking.Capture;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ViewableTrapDevice {
	
	
	private StringProperty trapDeviceId;
	
	private StringProperty position;
	
    /**
     * Default constructor.
     */
    public ViewableTrapDevice() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param firstName
     * @param lastName
     */
    public ViewableTrapDevice(Integer trapId, String position) {
    
    	this.trapDeviceId = new SimpleStringProperty(trapId.toString());
    	this.position= new SimpleStringProperty(position);
    }
    
    public ViewableTrapDevice(TrapDevice trapDevice) {
        
    	this.trapDeviceId = new SimpleStringProperty(trapDevice.id.toString());
    	this.position= new SimpleStringProperty(trapDevice.GPSPos);
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
    
    //TrapDeviceID
    public String getTrapDeviceId() {
        return trapDeviceId.get();
    }

    public void setTrapDeviceId(Integer id) {
        this.trapDeviceId.set(id.toString());
    }

    public StringProperty trapDeviceIdProperty() {
        return trapDeviceId;
    }
    
    }