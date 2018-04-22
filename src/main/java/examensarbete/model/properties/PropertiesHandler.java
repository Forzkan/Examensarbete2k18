package examensarbete.model.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import examensarbete.model.utility.FileUtility;

public class PropertiesHandler {

	private final static String filename = "tt_properties";
	public final static Properties properties = new Properties();;
	
	
	public static void InitializePropertiesHandler() {
		if(configFileExist() == false) {
			createDefaultConfigFile();
		}else {
			loadPropertiesFile();
		}
	}
	
	private static void loadPropertiesFile() {
		InputStream input = null;
		try {
			input = new FileInputStream(filename);
			// load a properties file
			properties.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void saveConfigProperty(TTProperties ttProp, String value) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(filename);
			// set the properties value
			properties.setProperty(ttProp.toString(), value);
			// save properties to project root folder
			properties.store(output, null);
			System.out.println("Successfully stored the new property.");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	
	
	public static void saveConfigProperties(ArrayList<TTProperties> ttProperties, ArrayList<String> values) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(filename);
			// set the properties value
			for(int i = 0; i < ttProperties.size(); i++) {
				properties.setProperty(ttProperties.get(i).toString(), values.get(i));
			}
			// save properties to project root folder
			properties.store(output, null);
			System.out.println("Successfully stored the new property.");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	
	private static void createDefaultConfigFile() {
		OutputStream output = null;
		try {
			output = new FileOutputStream(filename);
			// set the properties value
			String root =  FileUtility.getProjectRoot();
			String driverPath = root + "//drivers";
			String testCasesPath =  root + "//TEST CASES";
			
			FileUtility.createPathIfNoExisting(driverPath);
			FileUtility.createPathIfNoExisting(testCasesPath);
			
			properties.setProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString(), driverPath + "//chromedriver.exe");
			properties.setProperty(TTProperties.INSTALLATION_DIRECTORY.toString(), root);
			properties.setProperty(TTProperties.TESTCASE_DIRECTORY.toString(), testCasesPath);
			properties.setProperty(TTProperties.LOCAL_HTML_DIRECTORY.toString(), root + "//LOCAL_HTML");
			properties.setProperty(TTProperties.DEFAULT_SELECT_DIR.toString(), root);
			
			// GCV 
			properties.setProperty(TTProperties.minTextScore.toString(), "50"); 
			properties.setProperty(TTProperties.minTextMatch.toString(),  "50"); 
			properties.setProperty(TTProperties.wvt_minLabelScore.toString(), "25"); 
			properties.setProperty(TTProperties.wvt_minLabelMatch.toString(), "35"); 
			properties.setProperty(TTProperties.wvt_minWebScore.toString(),  "25"); 
			properties.setProperty(TTProperties.wvt_minWebMatch.toString(),  "35"); 
			properties.setProperty(TTProperties.minLabelScore.toString(),  "65"); 
			properties.setProperty(TTProperties.minLabelMatch.toString(),  "60"); 
			properties.setProperty(TTProperties.confidentLabel.toString(), "75"); 
			properties.setProperty(TTProperties.minWebScore.toString(),  "50"); 
			properties.setProperty(TTProperties.minWebMatch.toString(),  "40"); 
			properties.setProperty(TTProperties.confidentWeb.toString(), "60");
			properties.setProperty(TTProperties.allowTextChange.toString(), "FALSE");
			
			
			// save properties to project root folder
			properties.store(output, null);
			System.out.println("Default Configuration file has been created.");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	
	
	
	
	private static boolean configFileExist() {

		String directory = System.getProperty("user.dir");
		File p = new File(directory + "\\" + filename);
		if(p.exists() && p.isDirectory() == false) {
			System.out.println("Configuration file loaded.");
			return true;
		}
		
		System.out.println("Configuration file does not exist.");
		return false;
	}
}
