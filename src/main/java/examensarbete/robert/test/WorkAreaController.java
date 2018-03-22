package examensarbete.robert.test;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import examensarbete.model.action.BrowserAction;
import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.action.ClickAction;
import examensarbete.model.action.IAction;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.action.TextTypeAction;
import examensarbete2k18.model.properties.PropertiesHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//import java.awt.Desktop;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import examensarbete.model.action.AutomaticImageSnapAction;
//import javafx.collections.ObservableList;
//import javafx.event.EventHandler;
//import javafx.event.WeakEventHandler;
//import javafx.geometry.Rectangle2D;
//import javafx.scene.Scene;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Popup;
//import javafx.stage.Screen;

	

public class WorkAreaController {


	@FXML
	private AnchorPane headerPane, toolBarPane, workAreaPane;
	@FXML
	private Button playButton, openBrowserButton, mouseClickAction, snipImageButton;
	@FXML
	private GridPane workAreaGridPane;
	@FXML
	private Text currentXCoordinateText, currentYCoordinateText;
	
	
	private Stage stage;
	private ArrayList<IAction> listOfActions = new ArrayList<IAction>();

	
	public WorkAreaController(Stage stage) {
		this.stage = stage;
		PropertiesHandler.InitializePropertiesHandler();
	}
	
	@FXML
	public void initialize() {
		
	}
	
	
	@FXML
	private void openPreferencesWindow() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(""));
			Object preferenceController = new Object();
			fxmlLoader.setController(preferenceController); // TODO:: Proper controller.
			Parent parent = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Preferences");
			stage.setScene(new Scene(parent));
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

	@FXML
	private void onBrowserButtonClick() {
		BrowserAction browserAction;
		try {
			browserAction = new BrowserAction();
			browserAction.actionSetup();
			listOfActions.add(browserAction);
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	private void onClickButtonPressed() {
		ClickAction clickAction;
		try {
			clickAction = new ClickAction(stage, 0,0);
			clickAction.actionSetup();
			listOfActions.add(clickAction);
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}
	
	SnapImageAction snapAction;
	@FXML
	private void onScreenCropCaptureClicked(ActionEvent event){
		
		try {
			snapAction = new SnapImageAction(stage);
			snapAction.actionSetup();
			listOfActions.add(snapAction);
			
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	private void onTextTypeButtonPressed() {
		TextTypeAction typeAction;
		try {
			typeAction = new TextTypeAction();
			typeAction.actionSetup();
			listOfActions.add(typeAction);
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@FXML
	private void snapImageFromBounds() {
		try {
			ChromeWebAction cwa = new ChromeWebAction("http://www.google.com");
			cwa.actionSetup();
			cwa.performAction();
			System.out.println(cwa.takeBrowserScreenshot("aUniqueNameForBrowserScreenshot"));
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
//		AutomaticImageSnap autoSnap;
//		try {
//			autoSnap = new AutomaticImageSnap(snapAction.getRectangleBounds());
//			autoSnap.actionSetup();
//		} catch (AWTException e) {
//			System.out.println(e.getMessage());
//		}
	}
	 
//	private EventHandler<MouseEvent> eHandler_MouseMovement;
//	private WeakEventHandler<MouseEvent> weakEventHandler_MouseMovement;
//	
//	private EventHandler<MouseEvent> eHandler_MouseSnippMovement;
//	private WeakEventHandler<MouseEvent> weakEventHandler_MouseSnippMovement;
	
	
//	public void activateCursorPositionListener() {
//		eHandler_MouseMovement = (MouseEvent event) -> {
//			currentXCoordinateText.setText(event.getScreenX() + "");
//			currentYCoordinateText.setText(event.getScreenY() + "");
//		};
//		weakEventHandler_MouseMovement = new WeakEventHandler<>(eHandler_MouseMovement);
//		stage.getScene().setOnMouseMoved(weakEventHandler_MouseMovement);
//	}
	
	
//	private Rectangle createScreenSnipRectangle(ActionEvent event) {
//		Rectangle rect = new Rectangle();
//		
////		rect.setX(event.getScreenX());
////		rect.setY(event.getScreenY());
//		rect.setWidth(100);
//		rect.setHeight(100);
//		// Initialize the snap animation.
//		Popup snippPopup = new Popup();
//		
//		double x = 100;
//		double y = 100;
//
//		rect.setFill(Color.WHITE);
//		rect.setStroke(Color.BLACK);
//		snippPopup.setWidth(100);
//		snippPopup.setHeight(100);
//		snippPopup.getContent().add(rect);
//		snippPopup.setX(x);
//		snippPopup.setY(y);
////		snippPopup.setOpacity(0.3);
//		
//		snippPopup.show(stage.getScene().getWindow());
//		
//		//getDefaultSnapPopup(snapPosition, event).show(rootNode.getScene().getWindow());
//		//stage.setAlwaysOnTop(true);
//		eHandler_MouseSnippMovement = (MouseEvent m_event) -> {
//			snippPopup.setX(m_event.getScreenX());
//			snippPopup.setY(m_event.getScreenY());
//		};
//		weakEventHandler_MouseSnippMovement = new WeakEventHandler<>(eHandler_MouseSnippMovement);
//		rect.setOnMouseMoved(weakEventHandler_MouseSnippMovement);
//		
//		
//		return rect;
//	}
	
	
	

	
	
	
	
	
}
