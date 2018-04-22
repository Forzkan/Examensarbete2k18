package examensarbete.model.test;

public class TestStepResultImpl implements TestStepResult {

	private MatchType testStepMatchType;
	private TestImage originalContextImage;
	private TestImage originalTargetImage;
	private TestImage matchedContextImage;
	private TestImage matchedTargetImage;
	
	public TestStepResultImpl(MatchType matchResult, TestImage originalContextImage, TestImage originalTargetImage, TestImage matchedContextImage, TestImage matchedTargetImage) {
		this.testStepMatchType = matchResult;
		this.originalContextImage = originalContextImage;
		this.originalTargetImage = originalTargetImage;
		this.matchedContextImage = matchedContextImage;
		this.matchedTargetImage = matchedTargetImage;
	}

	@Override
	public boolean getStepSuccess() {
		if(testStepMatchType == MatchType.MATCH || testStepMatchType == MatchType.NEW_LOC_MATCH) {
			return true;
		}
		return false;
	}

	@Override
	public MatchType getTestStepMatchType() {
		return testStepMatchType;
	}

	@Override
	public TestImage getOriginalContextImage() {
		return originalContextImage;
	}

	@Override
	public TestImage getOriginalTargetImage() {
		return originalTargetImage;
	}

	@Override
	public TestImage getMatchedContextImage() {
		return matchedContextImage;
	}

	@Override
	public TestImage getMatchedTargetImage() {
		return matchedTargetImage;
	}
	
}
