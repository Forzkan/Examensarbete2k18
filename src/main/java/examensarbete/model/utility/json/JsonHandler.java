package examensarbete.model.utility.json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import examensarbete.google.cloud.vision.GCVImageResult;
import examensarbete.model.test.TestImpl;
import examensarbete2k18.model.properties.PropertiesHandler;
import examensarbete2k18.model.properties.TTProperties;


public class JsonHandler {

	public boolean saveGCVImageResult(String dirPath, String fileName, GCVImageResult result) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert object to JSON string and save into a file directly
			mapper.writeValue(new File(dirPath + "\\" + fileName +".json"), result);
			return true;

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public GCVImageResult readGCVImageResult(String jsonPath) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert JSON string from file to Object
			GCVImageResult result = mapper.readValue(new File(jsonPath), GCVImageResult.class);
			System.out.println(result);
			return result;
			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	
	
	private String createGroupPath(String groupName) {
		String dirPath = PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString());
		return dirPath + "\\" + groupName;
	}
	
	private String createFullTestPath(String groupName, String testName) {
		String dirPath = PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString());
		return dirPath + "\\" + groupName + "\\" +testName +".json";
	}
	
	
	
	

	public TestImpl readTest(String groupName, String testName) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert JSON string from file to Object
			TestImpl test = mapper.readValue(new File(createFullTestPath(groupName, testName)), TestImpl.class);
			return test;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	public void saveTest(String groupName, String testName, TestImpl test) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// check if we should create directory first.
			File groupDir = new File(createGroupPath(groupName));
			if(!groupDir.exists() || !groupDir.isDirectory()) {
				groupDir.mkdir();
			}
			mapper.writeValue(new File(createFullTestPath(groupName, testName)), test);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	
	
	
	
}
