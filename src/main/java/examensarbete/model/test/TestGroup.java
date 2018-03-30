package examensarbete.model.test;

import javafx.beans.property.SimpleStringProperty;

public class TestGroup {
	private final SimpleStringProperty testName;
	private final SimpleStringProperty groupName;
	private TestImpl test;
	
	
	public TestGroup(String testName, String collectionName, TestImpl test) {
		this.testName = new SimpleStringProperty(testName);
		this.groupName = new SimpleStringProperty(collectionName);
		this.setTest(test);
	}

	public String getTestName() {
		return testName.get();
	}

	public String getGroupName() {
		return groupName.get();
	}

	public void setTestName(String testName) {
		this.testName.set(testName);
	}

	public void setGroupName(String collectionName) {
		this.groupName.set(collectionName);
	}

	public TestImpl getTest() {
		return test;
	}

	public void setTest(TestImpl test) {
		this.test = test;
	}
}
