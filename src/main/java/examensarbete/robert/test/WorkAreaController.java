package examensarbete.robert.test;

import java.awt.AWTException;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
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
	
	private EventHandler<MouseEvent> eHandler_MouseSnippMovement;
	private WeakEventHandler<MouseEvent> weakEventHandler_MouseSnippMovement;
	
	
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
	
	@FXML
	private void onScreenCropCaptureClicked(ActionEvent event) {
//		try {
//			ClickAction ca = new ClickAction(1,1);
//			// Tmp solution, until an ImageTakerAction is created.
//			ca.takeScreenShot();
//			
//		} catch (AWTException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		createScreenSnipRectangle(event);
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
	
	
	private Rectangle createScreenSnipRectangle(ActionEvent event) {
		Rectangle rect = new Rectangle();
		
//		rect.setX(event.getScreenX());
//		rect.setY(event.getScreenY());
		rect.setWidth(100);
		rect.setHeight(100);
		// Initialize the snap animation.
		Popup snippPopup = new Popup();
		
		double x = 100;
		double y = 100;

		rect.setFill(Color.WHITE);
		rect.setStroke(Color.BLACK);
		snippPopup.setWidth(100);
		snippPopup.setHeight(100);
		snippPopup.getContent().add(rect);
		snippPopup.setX(x);
		snippPopup.setY(y);
//		snippPopup.setOpacity(0.3);
		
		snippPopup.show(stage.getScene().getWindow());
		
		//getDefaultSnapPopup(snapPosition, event).show(rootNode.getScene().getWindow());
		//stage.setAlwaysOnTop(true);
		eHandler_MouseSnippMovement = (MouseEvent m_event) -> {
			snippPopup.setX(m_event.getScreenX());
			snippPopup.setY(m_event.getScreenY());
		};
		weakEventHandler_MouseSnippMovement = new WeakEventHandler<>(eHandler_MouseSnippMovement);
		rect.setOnMouseMoved(weakEventHandler_MouseSnippMovement);
		
		
		return rect;
	}
	
	
	
	
	
	
	
	private ArrayList<Popup> popups = new ArrayList<Popup>();
	private ArrayList<EventHandler<MouseEvent>> eventHandlers = new ArrayList<EventHandler<MouseEvent>>();
	private ArrayList<WeakEventHandler<MouseEvent>> weakEventHandlers = new ArrayList<WeakEventHandler<MouseEvent>>();
	
	
	
	public ObservableList<Screen> getAllScreens(){
		return Screen.getScreens();
	}
	
	//just needs
	public void createScreenCover(){
//		System.out.println(event.getScreenX());
		for(Screen screen : getAllScreens())
		{
			
			// Create one popup and rectangle for each screen,
					// each rectangle should all get eventlisteners that updates the label displaying the position of the mouse coursor. 
			
			Rectangle rect = new Rectangle();
			rect.setWidth(screen.getVisualBounds().getWidth());
			rect.setHeight(screen.getVisualBounds().getHeight());
			rect.setFill(Color.WHITE);
			rect.setStroke(Color.BLACK);

			Popup popup = new Popup();
			popup.setWidth(screen.getVisualBounds().getWidth());
			popup.setHeight(screen.getVisualBounds().getHeight());
			popup.getContent().add(rect);
			popup.setX(screen.getVisualBounds().getMinX());
			popup.setY(screen.getVisualBounds().getMinY());
			popup.setOpacity(0.3);
			
			popup.show(stage.getScene().getWindow());
			
			popups.add(popup);
			EventHandler<MouseEvent> event_handler;
			WeakEventHandler<MouseEvent> weak_event_handler;
			
			event_handler = (MouseEvent event) -> {
				currentXCoordinateText.setText(event.getScreenX() + "");
				currentYCoordinateText.setText(event.getScreenY() + "");
			};
			weak_event_handler = new WeakEventHandler<>(event_handler);
			stage.getScene().setOnMouseMoved(weak_event_handler);
			
			eventHandlers.add(event_handler);
			weakEventHandlers.add(weak_event_handler);	
		}

	}
	
	
	
	
	
}
