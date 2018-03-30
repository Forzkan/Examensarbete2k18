package examensarbete.javafx.controller;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.action.ClickAction;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.action.TextTypeAction;
import examensarbete.model.test.TestGroup;
import examensarbete.model.test.TestHandler;
import examensarbete2k18.model.properties.PropertiesHandler;
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
import javafx.scene.text.Text;
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


	
	private final TestHandler testHandler = new TestHandler();
	
	public WorkAreaController() {
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
			fxmlLoader.setController(preferenceController);
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

	// TODO :: På browserclick så kollar vi vad som är valt i listan av test, 
	// och om något är valt så hämtar vi gruppnamn och testnamn.
	// Sedan lägger vi till den action vi har klickat på.
	
	@FXML
	private void onBrowserButtonClick() {
		
//		BrowserAction browserAction;
//		try {
//			browserAction = new BrowserAction();
//			browserAction.actionSetup();
//		} catch (AWTException e) {
//			System.out.println(e.getMessage());
//		}
		// TODO:: Remove this button.
		//TESTING:
		getSelectedTestsAndOrGroup();
	}

	@FXML
	private void onClickButtonPressed() {
		ClickAction clickAction;
		try {
			clickAction = new ClickAction();
			clickAction.actionSetup();

		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}



	@FXML
	private void onScreenCropCaptureClicked(ActionEvent event) {
		SnapImageAction snapAction;
		try {
			snapAction = new SnapImageAction();
			snapAction.actionSetup();

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

		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	private void snapImageFromBounds() {
		try {
			ChromeWebAction cwa = new ChromeWebAction();
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


	
	 
	
	private ArrayList<TestGroup> getSelectedTestsAndOrGroup() {
		ArrayList<TestGroup> tests = new ArrayList<TestGroup>();
		TreeItem<String> selected = treeView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			List<TestGroup> testGroups = testHandler.getTestList();
			String parentName = selected.getParent().getValue().toString();
			String selectedName = selected.getValue().toString();

			if (parentName.equals("Test Cases")) {
				for (TestGroup tg : testGroups) {
					if (tg.getGroupName().equals(selectedName)) {
						tests.add(tg);
					}
				}
			} else {
				for (TestGroup tg : testGroups) {
					if (selectedName.equals(tg.getTestName()) && parentName.equals(tg.getGroupName())) {
						tests.add(tg);
						break;
					}
				}
			}
		}
		return tests;
	}
	
	
	
	@FXML
	private AnchorPane treeViewPane;
	private TreeView<String> treeView;
	
	private void treeViewLoader() {
		testHandler.loadSavedTests();
		List<TestGroup> testCollection = testHandler.getTestList();
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

		TreeItem<String> rootNode = new TreeItem<String>("Test Cases", rootIcon);

		rootNode.setExpanded(true);
		for (TestGroup c : testCollection) {
			TreeItem<String> testNameLeaf = new TreeItem<String>(c.getTestName());
			boolean found = false;
			for (TreeItem<String> collNode : rootNode.getChildren()) {
				if (collNode.getValue().contentEquals(c.getGroupName())) {
					collNode.getChildren().add(testNameLeaf);
					found = true;
					break;
				}
			}
			if (!found) {
				TreeItem<String> collNode = new TreeItem<String>(c.getGroupName(), new ImageView(collectionIcon));
				rootNode.getChildren().add(collNode);
				collNode.getChildren().add(testNameLeaf);
			}
		}

		treeView = new TreeView<String>(rootNode);
		treeViewPane.getChildren().add(treeView);
		treeView.prefHeightProperty().bind(treeViewPane.heightProperty());
		treeView.prefWidthProperty().bind(treeViewPane.widthProperty());

	}



}
