package examensarbete.model.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

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
			properties.setProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString(), "D://WORKSPACE/chromedriver.exe");
			properties.setProperty(TTProperties.INSTALLATION_DIRECTORY.toString(), "D://WORKSPACE");
			properties.setProperty(TTProperties.TESTCASE_DIRECTORY.toString(), "D://WORKSPACE");
			properties.setProperty(TTProperties.IMAGE_DIRECTORY.toString(), "D://WORKSPACE");
			properties.setProperty(TTProperties.DEFAULT_SELECT_DIR.toString(), "D://WEB_COMPONENTS");

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
