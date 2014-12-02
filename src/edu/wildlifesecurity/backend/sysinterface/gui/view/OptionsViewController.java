package edu.wildlifesecurity.backend.sysinterface.gui.view;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import edu.wildlifesecurity.backend.sysinterface.gui.JavaFXGUI;
import edu.wildlifesecurity.backend.sysinterface.gui.model.ViewableOption;
import edu.wildlifesecurity.framework.EventDispatcher;
import edu.wildlifesecurity.framework.EventType;
import edu.wildlifesecurity.framework.Message;
import edu.wildlifesecurity.framework.MessageEvent;
import edu.wildlifesecurity.framework.Message.Commands;
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
 

	
       // Reference to the main application.
    private static JavaFXGUI javaFXGUI;

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
    @FXML
    private void initialize() {

    	showDesciption(null);
    	optionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDesciption(newValue)); 
        valueColumn.setOnEditCommit(new EventHandler<CellEditEvent<ViewableOption, String>>() {
            @Override
            public void handle(CellEditEvent<ViewableOption, String> t) {
                ((ViewableOption) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
                
                //JavaFXGUI.repository.setConfigOption(((ViewableOption) t.getTableView().getItems().get(t.getTablePosition().getRow())).getOption(),t.getNewValue());
                String message="SET_CONFIG,"+((ViewableOption) t.getTableView().getItems().get(t.getTablePosition().getRow())).getOption()+","+t.getNewValue();
                System.out.println(message);
               
                List<TrapDevice> trapDevices=new ArrayList<TrapDevice>(); 
                try{
             	   trapDevices=JavaFXGUI.communicator.getConnectedTrapDevices();
                }catch(Exception e){
             	   System.out.println("no trapDevices found");
                }
                for (TrapDevice trap:trapDevices){
                	JavaFXGUI.communicator.sendMessage(new Message(trap.id,message));
                }
            }
        });
    	

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
    



    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param javaFXGUI
     */
    
    public void setMainApp(JavaFXGUI javaFXGUI) {
        OptionsViewController.javaFXGUI = javaFXGUI;
        optionTable.setItems(javaFXGUI.getOptionData());
        optionsColumn.setCellValueFactory(cellData -> cellData.getValue().optionProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        valueColumn.setCellFactory(TextFieldTableCell.<ViewableOption>forTableColumn());



    }
    
    
    
}