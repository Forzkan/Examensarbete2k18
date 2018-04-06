package examensarbete.model.test;

import java.util.ArrayList;

import examensarbete.model.action.ActionBase;
import examensarbete.model.action.ChromeWebAction;

//import java.util.List;

public interface TestStep {

	ActionBase getMainAction();
	void setMainAction(ActionBase mainAction);

	TestImage getTestStepContextImage();
	void setTestStepContextImage(TestImage testStepContextImage);

	TestImage getTestStepTargetImage();
	void setTestStepTargetImage(TestImage testStepTargetImage);

	
	boolean performTestStep();
	void takeScreenshot(ChromeWebAction chrome, String groupName, String testName);
	ArrayList<String> getListOfContextInformation();
	

//	TestImage getTestStepTargetImage();
//	void setTestStepTargetImage(TestImage testImage);
//	TestImage getTestStepContextImage();
//	void setTestStepContextImage(TestImage testImage);
//	//List<TestAction> getTestStepActions();
//	boolean performTestStep();
	
	
}
