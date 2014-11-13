package edu.wildlifesecurity.backend.sysinterface.gui.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import edu.wildlifesecurity.backend.sysinterface.gui.MainApp;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableCapture;

public class CaptureViewController {
	
    @FXML
    private TableView<ViewableCapture> captureTable;
    @FXML
    private TableColumn<ViewableCapture, String> captureTimeStampColumn;
    @FXML
    private TableColumn<ViewableCapture, String> captureIdColumn;

    @FXML
	private Label captureIdLabel;
	@FXML
	private Label timeStampLabel;
	@FXML
	private Label trapDeviceIdLabel;
	@FXML
	private Label positionLabel;
	@FXML
	private ImageView captureImage;
	
       // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public CaptureViewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {


    	captureTimeStampColumn.setCellValueFactory(cellData -> cellData.getValue().timeStampStringProperty());
    	
    	captureIdColumn.setCellValueFactory(cellData -> cellData.getValue().captureIdStringProperty());

    	showCaptureDetails(null);

        // Listen for selection changes and show the person details when changed.
        captureTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCaptureDetails(newValue));
        
        

    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    @FXML
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        captureTable.setItems(mainApp.getCaptureData());
    }
    
    private void showCaptureDetails(ViewableCapture capture) {
        if (capture != null) {
        	
        	captureIdLabel.setText(Integer.toString(capture.getCaptureId()));
            timeStampLabel.setText(capture.getTimeStampString());
        	trapDeviceIdLabel.setText(Integer.toString(capture.getTrapDeviceId()));
        	positionLabel.setText(capture.getPosition());
        	captureImage.setImage(new Image("file:captureImages/ogre52.png"));
        	

        } else {

        	captureIdLabel.setText("");
            timeStampLabel.setText("");
        	trapDeviceIdLabel.setText("");
        	positionLabel.setText("");

        }
    }
}