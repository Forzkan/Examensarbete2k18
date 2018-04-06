package examensarbete.model.test;

import java.util.ArrayList;
import java.util.List;

import examensarbete.model.action.ActionBase;

public interface Test {
	
	void initializeTest();
	boolean runTest();
	void cleanup();

	void addTestStep(ActionBase action);
	List<TestStep> getTestSteps();	
	void setTestSteps(ArrayList<TestStep> steps);
	
	String getTestName();
	void setTestName(String testName);

	String getTestGroupName();
	void setTestGroupName(String testGroupName);


	
	
//	void saveTest();
}
