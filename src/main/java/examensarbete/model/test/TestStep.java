package examensarbete.model.test;

import java.awt.AWTException;
import java.io.IOException;
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

	
	boolean performTestStep() throws IOException, AWTException;
	void takeScreenshot(String groupName, String testName);
	ArrayList<String> getListOfContextInformation();
	void setChrome(ChromeWebAction chrome);
	ChromeWebAction getChrome();
	

//	TestImage getTestStepTargetImage();
//	void setTestStepTargetImage(TestImage testImage);
//	TestImage getTestStepContextImage();
//	void setTestStepContextImage(TestImage testImage);
//	//List<TestAction> getTestStepActions();
//	boolean performTestStep();
	
	
}
