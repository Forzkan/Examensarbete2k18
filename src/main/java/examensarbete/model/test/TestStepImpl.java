package examensarbete.model.test;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import examensarbete.google.cloud.vision.GCVComparator;
import examensarbete.google.cloud.vision.GCVConnector;
import examensarbete.model.action.ActionBase;
import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.action.EActionType;
import examensarbete.model.action.SnapImageAction;
import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import examensarbete.model.utility.FileUtility;
import examensarbete.model.utility.TestRunUtility;
import examensarbete.opencv.OpenCvController;
import examensarbete.model.test.MatchType;

public class TestStepImpl implements TestStep {

	private TestImage testStepContextImage;
	private ActionBase mainAction;

	@JsonIgnore
	private ChromeWebAction chrome;

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
		if (mainAction.getActionType() == EActionType.IMAGESNAP) {
			SnapImageAction a = (SnapImageAction) mainAction;
			return a.getTargetImage();
		}
		return null;
	}

	@JsonIgnore
	@Override
	public void setTestStepTargetImage(TestImage testStepTargetImage) {
		if (mainAction.getActionType() == EActionType.IMAGESNAP) {
			SnapImageAction a = (SnapImageAction) mainAction;
			a.setTargetImage(testStepTargetImage);
		}
	}

	@Override
	public TestStepResult performTestStep() throws IOException, AWTException {
		boolean actionOutcome = false;
		
		// TODO:: PERFORM THE MAIN ACTION, AND HANDLE THE OUTCOME OF IT.
		if (mainAction.getActionType() == EActionType.IMAGESNAP) {
			SnapImageAction snapImageAction = (SnapImageAction) mainAction;
			OpenCvController openCvController = new OpenCvController();
			MatchType matchResult = MatchType.NO_MATCH;

			// TAKE NEW IMAGE, IN SAME LOCATION.
			TestImage newTarget = new TestImageImpl();
			newTarget.setImagePath(TestRunUtility.takeNewTargetImage(snapImageAction.getTargetImage().getBounds(),
					PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + "\\tmpNewTargetForGCV")); // TODO:: Should probably create tmp folder
																				// to store such images in.
			// GET GCVImageResult FOR THE NEW IMAGE.
			newTarget.setImageGCVResults(GCVConnector.getGCVImageResult(newTarget.getFullImagePath()));
			// IF VALID CHANGE
			GCVComparator gcvComparator = new GCVComparator();
			MatchType gcvMatchType = gcvComparator.isValidChange(snapImageAction.getTargetImage().getImageGCVResults(), newTarget.getImageGCVResults());
			if (gcvComparator.isValidGCVMatchType(gcvMatchType)) {
//			if(false) {
				System.out.println("IS VALID GCV change.");
				SnapImageAction newSnapAction = new SnapImageAction();

				newTarget.setCoordinates(snapImageAction.getTargetImage().getCoordinates());
				newTarget.setCoordinateOffset(snapImageAction.getTargetImage().getCoordinateOffset());
				newTarget.setClickOffset(snapImageAction.getTargetImage().getClickOffset());
				newSnapAction.setTargetImage(newTarget);
				// PERFORM CLICK, AND SAVE THE IMAGE SO THAT THE USER CAN SEE WHAT IS CONSIDERED
				// A VALID CHANGE, AND UPDATE THE BASELINE IF THE USER WANTS TO DO SO.
				actionOutcome = newSnapAction.performAction();
			} else {
				matchResult = openCvController.runComparison(chrome.getNewFullScreenContextImage(),
						snapImageAction.getTargetImage());
				
				TestImage newLocationTestImage = openCvController.getResultTestImage();
				// Save image to filesystem..
				String filePathAndName = PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) +"/newTmpTemplateMatchImage.png";
				File image = new File(filePathAndName);
				ImageIO.write((BufferedImage)newLocationTestImage.getImage(), "PNG", image);
				newLocationTestImage.setImagePath(image.getAbsolutePath());
				newLocationTestImage.setImageGCVResults(GCVConnector.getGCVImageResult(newLocationTestImage.getFullImagePath()));
				
				if (matchResult == MatchType.MATCH) {
					if(gcvComparator.performTextMatch(snapImageAction.getTargetImage().getImageGCVResults(), newLocationTestImage.getImageGCVResults())) {
						System.out.println("TEMPLATE MATCHING FOUND THE IMAGE IN THE SAME LOCATION.");
						actionOutcome = mainAction.performAction();
					}else {
						actionOutcome = false;
					}

				} else if (matchResult == MatchType.LOCATION_CHANGED_MATCH) { // Should we verify this with the AI?
					if(gcvComparator.performTextMatch(snapImageAction.getTargetImage().getImageGCVResults(), newLocationTestImage.getImageGCVResults())){	
	//					newLocationTestImage.setCoordinateOffset(TestRunUtility.getOffset(chrome));
						System.out.println("TEMPLATE MATCH FOUND THE IMAGE IN A NEW LOCATION.");
						newLocationTestImage.setClickOffset(snapImageAction.getTargetImage().getClickOffset());
	
						snapImageAction.setTargetImage(newLocationTestImage);
	
						actionOutcome = snapImageAction.performAction();
					}else {
						actionOutcome = false;
					}

				}
			}

			// DO TEMPLATE,
			// IF TEMPLATE OK AND IN SAME PLACE, GO ON LIKE NOTHING EEELSE MATTEEEEEEERS

			// IF TEMPLATE OK BUT HAS DO THE SWAP IN POSITION, NOTIFY USER ( IT IS
			// CONSIDERED A VALID CHANGE)

			// ELSE IF NOT OK, TAKE IMAGE FROM THE BOUNDS OF THE TARGET IMAGE TO CREATE A
			// NEW ONE TO BE USED FOR GCV COMPARISONS.
			// IF VALID CHANGE,
			// STORE THE NEW IMAGE IN SOME TEST RESULTS (?),
			// CLICK ON THE PREVIOUS USER-DEFINED CLICK-COORDINATES AND CONTINUE.
			// ELSE IF INVALID CHANGE, RETURN FALSE.

		} else {
			actionOutcome = mainAction.performAction();
		}

		MatchType testStepMatchType = actionOutcome ? MatchType.MATCH : MatchType.NO_MATCH;
		
		return new TestStepResultImpl(testStepMatchType, new TestImageImpl(), new TestImageImpl(), new TestImageImpl(), new TestImageImpl() );
	}

	@Override
	public void takeScreenshot(String groupName, String testName) {
		String path = chrome.takeBrowserScreenshot(FileUtility.createUniqueContextImageFilePath(groupName, testName));
		testStepContextImage = new TestImageImpl();
		System.out.println("");
		testStepContextImage.setImagePath(path);
	}

	@JsonIgnore
	public ArrayList<String> getListOfContextInformation() {
		ArrayList<String> ret = new ArrayList<String>();
		if (testStepContextImage != null) {
			ret.add("Context Image: " + testStepContextImage.getFullImagePath());
			if (testStepContextImage.getImageScreenCoordinates() != null) {
				ret.add("X Coordinate: " + testStepContextImage.getImageScreenCoordinates().x);
				ret.add("Y Coordinate: " + testStepContextImage.getImageScreenCoordinates().y);
			} else {
				ret.add("X Coordinate: N/A");
				ret.add("Y Coordinate: N/A");
			}
			ret.add("Image Width: " + testStepContextImage.getImageWidth());
			ret.add("Image Height: " + testStepContextImage.getImageHeight());
		}
		return ret;
	}

	public TestStepImpl() {
	}

	public TestStepImpl(ActionBase action) {
		this.setMainAction(action);
	}

	@JsonIgnore
	@Override
	public ChromeWebAction getChrome() {
		return chrome;
	}

	@JsonIgnore
	@Override
	public void setChrome(ChromeWebAction chrome) {
		this.chrome = chrome;
	}

}
