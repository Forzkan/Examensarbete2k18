package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.collections.ObservableList;
import javafx.stage.Screen;

public class ActionRobotBase{

	protected Robot robot;
	protected EActionType actionType;
	
	public ActionRobotBase(EActionType actionType) throws AWTException {
		try {
			robot = new Robot();
			this.actionType = actionType;
		} catch (AWTException e) {
			System.out.println("Could not initialize ClickRobot");
			System.out.println(e.getMessage());
			throw new AWTException(e.getMessage());
		}
	}

	
	protected String takeScreenShot(String path, String imageName, Rectangle bounds) throws AWTException, IOException {
		Robot robot = new Robot();
		Rectangle area = bounds.getBounds();
		BufferedImage bufferedImage = robot.createScreenCapture(area);
		bufferedImage = robot.createScreenCapture(area);
		String filePathAndName = path+"/"+imageName+".png";
		ImageIO.write(bufferedImage, "PNG", new File(filePathAndName));
		return filePathAndName;
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
	
	
	protected ObservableList<Screen> getAllScreens(){
		return Screen.getScreens();
	}
	
	
}
