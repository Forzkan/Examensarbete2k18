package examensarbete2k18.model.properties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesHandler {

	private final static String filename = "tt_properties";
	public final static Properties properties = new Properties();;
	
	
	public static void InitializePropertiesHandler() {
		if(configFileExist() == false) {
			createDefaultConfigFile();
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
	
	
	public static void saveConfigProperties(ArrayList<TTProperties> ttProperties, String value) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(filename);
			// set the properties value
			for(TTProperties prop : ttProperties) {
				properties.setProperty(prop.toString(), value);
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
		InputStream input = null;

		try {

			String filename = "config.properties";
			input = PropertiesHandler.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Unable to find properties file: " + filename + " \nCreating a new properties file.");
				return false;
			}

			// load a properties file from class path, inside static method
			properties.load(input);
			
			System.out.println("Loaded properties file: " + filename);
			Enumeration<?> e = properties.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = properties.getProperty(key);
				System.out.println("Key : " + key + ", Value : " + value);
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			return false;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
					return false;
				}
			}
		}
		return true;
	}
}
