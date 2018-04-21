package examensarbete.model.test;

import java.io.File;
import java.util.ArrayList;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import examensarbete.model.utility.FileUtility;
import examensarbete.model.utility.json.JsonMapper;


public class TestHandler {

	private final JsonMapper json = new JsonMapper();
	private ArrayList<TestGroup> testGroups = new ArrayList<TestGroup>();
	
	public ArrayList<TestGroup> getTestList() {
		return testGroups;
	}
	
	
	public void saveNewTest(String testName,String groupName, TestImpl test) {
		// SAVING THE NEWLY CREATED TEST.
		saveTest(new TestGroup(testName, groupName, test));
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
		testGroups.clear();
		createTestCollectionList();
	}
	
	
	private void createTestCollectionList() {
		File directory = new File(FileUtility.getProjectRoot() + "\\TEST CASES");
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
