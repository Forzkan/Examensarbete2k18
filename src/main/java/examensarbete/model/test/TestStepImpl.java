package examensarbete.model.test;

import examensarbete.model.action.ActionBase;
import examensarbete.model.action.IAction;

public class TestStepImpl implements TestStep{

	@Override
	public TestImage getTestStepTargetImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTestStepTargetImage(TestImage testImage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TestImage getTestStepContextImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTestStepContextImage(TestImage testImage) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public TestStepImpl() {
		
	}
	
	
	private ActionBase mainAction;
	public ActionBase getMainAction() {
		return mainAction;
	}
	public void setMainAction(ActionBase mainAction) {
		this.mainAction = mainAction;
	}	
	
	public TestStepImpl(ActionBase action) {
		this.setMainAction(action);
	}
	
	
	
	@Override
	public boolean performTestStep() {
		// TODO:: PERFORM THE MAIN ACTION, AND HANDLE THE OUTCOME OF IT.
		return mainAction.performAction();
	}




}
