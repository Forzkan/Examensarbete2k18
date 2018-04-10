package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.collections.ObservableList;
import javafx.stage.Screen;

public abstract class ActionBase implements IAction{

	protected Robot robot;
	protected EActionType actionType;
	
	public EActionType getActionType() {
		return actionType;
	}

	public void setActionType(EActionType actionType) {
		this.actionType = actionType;
	}


	public ActionBase() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("Could not initialize ClickRobot from the loaded test.");
			System.out.println(e.getMessage());
		}
	}

	public ActionBase(EActionType actionType) {
		try {
			robot = new Robot();
			this.actionType = actionType;
		} catch (AWTException e) {
			System.out.println("Could not initialize ClickRobot");
			System.out.println(e.getMessage());
//			throw new AWTException(e.getMessage());
		}
	}
	
	
	
	public String takeScreenShot(String pathAndNameWithNoEnd, Rectangle bounds) throws AWTException, IOException {
		Robot robot = new Robot();
		Rectangle area = bounds.getBounds();
		BufferedImage bufferedImage = robot.createScreenCapture(area);
		bufferedImage = robot.createScreenCapture(area);
		String filePathAndName = pathAndNameWithNoEnd+".png";
		File image = new File(filePathAndName);
		ImageIO.write(bufferedImage, "PNG", image);
		return image.getAbsolutePath();
	}
	
	
	protected BufferedImage getImageFromPath(String path) {
		BufferedImage img = null;
		try 
		{
		    img = ImageIO.read(new File("path"));
		} 
		catch (IOException e) 
		{
		    System.out.println(e.getMessage());
		}
		return img;
	}

	protected void performClick(int x, int y) {
		System.out.println("Click on position x: " + x + " y: " + y);
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	protected ObservableList<Screen> getAllScreens(){
		return Screen.getScreens();
	}



	@Override
	public abstract void actionSetup();
	
	@Override
	public abstract boolean performAction();
	
	
}
