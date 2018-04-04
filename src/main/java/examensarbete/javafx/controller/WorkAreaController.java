package examensarbete.javafx.controller;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import examensarbete.javafx.stage.StageHandler;
import examensarbete.javafx.stage.TTStage;
import examensarbete.model.action.ActionBase;
import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.action.ClickAction;
import examensarbete.model.action.IAction;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.action.TextTypeAction;
import examensarbete.model.test.TestGroup;
import examensarbete.model.test.TestHandler;
import examensarbete.model.test.TestStepImpl;
import examensarbete2k18.model.properties.PropertiesHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// TODO:: Handle page loading times:   driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
public class WorkAreaController {

	@FXML
	private AnchorPane headerPane, toolBarPane, workAreaPane;
	@FXML
	private Button playButton, openBrowserButton, mouseClickAction, snipImageButton;
	@FXML
	private GridPane workAreaGridPane;
	@FXML
	private Text currentXCoordinateText, currentYCoordinateText;
	@FXML
	private HBox stepsHBox;

	private StageHandler stageHandler = new StageHandler();
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
		PreferencesController preferenceController = new PreferencesController();
		preferencesStage = stageHandler.openStage(TTStage.PREFERENCES, preferenceController);
	}

	@FXML
	private void saveSelectedTest() {
		if(selectedTest != null && isRecording == false) {
			testHandler.saveTest(selectedTest);
			System.out.println("Saving selected test.");
		}
	}
	
	@FXML
	private void saveAllTests() {
		if(selectedTest != null && isRecording == false) {
			testHandler.saveTests();
			System.out.println("Saving all tests.");
		}
	}
	
	@FXML
	private void runSelectedTests() {
		// TODO:: Do something with the results. (create report)
		if(rootIsSelected()) {
			// Run all tests for all groups.
		}else if(parentIsSelected()) {
			// Run all tests in group.
			for(TestGroup tg : getSelectedTestsAndOrGroup()) {
				tg.getTest().runTest();
			}
		}else if(selectedTest != null) {
			// Run selected test.
			selectedTest.getTest().runTest();
			selectedTest.getTest().cleanup();
		}
		
	}
	
	// TODO :: På browserclick så kollar vi vad som är valt i listan av test,
	// och om något är valt så hämtar vi gruppnamn och testnamn.
	// Sedan lägger vi till den action vi har klickat på.

	// TODO :: CREATE AND SAVE TESTS.
	// TODO:: FIND OUT A WAY TO SEE IF WEBPAGE HAS LOADED.

	@FXML
	private void onBrowserButtonClick() {

		// BrowserAction browserAction;
		// try {
		// browserAction = new BrowserAction();
		// browserAction.actionSetup();
		// } catch (AWTException e) {
		// System.out.println(e.getMessage());
		// }
		// TODO:: Remove this button.
		// TESTING:
		getSelectedTestsAndOrGroup();
	}

	@FXML
	private void onClickButtonPressed() {

		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
			ClickAction clickAction;
			try {
				clickAction = new ClickAction();
				clickAction.actionSetup();
				addActionToSelectedTest(clickAction);
			} catch (AWTException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@FXML
	private void onScreenCropCaptureClicked(ActionEvent event) {
		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
			SnapImageAction snapAction;
			try {
				snapAction = new SnapImageAction();
				snapAction.actionSetup();
				
				addActionToSelectedTest(snapAction);
			} catch (AWTException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@FXML
	private void onTextTypeButtonPressed() {
		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
			TextTypeAction typeAction;
			try {
				typeAction = new TextTypeAction();
				typeAction.actionSetup();
				addActionToSelectedTest(typeAction);
			} catch (AWTException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@FXML
	private void snapImageFromBounds() {
//		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
//			try {
//				ChromeWebAction cwa = new ChromeWebAction();
//				cwa.actionSetup();
//				cwa.performAction();
//				System.out.println(cwa.takeBrowserScreenshot("aUniqueNameForBrowserScreenshot"));
//				selectedTest.getTest().addTestStep(cwa);
//				updateSelectedTestHBox();
//			} catch (AWTException e) {
//				System.out.println(e.getMessage());
//			}
//		}
		
		
		// AutomaticImageSnap autoSnap;
		// try {
		// autoSnap = new AutomaticImageSnap(snapAction.getRectangleBounds());
		// autoSnap.actionSetup();
		// } catch (AWTException e) {
		// System.out.println(e.getMessage());
		// }
	}

	private Stage newTestStage;

	@FXML
	private void createNewTest() {
		NewTestController ntC = new NewTestController(testHandler, this);
		newTestStage = stageHandler.openStage(TTStage.NEW_TEST, ntC);
	}

	@FXML
	private Button recordButton;
	@FXML
	private FontIcon recordFontIcon;

	private boolean isRecording = false;

	@FXML
	private void startTestRecording() {
		// Get selected
		if (isRecording == false) {

			// Check if a test is selected, else we should not start recording.
			if (!rootIsSelected() && !parentIsSelected() && selectedTest != null) {
				isRecording = true;
				// change logo of the button,
				recordFontIcon.setIconLiteral("ion-stop");
				// Running the default step which is present for all tests, which is opening the
				// browser.
				selectedTest.getTest().getSteps().get(0).performTestStep();
			} else {
				System.out.println("No test have been selected.");
			}
		} else {
			// change back the logo of the button,
			stopRecording();
		}
	}

	private void stopRecording() {
		recordFontIcon.setIconLiteral("ion-record");
		isRecording = false;
	}

	private ArrayList<TestGroup> getSelectedTestsAndOrGroup() {
		System.out.println("LOOKING FOR SELECTED TEST");
		ArrayList<TestGroup> tests = new ArrayList<TestGroup>();
		try {
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
		} catch (Exception e) {
			System.out.println("Root has been selected.");
		}
		return tests;
	}

	private boolean isRecordingAndTestIsSelected() {
		if (isRecording && selectedTest != null) {
			return true;
		}
		return false;
	}

	@FXML
	private AnchorPane treeViewPane;
	private TreeView<String> treeView;

	public void treeViewLoader() {
		testHandler.loadSavedTests();
		List<TestGroup> testCollection = testHandler.getTestList();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/root.png");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Node rootIcon = new ImageView(new Image(input));

		InputStream collInput = null;
		try {
			collInput = new FileInputStream("src/main/resources/collectionIcon.PNG");
		} catch (Exception e) {
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
		setOnTestSelectedEvent(treeView);
	}

	EventHandler<MouseEvent> event_handler;
	WeakEventHandler<MouseEvent> weak_event_handler;

	// TODO:: CALL THIS ONCE A TEST HAS BEEN CLICKED.
	private void setOnTestSelectedEvent(Node node) {

		event_handler = (MouseEvent event) -> {
			ArrayList<TestGroup> selectedList = getSelectedTestsAndOrGroup();
			if (parentIsSelected()) {
				// Multiple tests "selected" since the parent is selected.
				selectedTest = null;
				System.out.println("SELECTED PARENT");
			} else if (parentIsSelected() == false && rootIsSelected() == false && selectedList.size() >= 1) {
				selectedTest = selectedList.get(0);
				updateSelectedTestHBox();
				System.out.println("SELECTED TEST");
			} else {
				// Do we want to do anything if no test is selected? for now, no.
				// ROOT or nothing selected.
			}
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		node.setOnMouseClicked(weak_event_handler);

	}

	private boolean rootIsSelected() {
		try {
			TreeItem<String> selected = treeView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				String selectedName = selected.getValue().toString();

				if (selectedName.equals("Test Cases")) {
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("Root has been selected.");
		}
		return false;
	}

	private boolean parentIsSelected() {
		try {
			TreeItem<String> selected = treeView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				List<TestGroup> testGroups = testHandler.getTestList();
				String parentName = selected.getParent().getValue().toString();
				String selectedName = selected.getValue().toString();

				if (parentName.equals("Test Cases")) {
					for (TestGroup tg : testGroups) {
						if (tg.getGroupName().equals(selectedName)) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Root has been selected.");
		}
		return false;
	}

	private void updateSelectedTestHBox() {
		stepsHBox.getChildren().clear();
		try {
			if (selectedTest != null) {
				ArrayList<Button> testStepsList = getStepsAsButtonList();
				stepsHBox.getChildren().addAll(testStepsList);
			}
		} catch (Exception e) {
			System.out.println("Could not load test steps from selected test..");
		}

	}

	private TestGroup selectedTest;

	private ArrayList<Button> getStepsAsButtonList() {
		ArrayList<Button> list = new ArrayList<Button>();

		for (TestStepImpl step : selectedTest.getTest().getSteps()) {
			FontIcon fontIcon = new FontIcon();
			fontIcon.setIconLiteral(getFontIconLiteralForAction(step.getMainAction()));
			fontIcon.setIconSize(33);
			fontIcon.setIconColor(Paint.valueOf("white"));
			Button btn = new Button();
			btn.setStyle("-fx-background-color: black;");
			btn.setGraphic(fontIcon);
			list.add(btn);
		}
		return list;
	}

	private String getFontIconLiteralForAction(ActionBase action) {
		String literal = "";
		switch (action.getType()) {
		case CLICK:
			literal = "ion-mouse";
			break;
		case IMAGESNAP:
			literal = "ion-crop";
			break;
		case KEYPRESS:
			literal = "fas-keyboard";
			break;
		case TYPE:
			literal = "fas-keyboard";
			break;
		case BROWSER:
			literal = "ion-social-chrome";
			break;
		case AUTOIMAGESNAP:
			literal = "ion-crop";
			break;
		case CHROMEBROWSER:
			literal = "ion-social-chrome";
			break;
		case AUTOCLICK:
			literal = "ion-mouse";
			break;
		}
		return literal;
	}

	private void updateSelectedStep() {

	}
	
	
	
	private void addActionToSelectedTest(ActionBase action) {
		selectedTest.getTest().addTestStep(action);
		updateSelectedTestHBox();
	}

}
