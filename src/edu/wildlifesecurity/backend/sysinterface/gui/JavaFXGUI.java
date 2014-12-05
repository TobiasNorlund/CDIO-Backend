package edu.wildlifesecurity.backend.sysinterface.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableOption;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableTrapDevice;
import edu.wildlifesecurity.backend.sysinterface.gui.view.TabController;
import edu.wildlifesecurity.framework.IEventHandler;
import edu.wildlifesecurity.framework.LogEvent;
import edu.wildlifesecurity.framework.actuator.IActuator;
import edu.wildlifesecurity.framework.communicatorserver.ICommunicatorServer;
import edu.wildlifesecurity.framework.communicatorserver.TrapDevice;
import edu.wildlifesecurity.framework.repository.IRepository;
import edu.wildlifesecurity.framework.tracking.Capture;

public class JavaFXGUI extends Application implements ISystemInterface {

	static public IRepository repository;
	static public ICommunicatorServer communicator;
    private Stage primaryStage;
    public BorderPane rootLayout;
    private Thread guiThread;
    
    
    private ObservableList<ViewableCapture> captureData = FXCollections.observableArrayList();
    private ObservableList<ViewableTrapDevice> trapDeviceData = FXCollections.observableArrayList();
    private ObservableList<ViewableOption> optionData = FXCollections.observableArrayList();
    

    /**
     * Constructor
     */
    public JavaFXGUI() {
        // Add some sample data

    }

    
    public ObservableList<ViewableCapture> getCaptureData() {
    	 List<Capture> captures=new ArrayList<Capture>();
         try{
         	captures=JavaFXGUI.repository.getCaptureDefinitions();
         }catch(Exception e){
         	System.out.println("no captures found");
         }
         captureData.clear();
         for (Capture c: captures)
         {
         	captureData.add(new ViewableCapture(c));
         }
        return captureData;
    }
    
    public ObservableList<ViewableTrapDevice> getTrapDeviceData() {
    	List<TrapDevice> trapDevices=new ArrayList<TrapDevice>(); 
        try{
     	   trapDevices=JavaFXGUI.communicator.getConnectedTrapDevices();
        }catch(Exception e){
     	   System.out.println("no trapDevices found");
        }
        trapDeviceData.clear();
        trapDeviceData.add(new ViewableTrapDevice(14,"test"));
         for (TrapDevice t:trapDevices)
         {
         	trapDeviceData.add(new ViewableTrapDevice(t));
         }
        return trapDeviceData;
    }
    
    public ObservableList<ViewableOption> getOptionData() {
        Map<String, Object> config=new HashMap<>();
        
        repository.loadConfiguration(config);
        optionData.clear();
        for (Entry<String, Object> entry : config.entrySet()) {
        	if (entry.getValue()!=null){
        		optionData.add(new ViewableOption(entry.getKey(),entry.getValue().toString()));
        	}
        	else{
        		optionData.add(new ViewableOption(entry.getKey(),""));
        	}
          }
        return optionData;
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
            loader.setLocation(JavaFXGUI.class.getResource("view/RootLayout.fxml"));
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
            loader.setLocation(JavaFXGUI.class.getResource("view/Tabbed.fxml"));
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

	@Override
	public void link(IRepository repository, ICommunicatorServer communicator, IActuator actuator) {

		JavaFXGUI.repository=repository;
		JavaFXGUI.communicator=communicator;

		guiThread = new Thread(new Runnable(){

				

				@Override
				public void run() {
					launch(JavaFXGUI.class);
					
				}
			
		});
		
		guiThread.start();
        
        

        
	}
}