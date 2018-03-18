package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Image;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SnapImageAction extends ActionRobotBase implements IAction {

	private java.awt.Rectangle snapImageRect = new java.awt.Rectangle();

	private Stage stage;
	
	public SnapImageAction(Stage stage) throws AWTException {
		super(EActionType.IMAGESNAP);
		this.stage = stage;
	}

	public void setCoordinates(int x, int y) {
		snapImageRect.setLocation(x, y);
	}

	public void setSize(int width, int height) {
		snapImageRect.setSize(width, height);
	}

	public void setSnapRectangle(int x, int y, int width, int height) {
		snapImageRect.setBounds(x, y, width, height);
	}

	
	
	@Override
	public boolean performAction() {
		try {
			takeScreenShot();
		} catch (AWTException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	public Image takeScreenShot() throws AWTException, IOException {
		Robot robot = new Robot();
		java.awt.Rectangle area = snapImageRect.getBounds();
		BufferedImage bufferedImage = robot.createScreenCapture(area);
		bufferedImage = robot.createScreenCapture(area);
		ImageIO.write(bufferedImage, "PNG", new File("C:\\_dev\\cropScreenShot.png"));

		return null;
	}

	public void setX(int x) {
		snapImageRect.x = x;
	}

	public void setY(int y) {
		snapImageRect.y = y;
	}


	public int getX() {
		return (int) snapImageRect.getX();
	}


	public int getY() {
		return (int) snapImageRect.getY();
	}

	public int getWidth() {
		return (int) snapImageRect.getWidth();
	}

	public int getHeight() {
		return (int) snapImageRect.getHeight();
	}

	public java.awt.Rectangle getRectangleBounds() {
		return snapImageRect.getBounds();
	}

	public void setWidth(int width) {
		snapImageRect.width = width;
	}

	
	public void setHeight(int height) {
		snapImageRect.height = height;
	}

	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("ACTION: " + actionType.name().toString());
		sb.append("\n--------\n");
		sb.append("X : " + snapImageRect.x);
		sb.append("\nY : " + snapImageRect.y);
		sb.append("\nWidth : " + snapImageRect.width);
		sb.append("\nHeight : " + snapImageRect.height);
			
		return sb.toString();
	}

	@Override
	public void actionSetup() {
		createScreenCoverForSnap();
	}


	
	private ArrayList<Popup> popups = new ArrayList<Popup>();
	private ArrayList<EventHandler<MouseEvent>> eventHandlers = new ArrayList<EventHandler<MouseEvent>>();
	private ArrayList<WeakEventHandler<MouseEvent>> weakEventHandlers = new ArrayList<WeakEventHandler<MouseEvent>>();
	
	
	
	public void createScreenCoverForSnap(){
//		System.out.println(event.getScreenX());
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
			popup.setOpacity(0.08);
			
			popup.show(stage.getScene().getWindow());
			
			popups.add(popup);
			setSnapClickAndReleasedEvents(popup);
		}

	}
	
	
	private void setSnapClickAndReleasedEvents(Popup popup) {
		snapClicked(popup);
		snapMoving(popup);
		snapReleased(popup);
	}
	
	
	
	private int startX = 0;
	private int startY = 0;
	private Rectangle snapRectangle;
	
	private void snapClicked(Popup popup) {
		EventHandler<MouseEvent> event_handler;
		WeakEventHandler<MouseEvent> weak_event_handler;
		
		event_handler = (MouseEvent event) -> {
			setCoordinates((int)event.getScreenX(), (int)event.getScreenY());
			System.out.println(toString());
			startX = (int)event.getScreenX();
			startY = (int)event.getScreenY();
			Rectangle rectangle = new Rectangle();
			rectangle.setX(event.getScreenX());
			rectangle.setY(event.getScreenY());
			rectangle.setWidth(1);
			rectangle.setHeight(1);
			rectangle.setStyle("-fx-stroke-width: 3;-fx-stroke: red;"); 
			popup.getContent().add(rectangle);
			snapRectangle = rectangle;
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		popup.getScene().setOnMousePressed(weak_event_handler);
	}
	
	private void snapMoving(Popup popup) {
		EventHandler<MouseEvent> event_handler;
		WeakEventHandler<MouseEvent> weak_event_handler;
		
		event_handler = (MouseEvent event) -> {
//			System.out.println("IS MOVING");
			if(startX > event.getScreenX()) {
				//meaning we ahve a negative value for the width.. then we should set the new X to be the startX (i.e set it in the action object).
				snapRectangle.setX((int)event.getScreenX());
				snapRectangle.setWidth( (int) (startX - event.getScreenX()));
				
			}else {
				snapRectangle.setWidth((int) (event.getScreenX() - startX));
			}
			if(startY > event.getScreenY()) {
				snapRectangle.setY((int)event.getScreenY());
				snapRectangle.setHeight((int) (startY - event.getScreenY()));
			}else {
				
				snapRectangle.setHeight((int)event.getScreenY() - startY);
			}
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		popup.getScene().setOnMouseDragged(weak_event_handler);
		
	}
	
	private void snapReleased(Popup popup) {
		EventHandler<MouseEvent> event_handler;
		WeakEventHandler<MouseEvent> weak_event_handler;
		
		event_handler = (MouseEvent event) -> {
			System.out.println("BEFORE SET - ON RELEASE");
			System.out.println(toString());
			System.out.println();
//			snapImageAction.setCoordinates((int)event.getScreenX(), (int)event.getScreenY());

			// If current X / Y is smaller then 0, then that coordinate has to be re-set. 
			if(startX > event.getScreenX()) {
				//meaning we ahve a negative value for the width.. then we should set the new X to be the startX (i.e set it in the action object).
				setX((int)event.getScreenX());
				setWidth( (int) (startX - event.getScreenX()));
				
			}else {
				setWidth((int) (event.getScreenX() - startX));
			}
			if(startY > event.getScreenY()) {
				setY((int)event.getScreenY());
				setHeight((int) (startY - event.getScreenY()));
			}else {
				
				setHeight((int)event.getScreenY() - startY);
			}

			System.out.println(toString());
			clearPopups();
			try {
				takeScreenShot();
			} catch (AWTException | IOException e) {
				System.out.println(e.getMessage());
			}
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		popup.getScene().setOnMouseReleased(weak_event_handler);
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
