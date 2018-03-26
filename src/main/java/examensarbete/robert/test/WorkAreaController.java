package examensarbete.robert.test;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import examensarbete.javafx.controller.PreferencesController;
import examensarbete.model.action.BrowserAction;
import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.action.ClickAction;
import examensarbete.model.action.IAction;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.action.TextTypeAction;
import examensarbete2k18.model.properties.PropertiesHandler;
import examensarbete2k18.model.properties.TTProperties;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
		treeViewLoader();
	}

	private Stage preferencesStage;

	@FXML
	private void openPreferencesWindow() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/robert_test/Preferences.fxml"));
			PreferencesController preferenceController = new PreferencesController(preferencesStage);
			fxmlLoader.setController(preferenceController); // TODO:: Proper controller.
			Parent parent = (Parent) fxmlLoader.load();
			preferencesStage = new Stage();
			preferencesStage.setTitle("Preferences");
			preferencesStage.setScene(new Scene(parent));
			preferencesStage.show();

		} catch (IOException e) {
			System.out.println("Could not open the properties Window.");
			System.out.println(e.getMessage());
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
			clickAction = new ClickAction(stage, 0, 0);
			clickAction.actionSetup();
			listOfActions.add(clickAction);
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}

	SnapImageAction snapAction;

	@FXML
	private void onScreenCropCaptureClicked(ActionEvent event) {

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
		// AutomaticImageSnap autoSnap;
		// try {
		// autoSnap = new AutomaticImageSnap(snapAction.getRectangleBounds());
		// autoSnap.actionSetup();
		// } catch (AWTException e) {
		// System.out.println(e.getMessage());
		// }
	}

	// private EventHandler<MouseEvent> eHandler_MouseMovement;
	// private WeakEventHandler<MouseEvent> weakEventHandler_MouseMovement;
	//
	// private EventHandler<MouseEvent> eHandler_MouseSnippMovement;
	// private WeakEventHandler<MouseEvent> weakEventHandler_MouseSnippMovement;

	// public void activateCursorPositionListener() {
	// eHandler_MouseMovement = (MouseEvent event) -> {
	// currentXCoordinateText.setText(event.getScreenX() + "");
	// currentYCoordinateText.setText(event.getScreenY() + "");
	// };
	// weakEventHandler_MouseMovement = new
	// WeakEventHandler<>(eHandler_MouseMovement);
	// stage.getScene().setOnMouseMoved(weakEventHandler_MouseMovement);
	// }

	// private Rectangle createScreenSnipRectangle(ActionEvent event) {
	// Rectangle rect = new Rectangle();
	//
	//// rect.setX(event.getScreenX());
	//// rect.setY(event.getScreenY());
	// rect.setWidth(100);
	// rect.setHeight(100);
	// // Initialize the snap animation.
	// Popup snippPopup = new Popup();
	//
	// double x = 100;
	// double y = 100;
	//
	// rect.setFill(Color.WHITE);
	// rect.setStroke(Color.BLACK);
	// snippPopup.setWidth(100);
	// snippPopup.setHeight(100);
	// snippPopup.getContent().add(rect);
	// snippPopup.setX(x);
	// snippPopup.setY(y);
	//// snippPopup.setOpacity(0.3);
	//
	// snippPopup.show(stage.getScene().getWindow());
	//
	// //getDefaultSnapPopup(snapPosition,
	// event).show(rootNode.getScene().getWindow());
	// //stage.setAlwaysOnTop(true);
	// eHandler_MouseSnippMovement = (MouseEvent m_event) -> {
	// snippPopup.setX(m_event.getScreenX());
	// snippPopup.setY(m_event.getScreenY());
	// };
	// weakEventHandler_MouseSnippMovement = new
	// WeakEventHandler<>(eHandler_MouseSnippMovement);
	// rect.setOnMouseMoved(weakEventHandler_MouseSnippMovement);
	//
	//
	// return rect;
	// }

	
	List<TestCollection> testCollection = new ArrayList<TestCollection>();
	
	private void createTestCollectionList() {
		File directory = new File(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()));
		if(directory.exists() && directory.isDirectory()) {
			for(File f : directory.listFiles()) {
				if(f.isDirectory()) {
					String collectionName = f.getName();
					System.out.println("COLLECTION: " + collectionName);
					for(File test : f.listFiles()) {
						if(test.isFile() && !test.isDirectory()) {
							if(getFileExtension(test).equals("json")) {
								testCollection.add(new TestCollection(test.getName().replaceAll(".json", ""), collectionName));
								System.out.println("TESTNAME: " + test.getName().replaceAll(".json", ""));
							}
						}
					}
				}
			}
		}
	}
	
	
    private String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
        	 return fileName.substring(fileName.lastIndexOf(".")+1);
        }else {
        	return "";
        }
    }
    
//    private String getFileNameWithoutExtension(File file) {
//        String fileName = file.getName();
//       System.out.println(fileName);
//    	StringBuilder name = new StringBuilder(); 
//    	name.append("");
//    	String[] parts = fileName.split(".");
//    	for(int i = 0; i < parts.length; i++) {
//    		name.append(parts[i]).toString();
//    	}
//    	return name.toString();
//    }
	
	@FXML
	private AnchorPane treeViewPane;

	private void treeViewLoader() {
		createTestCollectionList();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/root.png");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		Node rootIcon = new ImageView(new Image(input));

		InputStream collInput = null;
		try {
			collInput = new FileInputStream("src/main/resources/collectionIcon.PNG");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		Image collectionIcon = new Image(collInput);

//		List<TestCollection> collection = Arrays.<TestCollection>asList(
//				new TestCollection("Test 1", "Sales Department"),
//				new TestCollection("Test 2", "Sales Department"),
//				new TestCollection("Test 3", "Sales Department"),
//				new TestCollection("Test 4", "Sales Department"),
//				new TestCollection("Test 5", "Sales Department"),
//				new TestCollection("Test 6", "Sales Department"),
//				new TestCollection("Test 1", "IT Support"), 
//				new TestCollection("Test 2", "IT Support"),
//				new TestCollection("Test 3", "IT Support"),
//				new TestCollection("Test 1", "Accounts Department"),
//				new TestCollection("Test 2", "Accounts Department"));

		TreeItem<String> rootNode = new TreeItem<String>("Test Cases", rootIcon);

		rootNode.setExpanded(true);
		for (TestCollection c : testCollection) {
			TreeItem<String> testNameLeaf = new TreeItem<String>(c.getTestName());
			boolean found = false;
			for (TreeItem<String> collNode : rootNode.getChildren()) {
				if (collNode.getValue().contentEquals(c.getCollectionName())) {
					collNode.getChildren().add(testNameLeaf);
					found = true;
					break;
				}
			}
			if (!found) {
				TreeItem<String> collNode = new TreeItem<String>(c.getCollectionName(), new ImageView(collectionIcon));
				rootNode.getChildren().add(collNode);
				collNode.getChildren().add(testNameLeaf);
			}
		}

		TreeView<String> treeView = new TreeView<String>(rootNode);
		treeViewPane.getChildren().add(treeView);
		treeView.prefHeightProperty().bind(treeViewPane.heightProperty());
		treeView.prefWidthProperty().bind(treeViewPane.widthProperty());

	}

	public class TestCollection {

		private final SimpleStringProperty testName;
		private final SimpleStringProperty collectionName;

		public TestCollection(String testName, String collectionName) {
			this.testName = new SimpleStringProperty(testName);
			this.collectionName = new SimpleStringProperty(collectionName);
		}

		public String getTestName() {
			return testName.get();
		}

		public String getCollectionName() {
			return collectionName.get();
		}

		public void setTestName(String testName) {
			this.testName.set(testName);
		}

		public void setCollectionName(String collectionName) {
			this.collectionName.set(collectionName);
		}

	}

}
