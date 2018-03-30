package examensarbete.model.test;

import java.util.List;

public interface Test {
	List<TestStep> getTestSteps();
	void saveTest();
}
