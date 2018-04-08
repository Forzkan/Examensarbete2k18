package examensarbete.model.test;

import java.awt.AWTException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import examensarbete.model.action.ActionBase;
import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.action.EActionType;
import examensarbete.model.utility.WaitHandler;

@JsonRootName("Test")
public class TestImpl implements Test{

// This is not included in this class because "Test" is the top level object which wraps around everything else when saving to json.
// Look instead to the "TestHandler" which can save all tests, or a single on. It is not called "Controller" to not confuse it with JavaFX controllers.
// Could perhaps be renamed to TestSaver or similar.	
	
//	@Override
//	public void saveTest() {
//		// TODO Auto-generated method stub
//		// The saving is done 1 level above this one, and is therefore not needed at this point unless we decide to divide different parts of a test to be saved in several files.
//	}
	
	
	// VARIABLES
	private String testName;	
	private String testGroupName;
	private ArrayList<TestStep> testSteps = new ArrayList<TestStep>();	
	
	private final int waitTimeBetweenSteps = 1000;
	
	
	// GETTERS AND SETTERS FOR SERIALIZABLE VARIABLES (JSON).
	@Override
	public String getTestGroupName() {
		return testGroupName;
	}
	
	@Override
	public void setTestGroupName(String testGroupName) {
		this.testGroupName = testGroupName;
	}
	
	
	@Override
	public String getTestName() {
		return testName;
	}

	@Override
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	
	@Override
	public ArrayList<TestStep> getTestSteps() {
		return testSteps;
	}

	@Override
	public void setTestSteps(ArrayList<TestStep> steps) {
		this.testSteps = steps;
	}
	

	
	
	/**
	 * All tests have a browser as action 1(index 0) by default.
	 */
	@Override
	public void initializeTest() {
		ChromeWebAction browser;
		try {
			browser = new ChromeWebAction();
			browser.actionSetup();
			TestStepImpl firstStep = new TestStepImpl(browser);
			testSteps.add(firstStep);
		} catch (AWTException e) {
			System.out.println("Error when creating browser action.");
			System.out.println(e.getMessage());
		}
	}
	
	
	@Override
	public void addTestStep(ActionBase action) {
		TestStepImpl step = new TestStepImpl(action);
		step.takeScreenshot((ChromeWebAction)testSteps.get(0).getMainAction(), testGroupName, testName);
		testSteps.add(step);
	}

	
	@Override
	public boolean runTest() {
		boolean passed = true;
		for(TestStep step : testSteps) {
			
			if(step.getMainAction().getActionType() == EActionType.IMAGESNAP) {
				//step.takeScreenshot((ChromeWebAction)testSteps.get(0).getMainAction(), testGroupName, testName);
				// TODO:: do something with the image. // Should we do this for all types of actions?
				// Maybe take the image at different times depending on what type of action we're dealing with?
			}
			passed = step.performTestStep(); 
			WaitHandler.waitForMilliseconds(waitTimeBetweenSteps);

			if(passed == false) {
				return false;
			}
		}
		return passed;
	}

	@Override
	public void cleanup() {
		ChromeWebAction cwa = (ChromeWebAction)testSteps.get(0).getMainAction();
		cwa.closeBrowser();
	}

	@JsonIgnore
	public ChromeWebAction getChromeWebAction() {
		return (ChromeWebAction)testSteps.get(0).getMainAction();
	}
	
	// CONSTRUCTORS
	public TestImpl(String testGroup, String testName) {
		setTestName(testName);
		setTestGroupName(testGroup);
	}
	
	public TestImpl() {}
	
}
