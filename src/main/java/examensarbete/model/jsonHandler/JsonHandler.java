package examensarbete.model.jsonHandler;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import examensarbete.google.cloud.vision.GCVImageResult;


public class JsonHandler {

	public boolean saveGCVImageResult(String dirPath, String fileName, GCVImageResult result) {
//		s mapper = new ObjectMapper();
		
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert object to JSON string and save into a file directly
			mapper.writeValue(new File(dirPath + "\\" + fileName +".json"), result);
			return true;
//			// Convert object to JSON string
//			String jsonInString = mapper.writeValueAsString(result);
//			System.out.println(jsonInString);
//
//			// Convert object to JSON string and pretty print
//			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
//			System.out.println(jsonInString);

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
//			// Convert JSON string to Object
//			String jsonInString = "{\"name\":\"mkyong\",\"salary\":7500,\"skills\":[\"java\",\"python\"]}";
//			GCVImageResult result1 = mapper.readValue(jsonInString, GCVImageResult.class);
//			System.out.println(result1);

			//Pretty print
//			String result2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result1);
//			System.out.println(result2);
			
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
}
