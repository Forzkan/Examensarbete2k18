package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Robot;


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

	
	protected ObservableList<Screen> getAllScreens(){
		return Screen.getScreens();
	}
	
	
}
