package examensarbete.robert.test;

import java.awt.AWTException;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import examensarbete.model.action.BrowserAction;
import examensarbete.model.action.ClickAction;
import examensarbete.model.action.IAction;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.action.TextTypeAction;
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
	}
	
	@FXML
	public void initialize() {
		
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
	
	@FXML
	private void onScreenCropCaptureClicked(ActionEvent event){
		SnapImageAction snapAction;
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
