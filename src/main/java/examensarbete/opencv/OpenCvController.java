package examensarbete.opencv;

import org.opencv.core.Core;

import examensarbete.model.test.TestImage;

public class OpenCvController {

	private TemplateMatcher templateMatcher;
	
	public OpenCvController() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		templateMatcher = new TemplateMatcher();
	}
	
	public void runComparison(TestImage contextImage, TestImage targetImage) {
//		templateMatcher.run(contextImage, targetImage);
		templateMatcher.findTargetImage(contextImage, targetImage);
//		MatchType result = templateMatcher.run(testImages);
		//TODO: Returnera enum med outcome, typ MATCH, NEW_LOC_MATCH, NO_MATCH
	}
}
