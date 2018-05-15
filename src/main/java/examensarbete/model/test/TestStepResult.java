package examensarbete.model.test;

public interface TestStepResult {
	boolean getStepSuccess();

	MatchType getTestStepMatchType();
	
	TestImage getOriginalContextImage();
	
	TestImage getOriginalTargetImage();
	
	TestImage getMatchedContextImage();
	
	TestImage getMatchedTargetImage();
	
	/**
	 * Bör innehålla:
	 * Ursprungliga contextImage
	 * Ursprungliga targetImage
	 * Nya contextImage
	 * Nya targetImage
	 * 
	 * Ha bara getters 
	 * Konstruktor med alla fält som parametrar, samla alla paramterar som behövs innan man skapar ett TestStepResult 
	 */
}
