package examensarbete.model.test;

import java.awt.AWTException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

import examensarbete.model.action.ActionBase;
import examensarbete.model.action.ChromeWebAction;

@JsonRootName("Test")
public class TestImpl {

//	@Override
//	public List<TestStep> getTestSteps() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void saveTest() {
//		// TODO Auto-generated method stub
//		// The saving is done 1 level above this one, and is therefore not needed at this point unless we decide to divide different parts of a test to be saved in several files.
//	}
	
	
	
	private String testName;
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	
	private ArrayList<TestStepImpl> steps = new ArrayList<TestStepImpl>();
	public ArrayList<TestStepImpl> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<TestStepImpl> steps) {
		this.steps = steps;
	}
	
	public TestImpl(String testName) {
		this.setTestName(testName);
	}
	
	public TestImpl() {
		
	}
	
	/**
	 * Not in a constructor for the simple reason of making it easy to convert to and from json.
	 */
	public void initializeTest() {
		ChromeWebAction browser;
		try {
			browser = new ChromeWebAction();
			browser.actionSetup();
			TestStepImpl firstStep = new TestStepImpl(browser);
			steps.add(firstStep);
		} catch (AWTException e) {
			System.out.println("Error when creating browser action.");
			System.out.println(e.getMessage());
		}
	}
	
	
	public void addTestStep(ActionBase action) {
		TestStepImpl step = new TestStepImpl(action);
		steps.add(step);
	}

	
	public boolean runTest() {
		boolean passed = true;
		for(TestStepImpl step : steps) {
			passed = step.performTestStep(); 
		}
		return passed;
	}

	public void cleanup() {
		ChromeWebAction cwa = (ChromeWebAction)steps.get(0).getMainAction();
		cwa.closeBrowser();
	}

	
}
