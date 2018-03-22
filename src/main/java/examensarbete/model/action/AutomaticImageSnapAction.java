package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

public class AutomaticImageSnapAction extends ActionRobotBase implements IAction{

	private Rectangle imageBounds;
	private Image image;
	private String imagePath;
	
	public AutomaticImageSnapAction(Rectangle imageBounds) throws AWTException {
		super(EActionType.AUTOIMAGESNAP);
		this.imageBounds = imageBounds;
	}

	@Override
	public void actionSetup() {
		try {
			imagePath = this.takeScreenShot("D://", "AutoSnapImage", imageBounds);
			image = this.getImageFromPath(imagePath);
		} catch (AWTException | IOException e) {
			System.out.println(e.getMessage());
		}	
	}

	@Override
	public boolean performAction() {
		// With the imageBounds, snap the new image.
		return false;
	}

	public Image getImage() {
		return image;
	}

	
	
	
	
}
