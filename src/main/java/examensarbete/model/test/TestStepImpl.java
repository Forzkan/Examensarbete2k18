package examensarbete.model.test;

import com.fasterxml.jackson.annotation.JsonIgnore;

import examensarbete.model.action.ActionBase;
import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.action.EActionType;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.utility.FileUtility;


public class TestStepImpl implements TestStep{

	private TestImage testStepContextImage;
	private ActionBase mainAction;
	

	@Override
	public ActionBase getMainAction() {
		return mainAction;
	}
	
	@Override
	public void setMainAction(ActionBase mainAction) {
		this.mainAction = mainAction;
	}	
	
	@Override
	public TestImage getTestStepContextImage() {
		return testStepContextImage;
	}
	
	@Override
	public void setTestStepContextImage(TestImage testStepContextImage) {
		this.testStepContextImage = testStepContextImage;
	}
	
	@JsonIgnore
	@Override
	public TestImage getTestStepTargetImage() {
		if(mainAction.getActionType() == EActionType.IMAGESNAP) {
			SnapImageAction a = (SnapImageAction)mainAction;
			return a.getTargetImage();
		}
		return null;
	}
	
	@JsonIgnore
	@Override
	public void setTestStepTargetImage(TestImage testStepTargetImage) {
		if(mainAction.getActionType() == EActionType.IMAGESNAP) {
			SnapImageAction a = (SnapImageAction)mainAction;
			a.setTargetImage(testStepTargetImage);
		}
	}
	
	
	
	@Override
	public boolean performTestStep() {
		// TODO:: PERFORM THE MAIN ACTION, AND HANDLE THE OUTCOME OF IT.
		
		if(mainAction.getActionType() == EActionType.IMAGESNAP) {
			// DO TEMPLATE,
			
			// IF TEMPLATE OK 
				// PERFORM ACTION. 
			
			
		}
		return mainAction.performAction();
	}
	
	
	@Override
	public void takeScreenshot(ChromeWebAction chrome, String groupName, String testName) {
		String path = chrome.takeBrowserScreenshot(FileUtility.createUniqueContextImageFilePath(groupName, testName));		
		testStepContextImage = new TestImageImpl();
		testStepContextImage.setImagePath(path);
	}
	

	
	public TestStepImpl() {}
	
	public TestStepImpl(ActionBase action) {
		this.setMainAction(action);
	}

}
