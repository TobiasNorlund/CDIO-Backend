package edu.wildlifesecurity.backend.sysinterface.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import edu.wildlifesecurity.backend.ISystemInterface;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableCapture;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableTrapDevice;
import edu.wildlifesecurity.backend.sysinterface.gui.view.TabController;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.LogEvent;
import edu.wildlifesecurity.framework.actuator.IActuator;
import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;
import edu.wildlifesecurity.framework.repository.IRepository;
import edu.wildlifesecurity.framework.tracking.Capture;

public class MainApp extends Application implements ISystemInterface {

	static public IRepository repository;
	static public ICommunicatorServer communicator;
    private Stage primaryStage;
    public BorderPane rootLayout;
    private Thread guiThread;
    
    
    
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<ViewableCapture> captureData = FXCollections.observableArrayList();
    private ObservableList<ViewableTrapDevice> trapDeviceData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data

    }

    /**
     * Returns the data as an observable list of Persons. 
     * @return
     */
    
    public ObservableList<ViewableCapture> getCaptureData() {
    	 List<Capture> captures=new ArrayList<Capture>();
         try{
         	captures=MainApp.repository.getCaptureDefinitions();
         }catch(Exception e){
         	System.out.println("no captures found");
         }
         for (Capture c: captures)
         {
         	captureData.add(new ViewableCapture(c));
         }
        return captureData;
    }
    
    public ObservableList<ViewableTrapDevice> getTrapDeviceData() {
    	List<TrapDevice> trapDevices=new ArrayList<TrapDevice>(); 
        try{
     	   trapDevices=MainApp.communicator.getConnectedTrapDevices();
        }catch(Exception e){
     	   System.out.println("no trapDevices found");
        }
         for (TrapDevice t:trapDevices)
         {
         	trapDeviceData.add(new ViewableTrapDevice(t));
         }
        return trapDeviceData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Capture View");
        
        initRootLayout();
        showTabView();
        

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

   
    
    public void showTabView() {
        try {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Tabbed.fxml"));
            TabPane logOverview = (TabPane) loader.load();
            
            rootLayout.setCenter(logOverview);
            
            TabController controller = loader.getController();
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

	@Override
	public void link(IRepository repository, ICommunicatorServer communicator, IActuator actuator) {
		// TODO Auto-generated method stub

		MainApp.repository=repository;
		MainApp.communicator=communicator;

		guiThread = new Thread(new Runnable(){

				

				@Override
				public void run() {
					launch(MainApp.class);
					
				}
			
		});
		
		guiThread.start();
        
        

        
	}
}