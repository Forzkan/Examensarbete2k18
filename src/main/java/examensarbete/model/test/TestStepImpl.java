package examensarbete.model.test;

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
	
	
	
	
	
	
	private IAction mainAction;
	public IAction getMainAction() {
		return mainAction;
	}
	public void setMainAction(IAction mainAction) {
		this.mainAction = mainAction;
	}	
	
	public TestStepImpl(IAction action) {
		this.setMainAction(action);
	}
	
	
	
	@Override
	public void performTestStep() {
		// TODO:: PERFORM DIFFERENT THE MAIN ACTION, AND HANDLE THE OUTCOME OF IT.
	}




}
