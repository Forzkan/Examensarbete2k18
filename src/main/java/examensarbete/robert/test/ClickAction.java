package examensarbete.robert.test;

import java.awt.AWTException;
import java.awt.event.InputEvent;

public class ClickAction extends ActionRobotBase{

	private int x, y;	
	
	public ClickAction(int x, int y) throws AWTException {
		super();
		this.x = x;
		this.y = y;
	}
	
	
	public void performAction() {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	
	
	
	
	
}
