package examensarbete.model.test;

import java.util.ArrayList;
import java.util.List;

public class TestResultImpl implements TestResult {

	private List<TestStepResult> testStepResults = new ArrayList<TestStepResult>();
	
	private Test test;
	
	public TestResultImpl(Test test) {
		this.test = test;
	}
	
	@Override
	public void addStepResult(TestStepResult testStepResult) {
		// TODO Auto-generated method stub
		testStepResults.add(testStepResult);
	}
	
	@Override
	public MatchType getLatestMatchResult() {
		return testStepResults.get(testStepResults.size()-1).getTestStepMatchType();
	}

	@Override
	public Test getTest() {
		return test;
	}

	@Override
	public List<TestStepResult> getStepResults() {
		return testStepResults;
	}

}
