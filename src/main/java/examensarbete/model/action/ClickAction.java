package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClickAction extends ActionRobotBase implements IAction{

	private int x, y;	
	private Stage stage;
	
	private ArrayList<Popup> popups = new ArrayList<Popup>();
	private ArrayList<EventHandler<MouseEvent>> eventHandlers = new ArrayList<EventHandler<MouseEvent>>();
	private ArrayList<WeakEventHandler<MouseEvent>> weakEventHandlers = new ArrayList<WeakEventHandler<MouseEvent>>();
	
	
	
	public ClickAction(Stage stage, int x, int y) throws AWTException {
		super(EActionType.CLICK);
		this.stage = stage;
		this.x = x;
		this.y = y;
	}
	
	
	public boolean performAction() {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		
		return true;
	}


	public void setCoordinates(double screenX, double screenY) {
		x = (int)screenX;
		y = (int)screenY;
	}

	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("ACTION: " + actionType.name().toString());
		sb.append("\n--------\n");
		sb.append("X : " + x);
		sb.append("\nY : " + y);
		
		return sb.toString();
	}


	@Override
	public void actionSetup() {
		createScreenCover();
	}
	

	public void createScreenCover(){

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
			
			popup.show(stage.getScene().getWindow());
			
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
