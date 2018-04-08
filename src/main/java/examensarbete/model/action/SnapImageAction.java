package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import examensarbete.google.cloud.vision.GCVConnector;
import examensarbete.google.cloud.vision.GCVImageResult;
import examensarbete.javafx.controller.ImageViewerController;
import examensarbete.javafx.stage.StageFactory;
import examensarbete.javafx.stage.TTStage;
import examensarbete.main.TTMain;
import examensarbete.model.test.TestImage;
import examensarbete.model.test.TestImageImpl;
import examensarbete.model.utility.FileUtility;
import examensarbete.model.utility.WaitHandler;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SnapImageAction extends ActionBase {

	@JsonIgnore
	private StageFactory stageFactory;

	// Also referred to as target image.
	private TestImage targetImage;

	public TestImage getTargetImage() {
		return targetImage;
	}

	public void setTargetImage(TestImage target) {
		targetImage = target;
	}

	@Override
	public EActionType getActionType() {
		return this.actionType;
	}

	// These are only used once when creating the test the first time.
	@JsonIgnore
	String testGroup, testName;

	public SnapImageAction(String testGroup, String testName) throws AWTException {
		super();
		this.actionType = EActionType.IMAGESNAP;
		targetImage = new TestImageImpl();
		this.testGroup = testGroup;
		this.testName = testName;
		System.out.println("Creating new SnapImageAction");
		stageFactory = new StageFactory();
	}

	public SnapImageAction() throws AWTException {
		super();
		this.actionType = EActionType.IMAGESNAP;
		targetImage = new TestImageImpl();
		stageFactory = new StageFactory();
	}

	@Override
	public void actionSetup() {
		createScreenCoverForSnap();
	}

	@Override
	public boolean performAction() {
		Point clickCoords = targetImage.getClickCoordinates();
		if (clickCoords != null) {
			this.performClick(clickCoords.x, clickCoords.y);
		} else {
			ClickWithinBoundsAction wBoundsClickAction;
			try {
				wBoundsClickAction = new ClickWithinBoundsAction(targetImage.getBounds());
				wBoundsClickAction.actionSetup();
				wBoundsClickAction.performAction();
			} catch (AWTException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private int startX = 0;
	private int startY = 0;
	private Rectangle snapRectangle;
	private ArrayList<Popup> popups = new ArrayList<Popup>();
	private ArrayList<EventHandler<MouseEvent>> eventHandlers = new ArrayList<EventHandler<MouseEvent>>();
	private ArrayList<WeakEventHandler<MouseEvent>> weakEventHandlers = new ArrayList<WeakEventHandler<MouseEvent>>();

	// only for the primary screen at this point, just slap on a for loop and call
	// getAllScreens() to do it for all.
	// it does need some small tweaking because there's something strange happening
	// when snaping images from the other screens.
	public void createScreenCoverForSnap() {
		Screen screen = Screen.getPrimary();

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

	private void setSnapClickAndReleasedEvents(Popup popup) {
		snapClicked(popup);
		snapMoving(popup);
		snapReleased(popup);
	}

	private void snapClicked(Popup popup) {
		EventHandler<MouseEvent> event_handler;
		WeakEventHandler<MouseEvent> weak_event_handler;

		event_handler = (MouseEvent event) -> {
			startX = (int) event.getScreenX();
			startY = (int) event.getScreenY();
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
			if (startX > event.getScreenX()) {
				// meaning we ahve a negative value for the width.. then we should set the new X
				// to be the startX (i.e set it in the action object).
				snapRectangle.setX((int) event.getScreenX());
				snapRectangle.setWidth((int) (startX - event.getScreenX()));

			} else {
				snapRectangle.setWidth((int) (event.getScreenX() - startX));
			}
			if (startY > event.getScreenY()) {
				snapRectangle.setY((int) event.getScreenY());
				snapRectangle.setHeight((int) (startY - event.getScreenY()));
			} else {

				snapRectangle.setHeight((int) event.getScreenY() - startY);
			}
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		popup.getScene().setOnMouseDragged(weak_event_handler);

	}

	private void snapReleased(Popup popup) {
		EventHandler<MouseEvent> event_handler;
		WeakEventHandler<MouseEvent> weak_event_handler;

		event_handler = (MouseEvent event) -> {
			// If current X / Y is smaller then 0, then that coordinate has to be re-set.
			if (startX > event.getScreenX()) {
				// meaning we ahve a negative value for the width.. then we should set the new X
				// to be the startX (i.e set it in the action object).
				snapRectangle.setX((int) event.getScreenX());
				snapRectangle.setWidth((int) (startX - event.getScreenX()));

			} else {
				snapRectangle.setWidth((int) (event.getScreenX() - startX));
			}
			if (startY > event.getScreenY()) {
				snapRectangle.setY((int) event.getScreenY());
				snapRectangle.setHeight((int) (startY - event.getScreenY()));
			} else {

				snapRectangle.setHeight((int) event.getScreenY() - startY);
			}

			System.out.println(toString());
			clearPopups();
			try {
				java.awt.Rectangle bounds = new java.awt.Rectangle();
				bounds.setBounds((int) snapRectangle.getX(), (int) snapRectangle.getY(), (int) snapRectangle.getWidth(),
						(int) snapRectangle.getHeight());

				targetImage.setImagePath(
						this.takeScreenShot(FileUtility.createUniqueSnapImageFilePath(testGroup, testName), bounds));
				targetImage.setCoordinates(new Point((int) bounds.getX(), (int) bounds.getY()));
				WaitHandler.waitForMilliseconds(200);
				System.out.println("");
				// DISPLAY THE IMAGEVIEWER.
				stageFactory.getStage(TTStage.IMAGEVIEWER, new ImageViewerController(targetImage)).showAndWait();
				WaitHandler.waitForMilliseconds(1000);
				performAction();
				targetImage.setImageGCVResults(GCVConnector.getGCVImageResult(targetImage.getImagePath()));
				displayWindow();
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

	// Used to display the stage again after the clicks are done..
	@JsonIgnore
	private Stage stage;

	@JsonIgnore
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void displayWindow() {
		stage.setIconified(false);
		stage.show();
		// stage.setAlwaysOnTop(true);
	}

	@JsonIgnore
	public ArrayList<String> getListOfActionInformation() {
		ArrayList<String> ret = new ArrayList<String>();
		if (targetImage != null) {
			ret.add("Target Image: " + targetImage.getImagePath());
			if (targetImage.getCoordinates() != null) {
				ret.add("X Coordinate: " + targetImage.getCoordinates().x);
				ret.add("Y Coordinate: " + targetImage.getCoordinates().y);
			} else {
				ret.add("X Coordinate: N/A");
				ret.add("Y Coordinate: N/A");
			}
			ret.add("Image Width: " + targetImage.getImageWidth());
			ret.add("Image Height: " + targetImage.getImageHeight());
			ret.add("Image taken on screen with resolution: " + targetImage.getResolutionX() + "x"
					+ targetImage.getResolutionY());
		}
		return ret;
	}

}
