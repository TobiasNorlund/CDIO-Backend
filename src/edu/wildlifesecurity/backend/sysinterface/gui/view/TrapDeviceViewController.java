package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.io.ByteArrayInputStream;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import edu.wildlifesecurity.backend.sysinterface.gui.MainApp;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableCapture;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableTrapDevice;

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


     	
    	deviceIdColumn.setCellValueFactory(cellData -> cellData.getValue().trapDeviceIdProperty());

    	showDeviceDetails(null);

        // Listen for selection changes and show the person details when changed.
    	deviceTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDeviceDetails(newValue));
        
        

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

        deviceTable.setItems(mainApp.getTrapDeviceData());
    }

    

}