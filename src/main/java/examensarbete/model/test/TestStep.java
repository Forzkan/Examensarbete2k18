package examensarbete.model.test;

//import java.util.List;

public interface TestStep {
	
	TestImage getTestStepTargetImage();
	void setTestStepTargetImage(TestImage testImage);
	TestImage getTestStepContextImage();
	void setTestStepContextImage(TestImage testImage);
	//List<TestAction> getTestStepActions();
	boolean performTestStep();
}
