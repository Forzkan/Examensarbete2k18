package examensarbete.main;


import examensarbete.javafx.controller.WorkAreaController;
//import examensarbete.javafx.controller.WorkAreaController;
import examensarbete.javafx.stage.StageFactory;
import examensarbete.javafx.stage.TTStage;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Screen;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.stage.Stage;

public class TTMain extends Application{
	
	@SuppressWarnings("unused")
	private final String rootFXML = "robert_test/ApplicationRoot.fxml";
	
//	private final String workareaFXML = "robert_test/ActionWorkArea.fxml";
	
	public static Stage primaryStage;
	StageFactory stageHandler = new StageFactory();
	
	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) throws Exception {
		
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(rootFXML));
//			loader.setController(new TestController(primaryStage));
			
			
//			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(workareaFXML));
//			WorkAreaController waController = new WorkAreaController();
//			loader.setController(waController);
//			
//			Parent parent = loader.load();
//			Scene scene = new Scene(parent);
//			primaryStage.setTitle("Robert Test 2k18");
//			primaryStage.setScene(scene);
//			primaryStage.show();
		WorkAreaController waController = new WorkAreaController();
		this.primaryStage = stageHandler.openStage(TTStage.MAIN, waController);
		
		Screen screen = Screen.getPrimary();
		if(screen.getBounds().getWidth() <= 1920 || screen.getBounds().getHeight() <= 1080) {
			this.primaryStage.setMaximized(true);
		}
		
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/gui_images/beetle_16.png")));
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/gui_images/beetle_24.png")));
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/gui_images/beetle_32.png")));
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/gui_images/beetle_64.png")));
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/gui_images/beetle_128.png")));
		this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/gui_images/beetle_256.png")));

//		Image image = new Image("/icons/beetle.png");
//		primaryStage.getIcons().add(image);

//		primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("/src/main/resources/gui_images/beetle.png"))); //));
//		
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	
	
}
