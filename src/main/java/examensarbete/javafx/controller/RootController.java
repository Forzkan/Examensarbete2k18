package examensarbete.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RootController {

	@SuppressWarnings("unused")
	private Stage primaryStage;
	
	@FXML
	private Button mainMenuBtn1, mainMenuBtn2, mainMenuBtn3, mainMenuBtn4, mainMenuBtn5;
	
	
	public RootController(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	private void initialize() {
		
	}
	
}
