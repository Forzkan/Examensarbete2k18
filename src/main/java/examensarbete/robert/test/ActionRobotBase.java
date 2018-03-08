package examensarbete.robert.test;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.api.gax.rpc.UnimplementedException;

public class ActionRobotBase implements IAction{

	protected Robot robot;
	
	public ActionRobotBase() throws AWTException {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("Could not initialize ClickRobot");
			System.out.println(e.getMessage());
			throw new AWTException(e.getMessage());
		}
	}
	
	
	public Image takeScreenShot() throws AWTException, IOException {
	    Robot robot = new Robot();
	    int x = 500;
	    int y = 500;
	    int width = 400;
	    int height = 200;
	    Rectangle area = new Rectangle(x, y, width, height);
	    BufferedImage bufferedImage = robot.createScreenCapture(area);

//	    area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	    bufferedImage = robot.createScreenCapture(area);
	    ImageIO.write(bufferedImage, "JPG", new File("C:\\_dev\\cropScreenShot.jpg"));
	    
	    
		return null;
	}
	
}
