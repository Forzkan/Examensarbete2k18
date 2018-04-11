package examensarbete.javafx.controller;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import examensarbete.javafx.stage.StageFactory;
import examensarbete.javafx.stage.TTStage;
import examensarbete.model.action.ActionBase;
import examensarbete.model.action.AutomaticImageSnapAction;
import examensarbete.model.action.ClickAction;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.action.TextTypeAction;
import examensarbete.model.action.TimerAction;
import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.test.TestGroup;
import examensarbete.model.test.TestHandler;
import examensarbete.model.test.TestStep;
import examensarbete.model.utility.FileUtility;
import examensarbete.model.utility.WaitHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// TODO:: Handle page loading times:   driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
public class WorkAreaController {
	// TODO:: SORT.
	@FXML
	private AnchorPane headerPane, toolBarPane, workAreaPane;
	@FXML
	private Button playButton, openBrowserButton, mouseClickAction, snipImageButton;
	@FXML
	private GridPane workAreaGridPane;
	@FXML
	private Text currentXCoordinateText, currentYCoordinateText;
	@FXML
	private VBox stepsVBox, targetInfoVBox, contextInfoVBox;

	@FXML
	private Label stepNumberLabel, stepTypeLabel;

	private StageFactory stageFactory = new StageFactory();
	private final TestHandler testHandler = new TestHandler();

	@FXML
	private ImageView aStepsContextImage, aStepsSnapImage;

	@FXML
	private ScrollPane treeViewScrollPane;

	public WorkAreaController() {
		PropertiesHandler.InitializePropertiesHandler();
	}

	@FXML
	public void initialize() {
		treeViewLoader();

	}

	private void clearStepInfo() {
		stepNumberLabel.setText("");
		stepTypeLabel.setText("");
		targetInfoVBox.getChildren().clear();
		contextInfoVBox.getChildren().clear();
		aStepsContextImage.setImage(null);
		aStepsSnapImage.setImage(null);
	}

	private void minimizeMainStage() {
		((Stage) toolBarPane.getScene().getWindow()).setIconified(true);
		((Stage) toolBarPane.getScene().getWindow()).setAlwaysOnTop(false);
	}

	private void displayMainStage() {
		((Stage) toolBarPane.getScene().getWindow()).setIconified(false);
		((Stage) toolBarPane.getScene().getWindow()).show();
		// ((Stage) toolBarPane.getScene().getWindow()).setAlwaysOnTop(true);
	}

	private Stage preferencesStage; // TODO:: Move to a Stage handler? Same with New Test.

	@FXML
	private void openPreferencesWindow() {
		if (preferencesStage == null) {
			PreferencesController preferenceController = new PreferencesController();
			preferencesStage = stageFactory.openStage(TTStage.PREFERENCES, preferenceController);
		} else if (preferencesStage.isShowing() == false) {
			preferencesStage.show();
		}

	}

	@FXML
	private void saveSelectedTest() {
		if (selectedTest != null && isRecording == false) {
			System.out.println("Saving selected test.");
			testHandler.saveTest(selectedTest);

		}
	}

	@FXML
	private void saveAllTests() {
		if (selectedTest != null && isRecording == false) {
			System.out.println("Saving all tests.");
			testHandler.saveTests();
		}
	}

	@FXML
	private void runSelectedTests() {
		// TODO:: Do something with the results. (create report)
		if (!isRecording) {
			if (rootIsSelected()) {
				// Run all tests for all groups.
			} else if (parentIsSelected()) {
				// Run all tests in group.
				for (TestGroup tg : getSelectedTestsAndOrGroup()) {
					tg.getTest().runTest();
					tg.getTest().cleanup();
				}
			} else if (selectedTest != null) {
				// Run selected test.
				selectedTest.getTest().runTest();
				selectedTest.getTest().cleanup();
			}
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
			minimizeMainStage();
			ClickAction clickAction;
			try {
				clickAction = new ClickAction();
				clickAction.actionSetup();
				addActionToSelectedTest(clickAction);
			} catch (AWTException e) {
				System.out.println(e.getMessage());
			}
			displayMainStage();
		}
	}

	@FXML
	private void onScreenCropCaptureClicked(ActionEvent event) {
		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
			minimizeMainStage();
			SnapImageAction snapAction;
			try {
				snapAction = new SnapImageAction(selectedTest.getGroupName(), selectedTest.getTestName());
				snapAction.setStage(((Stage) toolBarPane.getScene().getWindow()));
				addActionToSelectedTest(snapAction);
				snapAction.actionSetup();
			} catch (AWTException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@FXML
	private void onTextTypeButtonPressed() {
		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
			minimizeMainStage();
			TextTypeAction typeAction;
			try {
				typeAction = new TextTypeAction();
				typeAction.actionSetup();

				// Make sure that the browser window if in focus.
				try {
					Rectangle browserBounds = selectedTest.getTest().getChromeWebAction().getBrowserBounds();
					int x = (int) browserBounds.getX();
					int y = (int) browserBounds.getY();
					ClickAction clickAction = new ClickAction();
					clickAction.setCoordinates(x + (browserBounds.getWidth() / 2), y);
					clickAction.performAction();
				} catch (AWTException e) {
					System.out.println(e.getMessage());
				}
				WaitHandler.waitForMilliseconds(1000);
				typeAction.performAction();
				addActionToSelectedTest(typeAction);
			} catch (AWTException e) {
				System.out.println(e.getMessage());
			}
			displayMainStage();
		}
	}

	@FXML
	private void onTimerActionButtonPressed() {
		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
			ActionBase timerAction = new TimerAction();
			timerAction.actionSetup();
			addActionToSelectedTest(timerAction);
		}
	}

	@FXML
	private void onScrollActionButtonPressed() {
		if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
			minimizeMainStage();
			// ToBeImplemented
			displayMainStage();
		}
	}

	@FXML
	private void snapImageFromBounds() {
		// if (isRecordingAndTestIsSelected()) { // i.e. we can add actions to a test.
		// try {
		// ChromeWebAction cwa = new ChromeWebAction();
		// cwa.actionSetup();
		// cwa.performAction();
		// System.out.println(cwa.takeBrowserScreenshot("aUniqueNameForBrowserScreenshot"));
		// selectedTest.getTest().addTestStep(cwa);
		// updateSelectedTestHBox();
		// } catch (AWTException e) {
		// System.out.println(e.getMessage());
		// }
		// }

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
		if (newTestStage == null) {
			NewTestController ntC = new NewTestController(testHandler, this);
			newTestStage = stageFactory.openStage(TTStage.NEW_TEST, ntC);
		} else if (newTestStage.isShowing() == false) {
			newTestStage.show();
		}
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
				setTestListDisabled(true);
				// change logo of the button,
				recordFontIcon.setIconLiteral("ion-stop"); // TODO:: Should not be done here.
				// Running the default step which is present for all tests, which is opening the
				// browser.
				try {
					selectedTest.getTest().getTestSteps().get(0).performTestStep();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("No test have been selected.");
			}
			displayMainStage();
		} else {
			// change back the logo of the button,
			stopRecording();
		}
	}

	private void stopRecording() {
		recordFontIcon.setIconLiteral("ion-record"); // TODO:: Should not be done here..
		isRecording = false;
		setTestListDisabled(false);
		selectedTest.getTest().cleanup();
		testHandler.saveTest(selectedTest);
	}

	private void setTestListDisabled(boolean disabled) {
		testTreeView.setDisable(disabled);
		treeViewPane.setDisable(disabled);
		treeViewScrollPane.setDisable(disabled);
	}

	private ArrayList<TestGroup> getSelectedTestsAndOrGroup() {
		ArrayList<TestGroup> tests = new ArrayList<TestGroup>();
		try {
			TreeItem<String> selected = testTreeView.getSelectionModel().getSelectedItem();
			if (selected != null) {
				if (!rootIsSelected()) {

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
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
	private TreeView<String> testTreeView;

	public void treeViewLoader() {
		testHandler.loadSavedTests();
		List<TestGroup> testCollection = testHandler.getTestList();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/gui_images/root.png");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Node rootIcon = new ImageView(new Image(input));

		InputStream collInput = null;
		try {
			collInput = new FileInputStream("src/main/resources/gui_images/collectionIcon.PNG");
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

		testTreeView = new TreeView<String>(rootNode);
		testTreeView.getStyleClass().add("testTree");
		treeViewPane.getChildren().add(testTreeView);
		testTreeView.prefHeightProperty().bind(treeViewScrollPane.heightProperty());
		testTreeView.prefWidthProperty().bind(treeViewScrollPane.widthProperty());
		setOnTestSelectedEvent(testTreeView);
	}

	EventHandler<MouseEvent> event_handler;
	WeakEventHandler<MouseEvent> weak_event_handler;

	private void setOnTestSelectedEvent(Node node) {

		event_handler = (MouseEvent event) -> {
			ArrayList<TestGroup> selectedList = getSelectedTestsAndOrGroup();
			if (parentIsSelected()) {
				// Multiple tests "selected" since the parent is selected.
				selectedTest = null;
				updateSelectedTestHBox();
				clearStepInfo();
			} else if (parentIsSelected() == false && rootIsSelected() == false && selectedList.size() >= 1) {
				selectedTest = selectedList.get(0);
				updateSelectedTestHBox();
			} else {
				// Do we want to do anything if no test is selected? for now, no.
				// ROOT or nothing selected.
				updateSelectedTestHBox();
				clearStepInfo();
			}
		};
		weak_event_handler = new WeakEventHandler<>(event_handler);
		node.setOnMouseClicked(weak_event_handler);

	}

	private boolean rootIsSelected() {
		try {
			TreeItem<String> selected = testTreeView.getSelectionModel().getSelectedItem();
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
			if (!rootIsSelected()) {

				TreeItem<String> selected = testTreeView.getSelectionModel().getSelectedItem();
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
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	private void updateSelectedTestHBox() {
		stepsVBox.getChildren().clear();
		try {
			if (selectedTest != null) {
				ArrayList<Button> testStepsList = getStepsAsButtonList();
				stepsVBox.getChildren().addAll(testStepsList);
			}
		} catch (Exception e) {
			System.out.println("Could not load test steps from selected test..");
		}

	}

	private TestGroup selectedTest;

	private ArrayList<Button> getStepsAsButtonList() {
		ArrayList<Button> list = new ArrayList<Button>();

		for (TestStep step : selectedTest.getTest().getTestSteps()) {
			FontIcon fontIcon = new FontIcon();
			fontIcon.setIconLiteral(getFontIconLiteralForAction(step.getMainAction()));
			fontIcon.setIconSize(33);
			fontIcon.setIconColor(Paint.valueOf("white"));
			Button btn = new Button();
			btn.setStyle("-fx-background-color: black;");
			btn.setGraphic(fontIcon);
			btn.setOnMouseClicked(event -> {
				for (int i = 0; i < stepsVBox.getChildren().size(); i++) {
					if (stepsVBox.getChildren().get(i).equals(btn)) {
						updateSelectedStepInformation(selectedTest.getTest().getTestSteps().get(i));
					}
				}
			});
			btn.setPrefWidth(stepsVBox.getPrefWidth() - 8);
			btn.setPrefHeight(stepsVBox.getPrefWidth() - 8);
			btn.setMinWidth(stepsVBox.getPrefWidth() - 8);
			btn.setMinHeight(stepsVBox.getPrefWidth() - 8);

			list.add(btn);
		}
		return list;
	}

	// TODO:: Move to Ikonli Helper class / GUI helper class..
	private String getFontIconLiteralForAction(ActionBase action) {
		String literal = "";
		switch (action.getActionType()) {
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
		default:
			break;
		}
		return literal;
	}

	private void updateSelectedStepInformation(TestStep selectedStep) {
		if (selectedStep != null) {
			clearStepInfo();
			URL contextURL = null, snapURL = null;
			switch (selectedStep.getMainAction().getActionType()) {
			case IMAGESNAP:
				SnapImageAction snapAction = (SnapImageAction) selectedStep.getMainAction();
				snapURL = FileUtility.getImageUrlFromPath(snapAction.getTargetImage().getImagePath());
				contextURL = FileUtility.getImageUrlFromPath(selectedStep.getTestStepContextImage().getImagePath());
				for (String info : snapAction.getListOfActionInformation()) {
					Label l = new Label(info);
					l.setStyle("-fx-font-size: 18px;");
					l.setStyle("-fx-text-fill: white;");
					l.setWrapText(true);
					targetInfoVBox.getChildren().add(l);

				}
				break;
			case AUTOIMAGESNAP:
				AutomaticImageSnapAction aSnapAction = (AutomaticImageSnapAction) selectedStep.getMainAction();
				snapURL = FileUtility.getImageUrlFromPath(aSnapAction.getSnapImage().getImagePath());// TODO:: Target
																										// Image, once
																										// im looking
																										// into this
																										// one..
				contextURL = FileUtility.getImageUrlFromPath(selectedStep.getTestStepContextImage().getImagePath());
				break;
			default:
				break;
			}

			try {
				aStepsSnapImage.setImage(new Image(snapURL.toString()));
				aStepsContextImage.setImage(new Image(contextURL.toString()));

			} catch (Exception e) {
				System.out.println("Could not display Step image(s)." + " \nError: " + e.getMessage());
			}
			// targetInfoVBox, contextInfoVBox;
			stepNumberLabel.setText("N/A");
			stepTypeLabel.setText(selectedStep.getMainAction().getActionType().name().toUpperCase());

			if (selectedStep.getTestStepContextImage() != null) {
				for (String info : selectedStep.getListOfContextInformation()) {
					Label l = new Label(info);
					l.setStyle("-fx-font-size: 18px;");
					l.setStyle("-fx-text-fill: white;");
					l.setWrapText(true);
					contextInfoVBox.getChildren().add(l);
				}
			}
		}
	}

	private void addActionToSelectedTest(ActionBase action) {
		selectedTest.getTest().addTestStep(action);
		updateSelectedTestHBox();
	}

}
