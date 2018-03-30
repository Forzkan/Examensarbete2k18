package examensarbete.model.action;

import java.awt.AWTException;

public class KeyPressAction extends ActionBase{

	
	public KeyPressAction() throws AWTException{
		super();
		this.actionType = EActionType.KEYPRESS;
	}
	
	@Override
	public EActionType getType() {
		return this.actionType;
	}
//	private String 

	// TODO:: TO BE IMPLEMENTED.
	@Override
	public boolean performAction() {
		return false;
	}


	@Override
	public void actionSetup() {

		
	}

}
