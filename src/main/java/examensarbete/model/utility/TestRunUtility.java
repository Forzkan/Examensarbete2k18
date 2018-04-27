package examensarbete.model.utility;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import examensarbete.model.action.ChromeWebAction;
import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import examensarbete.model.test.TestImage;
import examensarbete.model.test.TestImageImpl;
import examensarbete.opencv.OpenCvController;
import examensarbete.model.test.MatchType;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class TestRunUtility {

	public static Point getOffset(ChromeWebAction chrome) throws AWTException, IOException {
		// Template match.
		TestImage target = new TestImageImpl();
		target.setImagePath(getBrowserImage(chrome, PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + "\\tmpBrowserAsTarget"));
		TestImage context = new TestImageImpl();
		Rectangle2D bounds = Screen.getPrimary().getBounds();
		Rectangle area = new Rectangle((int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getWidth(), (int)bounds.getHeight());
		context.setImagePath(getScreenImage(area, PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + "\\tmpFullscreenAsContext")); // TODO::RELATIVE FILE PATHS.
		
		OpenCvController openCvController = new OpenCvController();
		MatchType matchResult = MatchType.NO_MATCH;
		matchResult = openCvController.runComparison(context, target);// TODO:: Handle if no match.
		TestImage imageWOffset = openCvController.getResultTestImage();
		return new Point((int)imageWOffset.getCoordinates().getX(), (int)imageWOffset.getCoordinates().getY());
	}
	
	
	
	private static String getScreenImage(Rectangle area, String pathAndNameNoFileEnding) throws AWTException, IOException {
//		String path = FileUtility.getProjectRoot() + "tmpFullScreenImage";
		Robot robot = new Robot();
		BufferedImage bufferedImage = robot.createScreenCapture(area);
		bufferedImage = robot.createScreenCapture(area);
		File image = new File(pathAndNameNoFileEnding + ".png");
		ImageIO.write(bufferedImage, "PNG", image);
//		BufferedImage ret = ImageIO.read(image);
		return image.getAbsolutePath();
	}
	

	
	private static String getBrowserImage(ChromeWebAction chrome, String pathAndNameNoFileEnding) throws IOException {
		final File browserImg = new File(pathAndNameNoFileEnding + ".png").getAbsoluteFile();
		final File outputFile = ((TakesScreenshot) chrome.getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(outputFile, browserImg);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return browserImg.getAbsolutePath();
	}
	
	
	
	
	public static String takeNewTargetImage(Rectangle bounds, String pathAndNameNoEnding) throws AWTException, IOException {
		return getScreenImage(bounds, pathAndNameNoEnding);
	}
	
	

}
