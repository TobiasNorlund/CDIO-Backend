package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.io.ByteArrayInputStream;

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
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.Message.Commands;
import edu.wildlifesecurity.framework.MessageEvent;

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
	private Label imagePath;
	@FXML
	private ImageView captureImage;

	
       // Reference to the main application.
    private static MainApp mainApp;

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

    	
    	showCaptureDetails(null);

        // Listen for selection changes and show the person details when changed.
        captureTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCaptureDetails(newValue));
        
        MainApp.communicator.addMessageEventHandler(MessageEvent.getEventType(Commands.NEW_CAPTURE), new IEventHandler<MessageEvent>(){

			@Override
			public void handle(MessageEvent event) {
				System.out.println("List of captures updated!");
				reloadList();
			}
			
		});

    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    
    public void setMainApp(MainApp mainApp) {
        CaptureViewController.mainApp = mainApp;
        reloadList();
    }
    
    private void reloadList(){
    	
    	Platform.runLater(new Runnable() {
    		   @Override
    		   public void run() {
    		    	captureTable.getItems().clear();
    		    	captureTable.setItems(mainApp.getCaptureData());

    		    	captureTimeStampColumn.setCellValueFactory(cellData -> cellData.getValue().timeStampStringProperty());
    		    	
    		    	captureIdColumn.setCellValueFactory(cellData -> cellData.getValue().captureIdStringProperty());
    		   }
    		});

    }
    
    public Image matToImage(Mat input) {
    	MatOfByte buf = new MatOfByte();    
    	Highgui.imencode(".png", input, buf);
    	return new Image(new ByteArrayInputStream(buf.toArray()));
    	}
    
    private void showCaptureDetails(ViewableCapture capture) {
//    	Platform.runLater(new Runnable() {
//    		   @Override
//    		   public void run() {
    			   if (capture != null) {
    		        	
    		        	captureIdLabel.setText(Integer.toString(capture.getCaptureId()));
    		            timeStampLabel.setText(capture.getTimeStampString());
    		        	trapDeviceIdLabel.setText(Integer.toString(capture.getTrapDeviceId()));
    		        	positionLabel.setText(capture.getPosition());
    		        	captureImage.setImage(matToImage(MainApp.repository.getCaptureImage(capture.getCaptureId())));
    		        	

    		        } else {

    		        	captureIdLabel.setText("");
    		            timeStampLabel.setText("");
    		        	trapDeviceIdLabel.setText("");
    		        	positionLabel.setText("");
    		        	imagePath.setText("");

    		        }
    		//   }
//    		});
//    	
       
    }
}