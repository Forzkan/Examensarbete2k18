package examensarbete.robert.test;

import java.awt.AWTException;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
	

public class WorkAreaController {


	@FXML
	private AnchorPane headerPane;

	@FXML
	private AnchorPane toolBarPane;

	@FXML
	private Button playButton, openBrowserButton, mouseClickAction, snipImageButton;

	@FXML
	private AnchorPane workAreaPane;

	@FXML
	private GridPane workAreaGridPane;
	
	@FXML
	private Text currentXCoordinateText, currentYCoordinateText;
	
	private Stage stage;
	private EventHandler<MouseEvent> eHandler_MouseMovement;
	private WeakEventHandler<MouseEvent> weakEventHandler_MouseMovement;
	
	
	public WorkAreaController(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private void onBrowerButtonClick() {
		if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    try {
				Desktop.getDesktop().browse(new URI("http://www.google.com"));
			} catch (IOException | URISyntaxException e) {
				System.out.println(e.getMessage());
				System.out.println("Could not open browser...");
			}
		}
	}
	
	@FXML
	private void onClickButtonPressed() {
		
		try { // tmporarily just clicking on a pre-set position.
			ClickAction clickAction = new ClickAction(3575, 710);// click on the Feeling Lucky Button if Google.com is open on the 3rd monitor.
			clickAction.performAction();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//1344, 330
	 
	
	public void activateCursorPositionListener() {
		eHandler_MouseMovement = (MouseEvent event) -> {
			currentXCoordinateText.setText(event.getScreenX() + "");
			currentYCoordinateText.setText(event.getScreenY() + "");
		};
		weakEventHandler_MouseMovement = new WeakEventHandler<>(eHandler_MouseMovement);
		stage.getScene().setOnMouseMoved(weakEventHandler_MouseMovement);
	}
	
	
}
