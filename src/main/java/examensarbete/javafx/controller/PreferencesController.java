package examensarbete.javafx.controller;

import java.io.File;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class PreferencesController {
	
	@FXML
	private AnchorPane preferencesWindow;
	
	public PreferencesController() {
	}
	
	
	@FXML
	private TextField 	defaultSelectedDir,
						installationDir,
						chromeDriverExePath,
						testcaseDir,
						imageDirectory;
	
	@FXML
	private void initialize() {
		try {					
			defaultSelectedDir.setText(PropertiesHandler.properties.getProperty(TTProperties.DEFAULT_SELECT_DIR.toString()));
			installationDir.setText(PropertiesHandler.properties.getProperty(TTProperties.INSTALLATION_DIRECTORY.toString()));
			chromeDriverExePath.setText(PropertiesHandler.properties.getProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString()));
			testcaseDir.setText(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()));
			imageDirectory.setText(PropertiesHandler.properties.getProperty(TTProperties.IMAGE_DIRECTORY.toString()));
		}catch(Exception e) {
			System.out.println("Error when setting properties text to preferences text field.");
			System.out.println(e.getMessage());
		}
	}
	
	
	@FXML
	private void savePreferences() {
		PropertiesHandler.saveConfigProperty(TTProperties.DEFAULT_SELECT_DIR, defaultSelectedDir.getText());
		PropertiesHandler.saveConfigProperty(TTProperties.INSTALLATION_DIRECTORY, installationDir.getText());
		PropertiesHandler.saveConfigProperty(TTProperties.CHROMEDRIVER_EXE_PATH, chromeDriverExePath.getText());
		PropertiesHandler.saveConfigProperty(TTProperties.TESTCASE_DIRECTORY, testcaseDir.getText());
		PropertiesHandler.saveConfigProperty(TTProperties.IMAGE_DIRECTORY, imageDirectory.getText());
		System.out.println("Properties have been saved.");
	}
	
    @FXML
    void chromeDriverOpen() {
    	setTextFieldTextIfNotNullOrEmpty(chromeDriverExePath,selectFile());
    }

    @FXML
    void defaultDirOpen() {
    	setTextFieldTextIfNotNullOrEmpty(defaultSelectedDir,selectDirectory());
    }

    @FXML
    void imageDirOpen() {
    	setTextFieldTextIfNotNullOrEmpty(imageDirectory,selectDirectory());
    }

    @FXML
    void installDirOpen() {
    	setTextFieldTextIfNotNullOrEmpty(installationDir, selectDirectory());
    }

    @FXML
    void testCaseDirOpen() {
    	setTextFieldTextIfNotNullOrEmpty(testcaseDir,selectDirectory());
    }
	
    
    private void setTextFieldTextIfNotNullOrEmpty(TextField tf, String text) {
    	if(text != null && text.equals("") == false) {
    		tf.setText(text);
    	}
    }
    
    
    private String selectDirectory() {
    	try {
	    	DirectoryChooser chooser = new DirectoryChooser();
	    	chooser.setTitle("Set Directory");
	    	File defaultDirectory = new File(System.getProperty("user.dir"));
	    	chooser.setInitialDirectory(defaultDirectory);
	    	File selectedDirectory = chooser.showDialog( ((Stage) preferencesWindow.getScene().getWindow()));
	    	if(selectedDirectory != null) {
	    		return selectedDirectory.getPath();	
	    	}
	    	
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	return "";
    }
    
    
    private String selectFile() {
		try {
	    	FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Set File");
			File defaultDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setInitialDirectory(defaultDirectory);
			
			FileChooser.ExtensionFilter extFilter = 
	                new FileChooser.ExtensionFilter("EXE files (*.exe)", "*.exe");
			fileChooser.getExtensionFilters().add(extFilter);
	
			File selectedFile= fileChooser.showOpenDialog( ((Stage) preferencesWindow.getScene().getWindow()));
			if(selectedFile != null) {
				return selectedFile.getPath();
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
    }
    
    
    
}
