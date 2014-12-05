package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import edu.wildlifesecurity.backend.sysinterface.gui.JavaFXGUI;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableOption;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableTrapDevice;
import edu.wildlifesecurity.framework.EventDispatcher;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.Message.Commands;
import edu.wildlifesecurity.framework.communicatorserver.ConnectEvent;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;

public class OptionsViewController {
	
    @FXML
    private TableView<ViewableOption> optionTable;
    @FXML
    private TableColumn<ViewableOption, String> optionsColumn;
    @FXML
    public TableColumn<ViewableOption, String> valueColumn;
    
    @FXML
    private Label descriptionBox;
    
    @FXML
    private ComboBox trapChooser;
    
    private ObservableList<ViewableTrapDevice> trapDeviceData = FXCollections.observableArrayList();

	
       // Reference to the main application.
    private static JavaFXGUI javaFXGUI;
    
    private Integer currentTrap=null;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public OptionsViewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @SuppressWarnings("unchecked")
	@FXML
    private void initialize() {

    	
    	JavaFXGUI.communicator.addConnectEventHandler(ConnectEvent.NEW_TRAPDEVICE, new IEventHandler<ConnectEvent>(){
    		

			@Override
			public void handle(ConnectEvent event) {
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
				    	reloadTrapDevices();
				    }
				});
				
			}
	
    	});
    	
    	
    	showDesciption(null);
    	optionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDesciption(newValue)); 
    	
        trapChooser.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String oldValue, String newValue) {
            	ObservableList<Integer> lest = trapChooser.getItems();
                

            	try{
                	currentTrap=Integer.parseInt(newValue);

            	}
            	catch (java.lang.NumberFormatException e){
            		currentTrap=null;
            	}
            	if(lest.contains(currentTrap)){
            		System.out.println("TRUE");
            	}
            	loadConfig(currentTrap);
            	}    
        });
        

    	trapChooser.setPromptText("Choose Trap Device");
    	trapChooser.setEditable(true);    
        valueColumn.setOnEditCommit(new EventHandler<CellEditEvent<ViewableOption, String>>() {
            @Override
            public void handle(CellEditEvent<ViewableOption, String> t) {
                ((ViewableOption) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
                
                //JavaFXGUI.repository.setConfigOption(((ViewableOption) t.getTableView().getItems().get(t.getTablePosition().getRow())).getOption(),t.getNewValue());
                String message="SET_CONFIG,"+((ViewableOption) t.getTableView().getItems().get(t.getTablePosition().getRow())).getOption()+","+t.getNewValue();
                System.out.println("SENT message to trapDevice %s:".format(currentTrap.toString())+ message);
            	JavaFXGUI.communicator.sendMessage(new Message(currentTrap ,message));
               
                
            }
        });
        
        
        
    	

    }
    
    private void loadConfig(Integer trapDevice) {
    	if (trapDevice!=null){
    	  	  optionTable.setItems(javaFXGUI.getOptionData());
    	      optionsColumn.setCellValueFactory(cellData -> cellData.getValue().optionProperty());
    	      valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
    	      valueColumn.setCellFactory(TextFieldTableCell.<ViewableOption>forTableColumn());
    	}
    	else{
    		optionTable.getItems().clear();
    	}

  }
	
    @FXML
    private void reloadTrapDevices(){
    	
        try{
        	trapDeviceData=javaFXGUI.getTrapDeviceData();
        }catch(Exception e){
     	   System.out.println("no trapDevices found");
        }
    	trapChooser.getItems().clear();
        for (ViewableTrapDevice trap:trapDeviceData){
        	trapChooser.getItems().add(trap.getTrapDeviceId());
        }
         
        
    }
    

    
    
    private void showDesciption(ViewableOption option) {
    	Platform.runLater(new Runnable() {
    		   @Override
    		   public void run() {
    			   if (option != null) {
    		        	
    				   descriptionBox.setText(option.getDescription());
    	

    		        } else {

    		        	descriptionBox.setText(" ");
    		        	
    		        }
    		   }
    		});
    	
       
    }
    
    public void setConfigOption(Integer trapDevice, CellEditEvent<ViewableOption, String> event){
    	
    }
    



    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param javaFXGUI
     */
    
    public void setMainApp(JavaFXGUI javaFXGUI) {
        OptionsViewController.javaFXGUI = javaFXGUI;
      



    }
    
    
    
}