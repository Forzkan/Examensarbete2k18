package examensarbete.model.action;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import javafx.scene.control.TextInputDialog;

import org.apache.commons.io.FileUtils;


public class ChromeWebAction extends ActionBase{
	
	private ChromeDriver driver;
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public EActionType getType() {
		return this.actionType;
	}
	
	public ChromeWebAction() throws AWTException {
		super();
		this.actionType = EActionType.CHROMEBROWSER;
		System.setProperty("webdriver.chrome.driver", PropertiesHandler.properties.getProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString()));
	}
	
	
	@Override
	public void actionSetup()    
	{   
		try {
			createTextInputDialog();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}// TODO::https://www.youtube.com/watch?v=KMvgbYMYApM

	private void createTextInputDialog(){
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Text Input Dialog");
		dialog.setHeaderText("Please enter web page url");
		dialog.setContentText("");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(page -> url = page);
	}
	
	@Override
	public boolean performAction() {
		try {
			System.out.println("OPENING CHROME, AND NAVIGATING TO URL: " + url);
		    driver = new ChromeDriver();
		    driver.manage().deleteAllCookies();
		    driver.manage().window().maximize();
		    driver.navigate().to(url);
		    driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		    return true;
		}catch(Exception e) {
			System.out.println("A problem occured when starting up Chrome.");
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	
	/**
	 * To be used between performing actions.
	 * @param timeout
	 */
	public void waitUntilPageIsLoaded(int timeout) {
		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
	}
	
	
	public String takeBrowserScreenshot(String pathAndFileNameNoEnding) {
		final File screenShot = new File(pathAndFileNameNoEnding + ".png").getAbsoluteFile();

		final File outputFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(outputFile, screenShot);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return screenShot.getAbsolutePath();
	}
	
	
	public void closeBrowser() {
		if(driver != null) {
			System.out.println("Closing Chrome web browser.");
			driver.quit();
		}
	}
	
	
	
	
}
