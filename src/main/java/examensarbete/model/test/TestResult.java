package examensarbete.model.test;

import java.util.List;

public interface TestResult {

	public Test getTest();
	public void addStepResult(TestStepResult testStepResult);
	public List<TestStepResult> getStepResults();
	public MatchType getLatestMatchResult();
}
