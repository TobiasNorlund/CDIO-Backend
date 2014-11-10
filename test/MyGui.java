import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import edu.wildlifesecurity.backend.repository.impl.FileRepository;
import edu.wildlifesecurity.backend.ISystemInterface; 
import edu.wildlifesecurity.framework.repository.IRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
 
public class MyGui extends Application implements ISystemInterface {
	
	public Date startDate=new Date();
	public Date endDate=new Date();
	TextFlow textFlow=null;
	Text log = new Text();
	
	
	@Override
	public void link(IRepository repository) {
		// TODO Auto-generated method stub
		
	}
		
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
		FileRepository fr = new FileRepository();
		fr.init();
        Parent root = FXMLLoader.load(getClass().getResource("MyTestGui.fxml"));
     
         Scene scene = new Scene(root, 1024, 768);
 		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		startDate=df.parse("2014-11-05 16:55:37");

         stage.setTitle("Log Viewer");
         stage.setScene(scene);
         textFlow = (TextFlow) scene.lookup("#log");
         
         DatePicker startDatePicker=(DatePicker) scene.lookup("#start_date");
         DatePicker endDatePicker=(DatePicker) scene.lookup("#end_date");
         
         textFlow.getChildren().addAll(log);
         stage.show();

         
         startDatePicker.setOnAction(event -> {
     	   LocalDate date = startDatePicker.getValue();
     	   startDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
           log.setText(fr.getLog(startDate,endDate));

     	});
         
         endDatePicker.setOnAction(event -> {
     	   LocalDate date = endDatePicker.getValue();
     	   endDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
           log.setText(fr.getLog(startDate,endDate));

     	});


     }
    


        

    }
    

    
    