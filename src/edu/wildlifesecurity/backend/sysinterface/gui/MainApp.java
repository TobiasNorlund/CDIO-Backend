package edu.wildlifesecurity.backend.sysinterface.gui;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.opencv.core.Mat;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import edu.wildlifesecurity.backend.repository.impl.FileRepository;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableCapture;
import edu.wildlifesecurity.backend.sysinterface.gui.view.CaptureViewController;
import edu.wildlifesecurity.framework.repository.IRepository;
import edu.wildlifesecurity.framework.tracking.Capture;

public class MainApp extends Application {

	private IRepository repository = new FileRepository();
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<ViewableCapture> captureData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data

    	List<Capture> captures=this.repository.getCaptureDefinitions();
        for (Capture c: captures)
        {
        	captureData.add(new ViewableCapture(c));
        }
    }

    /**
     * Returns the data as an observable list of Persons. 
     * @return
     */
    
    public ObservableList<ViewableCapture> getCaptureData() {
        return captureData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Capture View");

        initRootLayout();

        showCaptureView();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showCaptureView() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CaptureView.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
            
            // Give the controller access to the main app.
            CaptureViewController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

    public static void main(String[] args) {
        launch(args);
    }
}