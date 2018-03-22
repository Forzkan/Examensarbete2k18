package examensarbete.model.action;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;

import examensarbete2k18.model.properties.PropertiesHandler;
import examensarbete2k18.model.properties.TTProperties;

import org.apache.commons.io.FileUtils;


public class ChromeWebAction extends ActionRobotBase implements IAction{
	
	private ChromeDriver driver;
	String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ChromeWebAction(String url) throws AWTException {
		super(EActionType.CHROMEBROWSER);
		this.url = url;
	}
	
	
	@Override
	public void actionSetup()    
	{   
		try {
			// TODO:: This will probably have to be located outside of the resources folder, i.e. the actual jar file, so we should probably look in preferences for the installationPath and then a drivers folder.
		    System.setProperty("webdriver.chrome.driver", PropertiesHandler.properties.getProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString()));
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	    
	}// TODO::https://www.youtube.com/watch?v=KMvgbYMYApM
	
	
	@Override
	public boolean performAction() {
		try {
		    driver = new ChromeDriver();
		    driver.manage().deleteAllCookies();
		    driver.manage().window().maximize();
		    driver.navigate().to(url);
		    System.out.println("DRIVER CAPABILITIES:");
		    System.out.println(driver.getCapabilities());
		}catch(Exception e) {
			System.out.println("A problem occured when starting up Chrome.");
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	
	public String takeBrowserScreenshot(String filename) {
		final File screenShot = new File(PropertiesHandler.properties.getProperty(TTProperties.IMAGE_DIRECTORY.toString()) + "/" + filename + ".png").getAbsoluteFile();

		final File outputFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(outputFile, screenShot);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return screenShot.getPath();
	}
	
	
	public void closeBrowser() {
		if(driver != null) {
			System.out.println("Closing Chrome web browser.");
			driver.quit();
		}
	}
	
	
	
	
}
