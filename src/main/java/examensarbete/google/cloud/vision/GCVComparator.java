package examensarbete.google.cloud.vision;

import java.awt.Point;
import java.util.ArrayList;

public class GCVComparator implements ValidChangeAlgorithm{
	
	
	private final double MIN_ACCEPTED_WEB_MATCH = 0.70; // 70%.
	private final double MIN_ACCEPTED_LABEL_MATCH = 0.70; // 70%.
	
	private final double MIN_WEB_SCORE = 0.55;
	private final double MIN_LABEL_SCORE = 0.65; 
	
	private final boolean allowNewText = false; // a page with several similar buttons could probably big a huuuge mess to deal with if the one button we are after gets removed, or gets a completely new text.
	
	
	// RULES, 
	// DIFFERENT TEXT = NOK.
	// SAME TEXT, DIFFERENT VERTICES = OK, BUT THEN AT LEAST THE MAIN COLOR SHOULD BE THE SAME,
	// .. AND THE LABEL + WEBENTITIESRESULTS MUST BE OK.
	// 
	
	private static ArrayList<ImageChange> changes;
	
	@Override
	public boolean isValidChange(GCVImageResult target, GCVImageResult newTarget) {
		changes = new ArrayList<ImageChange>();
		
		verifyColor(target, newTarget);
		
		verifyText(target.getTextResults(), newTarget.getTextResults());
		
		verifyLabels(target.getLabelResults(), newTarget.getLabelResults());
		
		verifyWebEntities(target.getWebEntitiesResults(), newTarget.getWebEntitiesResults());
		
		return analyzeIfValidChanges();
	}
	
	

	private boolean analyzeIfValidChanges() {
		int changeCounter = 0;
		for(ImageChange c : changes){
			System.out.println(c.name());
		}
		// TODO:: First attempt, the logic here should be well thought through.
		if(changes.contains(ImageChange.INVALID_TEXTCHANGE) && allowNewText == false) {
			return false;
		}
		if(changes.contains(ImageChange.MAINCOLOR)  || 
			changes.contains(ImageChange.SECONDARYCOLOR) ||
			changes.contains(ImageChange.THIRDCOLOR)) {
			changeCounter ++;
		}
		if(changes.contains(ImageChange.FONT)) {
			changeCounter ++;
		}
		if(changes.contains(ImageChange.TEXT)) {
			changeCounter ++;
		}
		if(changes.contains(ImageChange.LABEL)) {
			changeCounter ++;
		}
		if(changes.contains(ImageChange.WEBENTITIES)) {
			changeCounter ++;
		}
	
		return changeCounter <= 1;
	}
	

	private void verifyColor(GCVImageResult target, GCVImageResult newTarget) {
		// Main color.
		if(isSameColorValues(target.getDominantColor(), newTarget.getDominantColor()) == false) {
			changes.add(ImageChange.MAINCOLOR);
			System.out.println("MAIN COLOR HAS CHANGED");
		}
		// Secondary color.
		if(isSameColorValues(target.getSecondaryColor(), newTarget.getSecondaryColor()) == false) {
			changes.add(ImageChange.SECONDARYCOLOR);
			System.out.println("SECONDARY COLOR HAS CHANGED");
		}
		// Third color.
		if(isSameColorValues(target.getThirdColor(), newTarget.getThirdColor()) == false) {
			changes.add(ImageChange.THIRDCOLOR);
			System.out.println("THIRD COLOR HAS CHANGED");
		}
	}
	
	
	private boolean isSameColorValues(DominantGCVColor target, DominantGCVColor newTarget) {
		if(target.getRed() 	 != newTarget.getRed() ||
		   target.getGreen() != newTarget.getGreen() || 
		   target.getBlue()  != newTarget.getBlue()) {
			System.out.println("Different color values.");
			return false;
		}
		return true;
	}
	
	
	
	private void verifyText(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList) {
		int foundCounter = 0;
		for(GCVResult r : newTargetList) {
			for(GCVResult tr : targetList) {
				if(r.getDescription().toString().equalsIgnoreCase(tr.getDescription())) {
					if(verifyTextVertices(tr, r) == false) {
						changes.add(ImageChange.FONT);
					}
					foundCounter++;
					break;
				}
			}
		}
		if(foundCounter < newTargetList.size()) {
			System.out.println("ATLEAST ONE TEXT STRING HAS CHANGED.");
			changes.add(ImageChange.INVALID_TEXTCHANGE);
		}
	}
	
	private boolean verifyTextVertices(GCVResult target, GCVResult newTarget) {
		// IF NOT THE EXACT SAME, 
		int equalCounter = 0;
		for(Point p : target.getVerticesPoints()) {
			for(Point np : newTarget.getVerticesPoints()) {
				if(p.equals(np)) {
					equalCounter ++;
				}
			}
		}
		// equalCounter should be the same as the number of vertices in the targetlist.
		if(equalCounter < target.getVerticesPoints().size()) {
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	private void verifyLabels(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList) {
		verifyGCVList(targetList, newTargetList, MIN_LABEL_SCORE, MIN_ACCEPTED_LABEL_MATCH, ImageChange.LABEL);
	}
	private void verifyWebEntities(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList) {
		verifyGCVList(targetList, newTargetList, MIN_WEB_SCORE, MIN_ACCEPTED_WEB_MATCH, ImageChange.WEBENTITIES);
	}

	private void verifyGCVList(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList, double minScore ,double minMatchPercentage, ImageChange change) {
		ArrayList<GCVResult> validTargetList = createListOfValidResults(targetList, minScore);
		ArrayList<GCVResult> validNewList = createListOfValidResults(newTargetList, minScore);
		
		int maxMatches = validTargetList.size() > validNewList.size() ? validTargetList.size() : validNewList.size();
		
		int matches = 0;
		for(GCVResult t : validTargetList) {
			for(GCVResult nt : validNewList) {
				if(t.getDescription().equalsIgnoreCase(nt.getDescription())) {
					matches++;
				}
			}
		}
		double matchPercentage = matches / maxMatches;
		System.out.println("MATCHING PERCENTAGE - " + change.name() + " : " + matchPercentage + " Threshold : " + minMatchPercentage);
		if(matchPercentage < minMatchPercentage) {
			changes.add(change);
		}
	}
	
	private ArrayList<GCVResult> createListOfValidResults(ArrayList<GCVResult> aTarget, double minScore){
		ArrayList<GCVResult> list = new ArrayList<GCVResult>();
		for(GCVResult r : aTarget) {
			if(r.getScore() >= minScore) {
				list.add(r);
			}
		}
		return list;
	}
	

	


	
	
	
	
	
	
}
