package examensarbete.javafx.controller;

import examensarbete.model.test.TestGroup;
import examensarbete.model.test.TestHandler;
import examensarbete.model.test.TestImpl;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewTestController {

	private TestHandler testHandler;
	private WorkAreaController waController;
	
	@FXML
	private CheckBox useExistingTGCheckBox;
	
	@FXML
	private TextField newTestGroupTextField, testNameTextField;
	
	@FXML
	private ChoiceBox<String> testGroupChoiceBox;
	
	
	private Stage stage;
	
	public NewTestController(TestHandler testHandler, WorkAreaController waController) {
		this.testHandler = testHandler;
		this.waController = waController;
	}
	
	@FXML
	public void initialize() {
		populateChoiceBox();
	}
	
	
	@FXML
	private void createNewTestPressed() {
		String groupName = useExistingTGCheckBox.isSelected() ? 
						   testGroupChoiceBox.getValue().toString() : newTestGroupTextField.getText();	
		
						   //TODO :: PERFORM SOM VALIDATION ON THE VALUES.
		String testName = testNameTextField.getText();
		TestImpl test = new TestImpl(testName);
		test.initializeTest();
		testHandler.saveNewTest(testName, groupName, test);
		waController.treeViewLoader(); // reload treeview with the new test.
		closeStage();
	}
	
	@FXML
	private void onClickUseExistingTGCheckBox() {
		if(useExistingTGCheckBox.isSelected()) {
			newTestGroupTextField.setDisable(true);
		}else {
			newTestGroupTextField.setDisable(false);
		}
	}
	
	
	
	private void closeStage() {
		try {
			stage = (Stage) testNameTextField.getScene().getWindow();
			stage.close();
		}catch(Exception e){
			System.out.println("Could either not fetch the stage, or not close it.");
			System.out.println(e.getMessage());
		}
	}
	
	private void populateChoiceBox() {
		for(TestGroup tg : testHandler.getTestList()) {
			boolean isAdded = false;
			for(String s : testGroupChoiceBox.getItems()) {
				if(s.equals(tg.getGroupName())) {
					isAdded = true;
					break;
				}
			}
			if(isAdded == false) {
				testGroupChoiceBox.getItems().add(tg.getGroupName());
			}
		}
	}
	
	
	
}
