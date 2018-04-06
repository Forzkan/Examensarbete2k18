package examensarbete.opencv;

import org.opencv.core.Core;

import examensarbete.model.test.TestImage;
import examensarbete.opencv.TemplateMatcher.MatchType;

public class OpenCvController {

	private TemplateMatcher templateMatcher;
	
	public OpenCvController() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		templateMatcher = new TemplateMatcher();
	}
	
	public MatchType runComparison(TestImage contextImage, TestImage targetImage) {
//		return templateMatcher.runVisualComparison(contextImage, targetImage);
//		templateMatcher.findTargetImage(contextImage, targetImage);
//		MatchType result = templateMatcher.run(testImages);
		
		//TODO: Returnera enum med outcome, typ MATCH, NEW_LOC_MATCH, NO_MATCH
		
		return templateMatcher.findTargetImage(contextImage, targetImage);
	}
}
