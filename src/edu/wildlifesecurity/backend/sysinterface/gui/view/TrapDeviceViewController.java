package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import edu.wildlifesecurity.backend.sysinterface.gui.MainApp;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableCapture;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableTrapDevice;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.LogEvent;
import edu.wildlifesecurity.framework.communicatorserver.ConnectEvent;

public class TrapDeviceViewController {
	
    @FXML
    private TableView<ViewableTrapDevice> deviceTable;
    @FXML
    private TableColumn<ViewableTrapDevice, String> deviceIdColumn;
    @FXML
    private Label positionLabel;
    @FXML
    private Label idLabel;

 
	
       // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TrapDeviceViewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {


     	

    	showDeviceDetails(null);

        // Listen for selection changes and show the person details when changed.
    	deviceTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDeviceDetails(newValue));
    	
    	MainApp.communicator.addConnectEventHandler(ConnectEvent.NEW_TRAPDEVICE, new IEventHandler<ConnectEvent>(){
    		

			@Override
			public void handle(ConnectEvent event) {
				Platform.runLater(new Runnable() {
				    @Override
				    public void run() {
				    	reloadList();
				    	System.out.println("List of TrapDevices updated");
				    }
				});
				
			}
	
});
        
        

    }
    
    private void showDeviceDetails(ViewableTrapDevice trapDevice) {
        
    	if (trapDevice != null) {
        positionLabel.setText(trapDevice.getPosition());
        idLabel.setText(trapDevice.getTrapDeviceId());

        } else {
            positionLabel.setText("");
            idLabel.setText("");
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        reloadList();
        
    }
    
    private void reloadList(){
    	System.out.println(mainApp.getTrapDeviceData());
    	deviceTable.getItems().clear();
    	deviceTable.setItems(mainApp.getTrapDeviceData());
    	deviceIdColumn.setCellValueFactory(cellData -> cellData.getValue().trapDeviceIdProperty());

    }

    

}