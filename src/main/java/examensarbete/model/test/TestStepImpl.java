package examensarbete.model.test;

import java.io.File;
import java.util.ArrayList;

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
		
		if(mainAction.getActionType() == EActionType.IMAGESNAP) {		}
		
			// DO TEMPLATE,
				// IF TEMPLATE OK AND IN SAME PLACE, GO ON LIKE NOTHING EEELSE MATTEEEEEEERS
			
				// IF TEMPLATE OK BUT HAS DO THE SWAP IN POSITION, NOTIFY USER ( IT IS CONSIDERED A VALID CHANGE)
			
				// ELSE IF NOT OK, TAKE IMAGE FROM THE BOUNDS OF THE TARGET IMAGE TO CREATE A NEW ONE TO BE USED FOR GCV COMPARISONS.
					// IF VALID CHANGE,
						// STORE THE NEW IMAGE IN SOME TEST RESULTS (?),
						// CLICK ON THE PREVIOUS USER-DEFINED CLICK-COORDINATES AND CONTINUE.
					// ELSE IF INVALID CHANGE, RETURN FALSE.
					
		
		

		return mainAction.performAction();
	}
	
	
	@Override
	public void takeScreenshot(ChromeWebAction chrome, String groupName, String testName) {
		String path = chrome.takeBrowserScreenshot(FileUtility.createUniqueContextImageFilePath(groupName, testName));		
		testStepContextImage = new TestImageImpl();
		testStepContextImage.setImagePath(path);
	}
	
	
	
	@JsonIgnore
	public ArrayList<String> getListOfContextInformation(){
		ArrayList<String> ret = new ArrayList<String>();
		if(testStepContextImage != null) {
			ret.add("Context Image: " + testStepContextImage.getImagePath());
			if(testStepContextImage.getCoordinates() != null) {
				ret.add("X Coordinate: " + testStepContextImage.getCoordinates().x);
				ret.add("Y Coordinate: " + testStepContextImage.getCoordinates().y);
			}else {
				ret.add("X Coordinate: N/A");
				ret.add("Y Coordinate: N/A");
			}
			ret.add("Image Width: " + testStepContextImage.getImageWidth());
			ret.add("Image Height: " + testStepContextImage.getImageHeight());
		}
		return ret;
	}
	

	
	public TestStepImpl() {}
	
	public TestStepImpl(ActionBase action) {
		this.setMainAction(action);
	}

}
