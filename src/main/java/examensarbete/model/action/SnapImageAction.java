package examensarbete.model.action;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;

import examensarbete.main.TTMain;
import examensarbete.model.test.TestImageImpl;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Screen;


public class SnapImageAction extends ActionBase {

	
	private TestImageImpl snapImage;
	private TestImageImpl fullPageImage;
	
	public TestImageImpl getSnapImage() {
		return snapImage;
	}
	public void setSnapImage(TestImageImpl snapImage) {
		this.snapImage = snapImage;
	}

	public TestImageImpl getFullPageImage() {
		return fullPageImage;
	}
	public void setFullPageImage(TestImageImpl fullPageImage) {
		this.fullPageImage = fullPageImage;
	}

	@Override
	public EActionType getType() {
		return this.actionType;
	}
	
	
	public SnapImageAction() throws AWTException {
		super();
		this.actionType = EActionType.IMAGESNAP;
	}

	@Override
	public void actionSetup() {
		createScreenCoverForSnap();
	}
	
	
	@Override
	public boolean performAction() {
		ClickWithinBoundsAction wBoundsClickAction;
		try {
			wBoundsClickAction = new ClickWithinBoundsAction(snapImage.getBounds());
			wBoundsClickAction.performAction();
		} catch (AWTException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	
	
	







	// ONLY USED FOR WHEN CREATING A NEW SNAPIMAGEACTION... NOT FOR WHEN IT IS LOADED.	
	private void setCoordinates(int x, int y) {
		snapImage.setX(x);
		snapImage.setY(y);
	}
	private void setX(int x) {
		snapImage.setX(x);
	}
	private void setY(int y) {
		snapImage.setY(y);
	}
	
	private void setWidth(int width) {
		snapImage.setWidth(width);
	}
	private void setHeight(int height) {
		snapImage.setHeight(height);
	}
	
	
	
	
	private int startX = 0;
	private int startY = 0;
	private Rectangle snapRectangle;
	private ArrayList<Popup> popups = new ArrayList<Popup>();
	private ArrayList<EventHandler<MouseEvent>> eventHandlers = new ArrayList<EventHandler<MouseEvent>>();
	private ArrayList<WeakEventHandler<MouseEvent>> weakEventHandlers = new ArrayList<WeakEventHandler<MouseEvent>>();
	
	
	
	public void createScreenCoverForSnap(){
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
			
			popup.show(TTMain.primaryStage.getScene().getWindow());
			
			popups.add(popup);
			setSnapClickAndReleasedEvents(popup);
		}
	}
	
	
	private void setSnapClickAndReleasedEvents(Popup popup) {
		snapClicked(popup);
		snapMoving(popup);
		snapReleased(popup);
	}

	
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
			try {// TODO:: PATH!!
				this.takeScreenShot("D://", "UserSnappedImage", snapImage.getBounds());
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
