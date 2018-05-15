package examensarbete.javafx.controller;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import examensarbete.model.action.ActionBase;
import examensarbete.model.action.EActionType;
import examensarbete.model.test.Test;
import examensarbete.model.test.TestGroup;
import examensarbete.model.test.TestImage;
import examensarbete.model.test.TestResult;
import examensarbete.model.test.TestStep;
import examensarbete.model.test.TestStepResult;
import examensarbete.model.utility.FileUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TestResultController {

    @FXML
    private VBox testListVBox;

    @FXML
    private VBox testStepVBox;

    @FXML
    private ImageView baselineImageView;

    @FXML
    private ImageView newContextImageView;

    @FXML
    private Button updateBaselineButton;

    @FXML
    private Button failTestButton;
    
	@FXML
	private ScrollPane treeViewScrollPane;
    
    
    @FXML
    void failTest(ActionEvent event) {
    	
    }

    @FXML
    void updateStepBaseline(ActionEvent event) {
    	
    }
    	
    
    private List<TestResult> resultCollection = new ArrayList<TestResult>();
    
    
    public void TestResultController(ArrayList<TestResult> results) {
    	// Get test result, 
    	resultCollection = results;

    	// populate test list.
    	treeViewLoader();
    }
    
    private TestResult selectedTest = null;
    
	private TreeView<String> resultTreeView;
	public void treeViewLoader() {
//		testHandler.loadSavedTests();
		
		
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
		for (TestResult r : resultCollection) {
			Test c = r.getTest();
			TreeItem<String> testNameLeaf = new TreeItem<String>(c.getTestName());
			boolean found = false;
			for (TreeItem<String> collNode : rootNode.getChildren()) {
				if (collNode.getValue().contentEquals(c.getTestGroupName())) {
					collNode.getChildren().add(testNameLeaf);
					found = true;
					break;
				}
			}
			if (!found) {
				TreeItem<String> collNode = new TreeItem<String>(c.getTestGroupName(), new ImageView(collectionIcon));
				rootNode.getChildren().add(collNode);
				collNode.getChildren().add(testNameLeaf);
			}
		}

		resultTreeView = new TreeView<String>(rootNode);
		resultTreeView.getStyleClass().add("testTree");
		treeViewScrollPane.getChildrenUnmodifiable().add(resultTreeView);
		resultTreeView.prefHeightProperty().bind(treeViewScrollPane.heightProperty());
		resultTreeView.prefWidthProperty().bind(treeViewScrollPane.widthProperty());
		setOnTestSelectedEvent(resultTreeView);
	}
    
	EventHandler<MouseEvent> event_handler;
	WeakEventHandler<MouseEvent> weak_event_handler;

	private void setOnTestSelectedEvent(Node node) {

//		event_handler = (MouseEvent event) -> {
////			ArrayList<TestGroup> selectedList = getSelectedTestsAndOrGroup();
////			if (parentIsSelected()) {
////				// Multiple tests "selected" since the parent is selected.
////				selectedTest = null;
////				updateSelectedTestHBox();
////				clearStepInfo();
////			} else if (parentIsSelected() == false && rootIsSelected() == false && selectedList.size() >= 1) {
////				selectedTest = selectedList.get(0);
////				updateSelectedTestHBox();
////			} else {
////				// Do we want to do anything if no test is selected? for now, no.
////				// ROOT or nothing selected.
////				updateSelectedTestHBox();
////				clearStepInfo();
////			}
//			if(!rootIsSelected()) {
//				// Get the selected test by iterating over the list of test results for the one that has a test with the name and group name of the selected test. 
//				 for(TestResult r : resultCollection) {
////					 String name = resultTreeView.get
////					 String group = 
////					 if() {
////						 
////					 }
//				 }
//			}
//			
//		};
//		weak_event_handler = new WeakEventHandler<>(event_handler);
//		node.setOnMouseClicked(weak_event_handler);

		
		node.setOnMouseClicked(event -> {
			boolean found = false;
			if(!rootIsSelected()) {
				// Get the selected test by iterating over the list of test results for the one that has a test with the name and group name of the selected test. 
				TreeItem<String> selected = resultTreeView.getSelectionModel().getSelectedItem();

				String parentName = selected.getParent().getValue().toString();
				String selectedName = selected.getValue().toString();
				for(TestResult r : resultCollection) {
					if(r.getTest().getTestGroupName().equalsIgnoreCase(parentName) == true &&
					   r.getTest().getTestName().equalsIgnoreCase(selectedName) == true) {
						// i.e it is neither the top root, nor a parent. 
						found = true;
						selectedTest = r;
						updateStepsVBox();
					}
					// if parent is the root, then we have selected a test group and everything should be cleared.
				}
			}
			if(found == false) {
				// clear the 
			}
		});
		
		
	}


	private void updateStepsVBox() {
		testStepVBox.getChildren().clear();
		try {
			if (selectedTest != null) {
				ArrayList<Button> testStepsList = getStepsAsButtonList();
				testStepVBox.getChildren().addAll(testStepsList);
			}
		} catch (Exception e) {
			System.out.println("Could not load test steps from selected test..");
		}

	}
	
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
				for (int i = 0; i < testStepVBox.getChildren().size(); i++) {
					if (testStepVBox.getChildren().get(i).equals(btn)) {
						updateSelectedStepInformation(selectedTest.getStepResults().get(i), selectedTest.getTest().getTestSteps().get(i).getMainAction().getActionType());
					}
				}
			});
			btn.setPrefWidth(testStepVBox.getPrefWidth() - 8);
			btn.setPrefHeight(testStepVBox.getPrefWidth() - 8);
			btn.setMinWidth(testStepVBox.getPrefWidth() - 8);
			btn.setMinHeight(testStepVBox.getPrefWidth() - 8);

			list.add(btn);
		}
		return list;
	}
	
	
	private void updateSelectedStepInformation(TestStepResult testStepResult, EActionType actionType) {
		
		// SET ALL IMAGES,
		
		URL contextURL = FileUtility.getImageUrlFromPath(testStepResult.getOriginalContextImage().getFullImagePath());
		URL newContextURL = FileUtility.getImageUrlFromPath(testStepResult.getMatchedContextImage().getFullImagePath());
		
		baselineImageView.setImage(new Image(contextURL.toString()));
		newContextImageView.setImage(new Image(newContextURL.toString()));

	
		
		// SET ON UPDATE BASELINE BUTTON CLICKED, WITH REFERENCES TO NEW IMAGES.
		//TODO::
		
		// DISPLAY THE ACTION_TYPE.
		//TODO::
		
	}
	
	
	@FXML 
	AnchorPane currentContextPane, newContextPane;
	
	private void drawTargetOnImageView(AnchorPane imagePane, TestImage target) {
		
		
		// Calculate Scale factor 
		ImageView imgView = (ImageView) imagePane.getChildrenUnmodifiable().get(0); // This is the ImageView.
		double xScale = imgView.getFitWidth() / target.getImageWidth();
		double yScale = imgView.getFitHeight() / target.getImageHeight();
		
		// Calculate new position and size with the scale factor.
		Rectangle rect = new Rectangle(target.getCoordinates().getX() * xScale,
									   target.getCoordinates().getY() * yScale,
									   target.getImageWidth()  * xScale,
									   target.getImageHeight() * yScale);
		
		
		rect.setStrokeWidth(2);
		rect.setStroke(Paint.valueOf("red"));
		rect.setFill(Paint.valueOf("#1e92ff00"));
		imagePane.getChildren().add(rect);
	}

	
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
	
	


	private boolean rootIsSelected() {
		try {
			TreeItem<String> selected = resultTreeView.getSelectionModel().getSelectedItem();
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

    
	
}
