package edu.wildlifesecurity.backend.sysinterface.gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ViewableOption {
	
	
	private StringProperty option;
	
	private StringProperty value;
	
	private StringProperty description;
	
    /**
     * Default constructor.
     */
    public ViewableOption() {
        this(null, null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param firstName
     * @param lastName
     */
    public ViewableOption(String option, String value, String description) {
    
    	this.option = new SimpleStringProperty(option);
    	this.value= new SimpleStringProperty(value);
    	this.description= new SimpleStringProperty(description);
    }
    
    public ViewableOption(String option, String value) {
        
    	this.option = new SimpleStringProperty(option);
    	this.value= new SimpleStringProperty(value);
    	this.description= new SimpleStringProperty("No Desciption");
    }
    
    
    //Option
    public String getOption() {
        return this.option.get();
    }

    public void setOption(String option) {
        this.option.set(option);
    }

    public StringProperty optionProperty() {
        return this.option;
    }
    
    //Value
    public String getValue() {
        return this.value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public StringProperty valueProperty() {
        return value;
    }
    
    //Description
    public String getDescription() {
        return this.description.get();
    }

    public void setDiscription(String description) {
        this.description.set(description);
    }

    public StringProperty desciptionProperty() {
        return description;
    }
    
    }