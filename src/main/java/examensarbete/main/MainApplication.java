package examensarbete.main;

import examensarbete.javafx.controller.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApplication extends Application{

	private final String rootFXML = "fxml/ApplicationRoot.fxml";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootFXML));
			loader.setController(new RootController(primaryStage));
			Parent parent = loader.load();
			
			Scene scene = new Scene(parent);
			primaryStage.setTitle("DegreeProject 2k18");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}//
