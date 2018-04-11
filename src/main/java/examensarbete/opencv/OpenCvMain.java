package examensarbete.opencv;

import java.awt.Point;
//import org.opencv.core.Point;

import examensarbete.model.test.TestImage;
import examensarbete.model.test.TestImageImpl;

public class OpenCvMain {

	public static void main(String[] args) {
		OpenCvController openCvController = new OpenCvController();
//		TestImage contextImage = new TestImageImpl("src/main/resources/tests/test_2/3-whole.png", new Point(0,0), 0, 0);
//		TestImage contextImage = new TestImageImpl("src/main/resources/tests/test_2/test2_CI.png", new Point(0,0), 0, 0);
//		TestImage contextImage = new TestImageImpl("src/main/resources/tests/test_2/tempContextImage.png", new Point(0,0), 0, 0);
		
//		TestImage contextImage = new TestImageImpl("src/main/resources/tests/test_2/3-whole-small.png", new Point(0,0), 0, 0);
//		TestImage contextImage = new TestImageImpl("src/main/resources/tests/test_2/3-whole-changed-extra.png", new Point(0,0), 2560, 1308);
		
//		TestImage targetImage = new TestImageImpl("src/main/resources/tests/test_2/3-part.png", new Point(1091,298), 75, 24);
		TestImage targetImage = new TestImageImpl("src/main/resources/tests/test_2/test2_SI.png", new Point(1091,298), 75, 24);
		
//		TestImage targetImage = new TestImageImpl("src/main/resources/tests/test_2/3-part-small.png", new Point(850,757), 80, 51);
//		TestImage targetImage = new TestImageImpl("src/main/resources/tests/test_2/warframe_part_1.png", new Point(850,757), 80, 51);
//		System.out.println(targetImage.toString());
//		TestImage[] testImages = {contextImage, targetImage};
		
//		openCvController.runComparison(contextImage, targetImage);
		
		System.out.println(openCvController.runComparison(contextImage, targetImage));
		
//		openCvController.runVisualComparison(contextImage, targetImage);
	}

}
