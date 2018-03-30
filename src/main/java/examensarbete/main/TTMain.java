package examensarbete.main;


import examensarbete.javafx.controller.WorkAreaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TTMain extends Application{
	
	@SuppressWarnings("unused")
	private final String rootFXML = "robert_test/ApplicationRoot.fxml";
	
	private final String workareaFXML = "robert_test/ActionWorkArea.fxml";
	
	public static Stage primaryStage;
	
	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootFXML));
//			loader.setController(new TestController(primaryStage));
			
			
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(workareaFXML));
			WorkAreaController waController = new WorkAreaController();
			loader.setController(waController);
			
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			primaryStage.setTitle("Robert Test 2k18");
			primaryStage.setScene(scene);
			primaryStage.show();
			this.primaryStage = primaryStage;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}