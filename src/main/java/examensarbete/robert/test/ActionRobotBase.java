package examensarbete.robert.test;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Robot;

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
	
	
	public Image takeScreenShot() {
		

		return null;
	}
	
}
