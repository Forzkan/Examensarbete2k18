package examensarbete.google.cloud.vision;

import examensarbete.model.test.MatchType;

public interface ValidChangeAlgorithm {

	
	public MatchType isValidChange(GCVImageResult target, GCVImageResult newTarget);

}
