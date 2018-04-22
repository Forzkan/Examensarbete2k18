package examensarbete.model.test;

import java.util.List;

public class TestResultImpl implements TestResult {

	private List<TestStepResult> testStepResults;
	
	@Override
	public void addStepResult(TestStepResult testStepResult) {
		// TODO Auto-generated method stub
		testStepResults.add(testStepResult);
	}

}
