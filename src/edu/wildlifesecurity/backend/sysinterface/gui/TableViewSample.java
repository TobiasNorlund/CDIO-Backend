package edu.wildlifesecurity.backend.sysinterface.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.wildlifesecurity.backend.repository.impl.FileRepository;
import edu.wildlifesecurity.framework.repository.IRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
 
public class TableViewSample extends Application {
 
    public static final String Column1MapKey = "Option";
    public static final String Column2MapKey = "Value";
	IRepository repository = new FileRepository();
 
    public static void main(String[] args) {
    
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(300);
        stage.setHeight(500);
        
        final Label label = new Label("Student IDs");
        label.setFont(new Font("Arial", 20));
 
        TableColumn<Map, String> firstDataColumn = new TableColumn<>("Option");
        TableColumn<Map, String> secondDataColumn = new TableColumn<>("Value");
 
        firstDataColumn.setCellValueFactory(new MapValueFactory(Column1MapKey));
        firstDataColumn.setMinWidth(130);
        secondDataColumn.setCellValueFactory(new MapValueFactory(Column2MapKey));
        secondDataColumn.setMinWidth(130);
 
        TableView table_view = new TableView<>(generateDataInMap());
 
        table_view.setEditable(true);
        table_view.getSelectionModel().setCellSelectionEnabled(true);
        table_view.getColumns().setAll(firstDataColumn, secondDataColumn);
        Callback<TableColumn<Map, String>, TableCell<Map, String>>
            cellFactoryForMap = new Callback<TableColumn<Map, String>,
                TableCell<Map, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new TextFieldTableCell(new StringConverter() {
                            @Override
                            public String toString(Object t) {
                                return t.toString();
                            }
                            @Override
                            public Object fromString(String string) {
                                return string;
                            }                                    
                        });
                    }
        };
        firstDataColumn.setCellFactory(cellFactoryForMap);
        secondDataColumn.setCellFactory(cellFactoryForMap);
 
        final VBox vbox = new VBox();
 
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table_view);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
 
        stage.show();
    }
    
    private String valueToStringOrEmpty(Map<String, ?> map, String key) {
        Object value = map.get(key);
        return value == null ? "" : value.toString();
    }
 
    private ObservableList<Map> generateDataInMap() {
        int max = 30;
        ObservableList<Map> allData = FXCollections.observableArrayList();
        Map<String, Object> config=new HashMap<>();
        


        
        repository.loadConfiguration(config);
        
        for (Entry<String, Object> entry : config.entrySet()) {
            Map<String, String> dataRow = new HashMap<>();
        	if (entry.getValue()!=null){
        		dataRow.put(Column1MapKey, (String) entry.getKey());
        		dataRow.put(Column2MapKey, (String) entry.getValue());
        	}
        	else{
        		dataRow.put(Column1MapKey, null);
        		dataRow.put(Column2MapKey, null);
        	}
        	allData.add(dataRow);


        }
        return allData;
    }
}