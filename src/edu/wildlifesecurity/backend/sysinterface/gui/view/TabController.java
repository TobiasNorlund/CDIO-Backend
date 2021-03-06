package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import edu.wildlifesecurity.backend.sysinterface.gui.JavaFXGUI;
import edu.wildlifesecurity.backend.sysinterface.gui.view.CaptureViewController;
import edu.wildlifesecurity.backend.sysinterface.gui.view.LogViewController;

public class TabController {
	
	@FXML
	private Tab capturePane;
	@FXML
	private Tab logPane;
	
	@FXML
	private Tab tabDevicesPane;
	
	@FXML
	private Tab optionsPane;
	
	private JavaFXGUI javaFXGUI;

    public TabController() {
    }

   
    @FXML
    private void initialize() {

    }
    @FXML
    public void showCaptureView() {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(JavaFXGUI.class.getResource("view/CaptureView.fxml"));
            AnchorPane captureOverview = (AnchorPane) loader.load();    

            capturePane.setContent(captureOverview);

            
            CaptureViewController controller = loader.getController();
            controller.setMainApp(this.javaFXGUI);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void showLogView() {
        try {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JavaFXGUI.class.getResource("view/LogView.fxml"));
            AnchorPane logOverview = (AnchorPane) loader.load();
            
            logPane.setContent(logOverview);
            
            LogViewController controller = loader.getController();
            controller.setMainApp(this.javaFXGUI);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showTrapDevicesView() {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(JavaFXGUI.class.getResource("view/TrapDeviceView.fxml"));
            AnchorPane captureOverview = (AnchorPane) loader.load();    

            tabDevicesPane.setContent(captureOverview);

            
            TrapDeviceViewController controller = loader.getController();
            controller.setMainApp(this.javaFXGUI);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showOptionView() {
        try {

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(JavaFXGUI.class.getResource("view/OptionsView.fxml"));
            AnchorPane Overview = (AnchorPane) loader.load();    

            optionsPane.setContent(Overview);

            
            OptionsViewController controller = loader.getController();
            controller.setMainApp(this.javaFXGUI);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param javaFXGUI
     */
    
    public void setMainApp(JavaFXGUI javaFXGUI) {
        this.javaFXGUI = javaFXGUI;
    	showLogView();
    	showCaptureView();
    	showTrapDevicesView();
    	showOptionView();
     
    }
    
}