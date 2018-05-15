package examensarbete.model.test;

public interface TestStepResult {
	boolean getStepSuccess();

	MatchType getTestStepMatchType();
	
	TestImage getOriginalContextImage();
	
	TestImage getOriginalTargetImage();
	
	TestImage getMatchedContextImage();
	
	TestImage getMatchedTargetImage();
	
	/**
	 * B�r inneh�lla:
	 * Ursprungliga contextImage
	 * Ursprungliga targetImage
	 * Nya contextImage
	 * Nya targetImage
	 * 
	 * Ha bara getters 
	 * Konstruktor med alla f�lt som parametrar, samla alla paramterar som beh�vs innan man skapar ett TestStepResult 
	 */
}
