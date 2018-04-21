package examensarbete.javafx.controller;

import java.io.File;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PreferencesController {

	
	private void addSliderListeners(Text percText, Slider slider) {
		percText.setText("" + Math.round(slider.getValue()));
		slider.valueProperty().addListener((obs, oldval, newVal) -> {
			slider.setValue(Math.round(newVal.doubleValue()));
			percText.setText("" + Math.round(newVal.doubleValue()));
		});
	}
	
	@FXML
	private Slider minTextScore, minTextMatch;
	@FXML
	private Text minTextScoreText, minMatchScoreText;
	@FXML
	private CheckBox alllowTextChangeCheckbox;
	
	
	@FXML
	private void onAllowTextChange() {

	}

	// WEB AND LABEL SCORE WHEN VALID TEXT (WVT)
			// LABEL
	@FXML
	private Slider wvt_minLabelScore, wvt_minLabelMatch;
	@FXML
	private Text wvt_minLabelScoreText, wvt_minLabelMatchText;
			//WEB
	@FXML
	private Slider wvt_minWebScore, wvt_minWebMatch;
	@FXML
	private Text wvt_minWebScoreText, wvt_minWebMatchText;

	
	// LABEL SCORE
	@FXML
	private Slider minLabelScore, minLabelMatch;
	@FXML
	private Text minLabelScoreText, minLabelMatchText;

	@FXML
	private Slider confidentLabel;
	@FXML
	private Text confidentLabelText;

	
	// WEB SCORE
	@FXML
	private Slider minWebScore, minWebMatch;
	@FXML
	private Text minWebScoreText, minWebMatchText;
	
	@FXML
	private Slider confidentWeb;
	@FXML
	private Text confidentWebText;

	
	
	@FXML
	private AnchorPane preferencesWindow;

	public PreferencesController() {
	}
	
	private void setSliderListeners() {
		addSliderListeners(minTextScoreText, minTextScore); 
		addSliderListeners(minMatchScoreText, minTextMatch); 
		addSliderListeners(wvt_minLabelScoreText, wvt_minLabelScore); 
		addSliderListeners(wvt_minLabelMatchText, wvt_minLabelMatch); 
		addSliderListeners(wvt_minWebScoreText, wvt_minWebScore); 
		addSliderListeners(wvt_minWebMatchText, wvt_minWebMatch); 
		addSliderListeners(minLabelScoreText, minLabelScore); 
		addSliderListeners(minLabelMatchText, minLabelMatch); 
		addSliderListeners(confidentLabelText, confidentLabel); 
		addSliderListeners(minWebScoreText, minWebScore); 
		addSliderListeners(minWebMatchText, minWebMatch); 
		addSliderListeners(confidentWebText, confidentWeb); 
	}
	

	@FXML
	private TextField defaultSelectedDir, installationDir, chromeDriverExePath, testcaseDir, imageDirectory;

	@FXML
	private void initialize() {
		setSliderListeners();
		try {
			defaultSelectedDir
					.setText(PropertiesHandler.properties.getProperty(TTProperties.DEFAULT_SELECT_DIR.toString()));
			installationDir
					.setText(PropertiesHandler.properties.getProperty(TTProperties.INSTALLATION_DIRECTORY.toString()));
			chromeDriverExePath
					.setText(PropertiesHandler.properties.getProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString()));
			testcaseDir.setText(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()));
			imageDirectory.setText(PropertiesHandler.properties.getProperty(TTProperties.IMAGE_DIRECTORY.toString()));
			
			minTextScore.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minTextScore.toString()))); 
			minTextMatch.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minTextMatch.toString())));
			wvt_minLabelScore.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minLabelScore.toString())));
			wvt_minLabelMatch.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minLabelMatch.toString())));
			wvt_minWebScore.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minWebScore.toString())));
			wvt_minWebMatch.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minWebMatch.toString()))); 
			minLabelScore.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minLabelScore.toString()))); 
			minLabelMatch.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minLabelMatch.toString()))); 
			confidentLabel.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.confidentLabel.toString()))); 
			minWebScore.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minWebScore.toString()))); 
			minWebMatch.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minWebMatch.toString()))); 
			confidentWeb.setValue(Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.confidentWeb.toString())));
	
		} catch (Exception e) {
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
		
		// GCV 
		PropertiesHandler.saveConfigProperty(TTProperties.minTextScore, Math.round(minTextScore.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.minTextMatch,  Math.round(minTextMatch.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.wvt_minLabelScore,  Math.round(wvt_minLabelScore.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.wvt_minLabelMatch,  Math.round(wvt_minLabelMatch.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.wvt_minWebScore,  Math.round(wvt_minWebScore.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.wvt_minWebMatch,  Math.round(wvt_minWebMatch.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.minLabelScore,  Math.round(minLabelScore.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.minLabelMatch,  Math.round(minLabelMatch.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.confidentLabel,  Math.round(confidentLabel.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.minWebScore,  Math.round(minWebScore.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.minWebMatch,  Math.round(minWebMatch.getValue()) + ""); 
		PropertiesHandler.saveConfigProperty(TTProperties.confidentWeb,  Math.round(confidentWeb.getValue()) + ""); 
		
		// TODO:: AllowTextChange should also be handled..
		System.out.println("Properties have been saved.");
	}
	
	
	@FXML
	void chromeDriverOpen() {
		setTextFieldTextIfNotNullOrEmpty(chromeDriverExePath, selectFile());
	}

	@FXML
	void defaultDirOpen() {
		setTextFieldTextIfNotNullOrEmpty(defaultSelectedDir, selectDirectory());
	}

	@FXML
	void imageDirOpen() {
		setTextFieldTextIfNotNullOrEmpty(imageDirectory, selectDirectory());
	}

	@FXML
	void installDirOpen() {
		setTextFieldTextIfNotNullOrEmpty(installationDir, selectDirectory());
	}

	@FXML
	void testCaseDirOpen() {
		setTextFieldTextIfNotNullOrEmpty(testcaseDir, selectDirectory());
	}

	private void setTextFieldTextIfNotNullOrEmpty(TextField tf, String text) {
		if (text != null && text.equals("") == false) {
			tf.setText(text);
		}
	}

	private String selectDirectory() {
		try {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Set Directory");
			File defaultDirectory = new File(System.getProperty("user.dir"));
			chooser.setInitialDirectory(defaultDirectory);
			File selectedDirectory = chooser.showDialog(((Stage) preferencesWindow.getScene().getWindow()));
			if (selectedDirectory != null) {
				return selectedDirectory.getPath();
			}

		} catch (Exception e) {
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

			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("EXE files (*.exe)", "*.exe");
			fileChooser.getExtensionFilters().add(extFilter);

			File selectedFile = fileChooser.showOpenDialog(((Stage) preferencesWindow.getScene().getWindow()));
			if (selectedFile != null) {
				return selectedFile.getPath();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

}
