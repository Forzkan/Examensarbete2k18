package old.or.not.in.use;

import java.awt.AWTException;

import examensarbete.model.action.ActionBase;
import examensarbete.model.action.EActionType;

public class KeyPressAction extends ActionBase{

	
	public KeyPressAction() throws AWTException{
		super();
		this.actionType = EActionType.KEYPRESS;
	}
	
	@Override
	public EActionType getActionType() {
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
