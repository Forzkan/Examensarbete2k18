package examensarbete.model.action;

import java.awt.AWTException;

public class KeyPressAction extends ActionRobotBase implements IAction {

	public KeyPressAction() throws AWTException {
		super(EActionType.KEYPRESS);
	}

//	private String 

	@Override
	public boolean performAction() {
		return false;
	}


	@Override
	public void actionSetup() {

		
	}

}
