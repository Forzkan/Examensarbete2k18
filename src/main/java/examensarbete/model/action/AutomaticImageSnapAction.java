package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.io.IOException;

import examensarbete.model.test.TestImageImpl;
import examensarbete.model.utility.FileUtility;


public class AutomaticImageSnapAction extends ActionBase {

	
	private TestImageImpl snapImage;
	private TestImageImpl fullPageImage;

	public TestImageImpl getSnapImage() {
		return snapImage;
	}
	public void setSnapImage(TestImageImpl snapImage) {
		this.snapImage = snapImage;
	}

	public TestImageImpl getFullPageImage() {
		return fullPageImage;
	}
	public void setFullPageImage(TestImageImpl fullPageImage) {
		this.fullPageImage = fullPageImage;
	}
	
	@Override
	public EActionType getType() {
		return this.actionType;
	}

	public String getSnapImagePath() {
		return snapImage.getImagePath();
	}

	public AutomaticImageSnapAction() {
		super();
		this.actionType = EActionType.AUTOIMAGESNAP;
	}

	String groupName, testName;
	
	public AutomaticImageSnapAction(Rectangle imageBounds, String groupName, String testName) throws AWTException {
		super(EActionType.AUTOIMAGESNAP);
		snapImage.getPosition().x = imageBounds.x;
		snapImage.getPosition().y = imageBounds.y;
		this.groupName = groupName;
		this.testName = testName;
//		snapImage.setImageWidth(imageBounds.width);
//		snapImage.setImageHeight(imageBounds.height);
	}

	@Override
	public void actionSetup() {
		try {																													// TODO:: GIVE PROPER NAME.
			String imagePath = this.takeScreenShot(FileUtility.createUniqueSnapImageFilePath(groupName, testName), snapImage.getBounds());
			snapImage.setImagePath(imagePath);
		} catch (AWTException | IOException e) {
			System.out.println(e.getMessage());
		}	
	}

	
	@Override
	public boolean performAction() {
		// With the imageBounds, click the new image.
		try {
			ClickWithinBoundsAction clickAction = new ClickWithinBoundsAction(snapImage.getBounds());
			if(clickAction.performAction()) {
				return true;
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	
//	public Image getImage() {
//		return image;
//	}

	
	
	
	
}
