package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.io.IOException;

import examensarbete.model.test.TestImageImpl;
import examensarbete2k18.model.properties.PropertiesHandler;
import examensarbete2k18.model.properties.TTProperties;

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
		return snapImage.getPath();
	}

	public AutomaticImageSnapAction() {
		super();
		this.actionType = EActionType.AUTOIMAGESNAP;
	}

	
	public AutomaticImageSnapAction(Rectangle imageBounds) throws AWTException {
		super(EActionType.AUTOIMAGESNAP);
		snapImage.setX(imageBounds.x);
		snapImage.setY(imageBounds.y);
		snapImage.setWidth(imageBounds.width);
		snapImage.setHeight(imageBounds.height);
	}

	@Override
	public void actionSetup() {
		try {																													// TODO:: GIVE PROPER NAME.
			String imagePath = this.takeScreenShot( PropertiesHandler.properties.getProperty(TTProperties.IMAGE_DIRECTORY.toString()), "AutoSnapImage", snapImage.fetchTheBounds());
			snapImage.setPath(imagePath);
		} catch (AWTException | IOException e) {
			System.out.println(e.getMessage());
		}	
	}

	
	@Override
	public boolean performAction() {
		// With the imageBounds, click the new image.
		try {
			ClickWithinBoundsAction clickAction = new ClickWithinBoundsAction(snapImage.fetchTheBounds());
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
