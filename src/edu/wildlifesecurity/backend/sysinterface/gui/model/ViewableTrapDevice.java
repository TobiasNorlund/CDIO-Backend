package edu.wildlifesecurity.backend.sysinterface.gui.model;

import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;
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