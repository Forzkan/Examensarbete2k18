package examensarbete.model.action;

import java.awt.AWTException;
import java.util.ArrayList;

import examensarbete.main.TTMain;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Screen;


public class ClickAction extends ActionBase{

	private int x, y;	

	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	
	public ClickAction() throws AWTException {
		super();
		this.actionType = EActionType.CLICK;
	}
	
	
	@Override
	public void actionSetup() {
		createScreenCover();
	}
	
	@Override
	public boolean performAction() {
		this.performClick(x, y);
		return true;
	}


	
	public void setCoordinates(double screenX, double screenY) {
		x = (int)screenX;
		y = (int)screenY;
	}

	@Override
	public EActionType getActionType() {
		return this.actionType;
	}

	



	// USED FOR SETTING UPP THE COORDINATES.
	private ArrayList<Popup> popups = new ArrayList<Popup>();
	private ArrayList<EventHandler<MouseEvent>> eventHandlers = new ArrayList<EventHandler<MouseEvent>>();
	private ArrayList<WeakEventHandler<MouseEvent>> weakEventHandlers = new ArrayList<WeakEventHandler<MouseEvent>>();
	
	
	private void createScreenCover(){

		for(Screen screen : getAllScreens())
		{
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
			popup.setOpacity(0.05);
			
			popup.show(TTMain.primaryStage.getScene().getWindow());
			
			popups.add(popup);
			setOnMouseMovedEvent(popup);
			setOnMouseClickedEvent(popup);
		}

	}//
	
	
	private void setOnMouseMovedEvent(Popup popup) {
		EventHandler<MouseEvent> event_handler;
		WeakEventHandler<MouseEvent> weak_event_handler;
		
		event_handler = (MouseEvent event) -> {
//			currentXCoordinateText.setText(event.getScreenX() + "");
//			currentYCoordinateText.setText(event.getScreenY() + "");
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		popup.getScene().setOnMouseMoved(weak_event_handler);
		
		eventHandlers.add(event_handler);
		weakEventHandlers.add(weak_event_handler);
	}
	
	private void setOnMouseClickedEvent(Popup popup) {
		EventHandler<MouseEvent> event_handler;
		WeakEventHandler<MouseEvent> weak_event_handler;
		
		event_handler = (MouseEvent event) -> {
			setCoordinates(event.getScreenX(), event.getScreenY());
			System.out.println(toString());
			clearPopups();
			performAction();
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		popup.getScene().setOnMouseClicked(weak_event_handler);
		
		eventHandlers.add(event_handler);
		weakEventHandlers.add(weak_event_handler);
	}
	
	private void clearPopups() {
		for (Popup p : popups) {
			p.hide();
		}
		popups = new ArrayList<Popup>();
		eventHandlers.clear();
		weakEventHandlers.clear();
	}
	
}
