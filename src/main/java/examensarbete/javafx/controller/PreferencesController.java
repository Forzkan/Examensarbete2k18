package examensarbete.javafx.controller;

import java.io.File;

import examensarbete2k18.model.properties.PropertiesHandler;
import examensarbete2k18.model.properties.TTProperties;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PreferencesController {
	
	private Stage stage;
	
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
			System.out.println(PropertiesHandler.properties.getProperty(TTProperties.DEFAULT_SELECT_DIR.toString()));
			defaultSelectedDir.setText(PropertiesHandler.properties.getProperty(TTProperties.DEFAULT_SELECT_DIR.toString()));
			installationDir.setText(PropertiesHandler.properties.getProperty(TTProperties.INSTALLATION_DIRECTORY.toString()));
			chromeDriverExePath.setText(PropertiesHandler.properties.getProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString()));
			testcaseDir.setText(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()));
			imageDirectory.setText(PropertiesHandler.properties.getProperty(TTProperties.IMAGE_DIRECTORY.toString()));
		}catch(Exception e) {
			System.out.println("Error when setting properties text to preferences text field.");
			System.out.println(e.getMessage());
		}
		// Set stage.
		stage = ((Stage) defaultSelectedDir.getScene().getWindow());

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
    	chromeDriverExePath.setText(selectFile());
    }

    @FXML
    void defaultDirOpen() {
    	defaultSelectedDir.setText(selectDirectory());
    }

    @FXML
    void imageDirOpen() {
    	imageDirectory.setText(selectDirectory());
    }

    @FXML
    void installDirOpen() {
    	installationDir.setText(selectDirectory());
    }

    @FXML
    void testCaseDirOpen() {
    	testcaseDir.setText(selectDirectory());
    }
	
    
    private String selectDirectory() {
    	try {
	    	DirectoryChooser chooser = new DirectoryChooser();
	    	chooser.setTitle("Set Directory");
	    	File defaultDirectory = new File(System.getProperty("user.dir"));
	    	chooser.setInitialDirectory(defaultDirectory);
	    	File selectedDirectory = chooser.showDialog(stage);
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
	
			File selectedFile= fileChooser.showOpenDialog(stage);
			if(selectedFile != null) {
				return selectedFile.getPath();
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
    }
    
    
    
}
