package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fasterxml.jackson.annotation.JsonIgnore;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import examensarbete.model.test.TestImage;
import examensarbete.model.test.TestImageImpl;
import examensarbete.model.utility.FileUtility;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Screen;

import org.apache.commons.io.FileUtils;


public class ChromeWebAction extends ActionBase{
	
	private ChromeDriver driver;
	private String url;
	private boolean useLocalHtml;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isUseLocalHtml() {
		return useLocalHtml;
	}
	public void setUseLocalHtml(boolean useLocalHtml) {
		this.useLocalHtml = useLocalHtml;
	}
	@Override
	public EActionType getActionType() {
		return this.actionType;
	}
	
	public ChromeWebAction() throws AWTException {
		super();
		this.actionType = EActionType.CHROMEBROWSER;
		System.setProperty("webdriver.chrome.driver", PropertiesHandler.properties.getProperty(TTProperties.CHROMEDRIVER_EXE_PATH.toString()));
	}
	public ChromeWebAction(boolean useLocalHtml) throws AWTException {
		super();
		this.actionType = EActionType.CHROMEBROWSER;
		this.setUseLocalHtml(useLocalHtml);
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
		    if(useLocalHtml) {
			    driver.navigate().to("file:///" + PropertiesHandler.properties.getProperty(TTProperties.LOCAL_HTML_DIRECTORY.toString()) + "//" + url);
		    }else {
		    	driver.navigate().to(url);
		    }
		    
		    driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		    return true;
		}catch(Exception e) {
			System.out.println("A problem occured when starting up Chrome.");
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	
	@JsonIgnore
	public Rectangle getBrowserBounds() {
		
		Point coordinates = driver.manage().window().getPosition();
		Dimension size = driver.manage().window().getSize();
		
		return new Rectangle(coordinates.x, coordinates.y, size.width, size.height);
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
System.out.println("");
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
	
	@JsonIgnore
	public TestImage getNewContextImage() throws IOException {
		final File outputFile = new File(takeBrowserScreenshot(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + "\\tempContextImage"));
	
		BufferedImage contextScreenshot = ImageIO.read(outputFile);
		return new TestImageImpl(outputFile.getAbsolutePath(), new java.awt.Point(0,0), contextScreenshot.getWidth(), contextScreenshot.getHeight());
	}
	
	@JsonIgnore
	public TestImage getNewFullScreenContextImage() throws IOException {
//		final File outputFile = new File(takeBrowserScreenshot(FileUtility.getProjectRoot() + "\\tempContextImage"));
		File outputFile;
		try {
			Screen screen = Screen.getPrimary();
			Rectangle bounds = new Rectangle((int)screen.getBounds().getMinX(),
					(int)screen.getBounds().getMinX(),
					(int)screen.getBounds().getWidth(),
					(int)screen.getBounds().getHeight());
			outputFile = new File(this.takeScreenShot(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + "/tempContextImage", bounds));
			BufferedImage contextScreenshot = ImageIO.read(outputFile);
			return new TestImageImpl(outputFile.getAbsolutePath(), new java.awt.Point(0,0), contextScreenshot.getWidth(), contextScreenshot.getHeight());
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
		return null;

	}
	
	
	@JsonIgnore
	public ChromeDriver getDriver() {
		return driver;
	}

	
	
}
