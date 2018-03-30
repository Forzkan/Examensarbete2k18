package examensarbete.model.test;

import java.io.File;
import java.util.ArrayList;

import examensarbete.model.utility.FileUtility;
import examensarbete.model.utility.json.JsonHandler;
import examensarbete2k18.model.properties.PropertiesHandler;
import examensarbete2k18.model.properties.TTProperties;


public class TestHandler {

	private final JsonHandler json = new JsonHandler();
	private ArrayList<TestGroup> testGroups = new ArrayList<TestGroup>();
	
	public ArrayList<TestGroup> getTestList() {
		return testGroups;
	}
	
	
	public void addTest(String groupName, String testName, TestImpl test) {
		testGroups.add(new TestGroup(groupName, testName, test));
	}
	
	
	public TestImpl getSelectedTest(String collectionName, String testName) throws NullPointerException {
		for(TestGroup tc : testGroups) {
			if( tc.getGroupName().equals(collectionName) && tc.getTestName().equals(testName)) {
				return tc.getTest();
			}
		}
		throw new NullPointerException("Could not find a test called: " + testName + " in collection: " + collectionName);
	}
	
	
	
	public void saveTests() {
		for(TestGroup tg : testGroups) {
			saveTest(tg);
		}
	}
	
	public void saveTest(TestGroup tg) {
		json.saveTest(tg.getGroupName(), tg.getTestName(), tg.getTest());
	}

	
	
	
	// LOAD EXISTING TESTS.
	
	public void loadSavedTests() {
		createTestCollectionList();
	}
	
	
	private void createTestCollectionList() {
		File directory = new File(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()));
		if(directory.exists() && directory.isDirectory()) {
			for(File f : directory.listFiles()) {
				if(f.isDirectory()) {
					String collectionName = f.getName();
					for(File test : f.listFiles()) {
						if(test.isFile() && !test.isDirectory()) {
							if(FileUtility.getFileExtension(test).equals("json")) {
								testGroups.add(new TestGroup(FileUtility.getNameWithoutJsonEnd(test), 
													collectionName, 
													json.readTest(f.getName(), FileUtility.getNameWithoutJsonEnd(test))));
							}
						}
					}
				}
			}
		}
	}


}
