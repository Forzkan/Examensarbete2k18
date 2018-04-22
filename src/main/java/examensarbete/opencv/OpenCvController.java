package examensarbete.opencv;

import org.opencv.core.Core;

import examensarbete.model.test.TestImage;
import examensarbete.model.test.MatchType;

public class OpenCvController {

	private TemplateMatcher templateMatcher;
	
	public OpenCvController() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		templateMatcher = new TemplateMatcher();
	}
	
	public MatchType runComparison(TestImage contextImage, TestImage targetImage) {
//		templateMatcher.runVisualComparison(contextImage, targetImage);
		MatchType result = templateMatcher.findTargetImage(contextImage, targetImage);
		
		TestImage resultImage = templateMatcher.getResultTestImage();
		
		System.out.println(targetImage);
		System.out.println(resultImage);
		
		return result;
//		return MatchType.NO_MATCH;
	}
	
	public void runVisualComparison(TestImage contextImage, TestImage targetImage) {
		templateMatcher.runVisualComparison(contextImage, targetImage);
//		MatchType result = templateMatcher.findTargetImage(contextImage, targetImage);
		
		TestImage resultImage = templateMatcher.getResultTestImage();
		
		System.out.println(targetImage);
		System.out.println(resultImage);
		
//		return result;
	}
	
	public TestImage getResultTestImage() {
		return templateMatcher.getResultTestImage();
	}
}
