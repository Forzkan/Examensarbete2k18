package examensarbete.google.cloud.vision;

import java.awt.Point;
import java.util.ArrayList;

public class GCVComparator implements ValidChangeAlgorithm{
	
	
	private final double MIN_ACCEPTED_TEXT_MATCH = 0.5; // 50%.
	private final double MIN_TEXT_SCORE = 0.5; // 50%.
	private final boolean allowNewText = false; // a page with several similar buttons could probably big a huuuge mess to deal with if the one button we are after gets removed, or gets a completely new text.
	
	
	private final double MIN_ACCEPTED_WEB_MATCH = 0.40; // 70%.
	private final double MIN_WEB_SCORE = 0.50;
	
	private final double MIN_ACCEPTED_LABEL_MATCH = 0.60; // 70%.
	private final double MIN_LABEL_SCORE = 0.65; 
	
	
	private final double MIN_ACCEPTED_WEB_MATCH_WHEN_VALID_TEXT = 0.35; // 70%.
	private final double MIN_ACCEPTED_LABEL_MATCH_WHEN_VALID_TEXT = 0.35; // 70%.
	
	private final double MIN_WEB_SCORE_WHEN_VALID_TEXT = 0.25;
	private final double MIN_LABEL_SCORE_WHEN_VALID_TEXT = 0.25; 
	

	private final double CONFIDENT_LABEL_THRESHOLD = 0.75; // if over 75% matches, then it is considered to be a very confident label match.
	private boolean veryConfidentLabelMatch = false;
	
	// RULES, 
	// DIFFERENT TEXT = NOK.
	// SAME TEXT, DIFFERENT VERTICES = OK, BUT THEN AT LEAST THE MAIN COLOR SHOULD BE THE SAME,
	// .. AND THE LABEL + WEBENTITIESRESULTS MUST BE OK.
	// 
	
	private static ArrayList<ImageChange> changes;
	
	@Override
	public boolean isValidChange(GCVImageResult target, GCVImageResult newTarget) {
		changes = new ArrayList<ImageChange>();
		System.out.println("");
		verifyColor(target, newTarget);
		
		verifyText(target.getTextResults(), newTarget.getTextResults());
		
		boolean isValidText = !changes.contains(ImageChange.INVALID_TEXTCHANGE);
		
		verifyLabels(target.getLabelResults(), newTarget.getLabelResults(), isValidText);
		
		verifyWebEntities(target.getWebEntitiesResults(), newTarget.getWebEntitiesResults(), isValidText);
		
		verifyLogos(target.getLogoResults(), newTarget.getLogoResults());
		
		return analyzeIfValidChanges();
	}
	
	

	private void verifyLogos(ArrayList<GCVResult> logoResults, ArrayList<GCVResult> newLogoResults) {
		
		for(GCVResult r : logoResults ) {
			System.out.println("LOGO: " + r.getDescription() + " SCORE: " + r.getScore());
		}
		for(GCVResult r : newLogoResults ) {
			System.out.println("LOGO: " + r.getDescription() + " SCORE: " + r.getScore());
		}
		System.out.println("LOGO RESULTS: ");
		
	}



	private boolean analyzeIfValidChanges() {
		System.out.println("ANALYZE IF VALID CHANGES.");
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
			if(veryConfidentLabelMatch == false) { // if true, leave out the results of web entities, because it seems to vary a lot between very similar images
												   // otherwise... add a change to the counter..
				changeCounter ++;
			}
			
		}
		
		
		if(changes.contains(ImageChange.FONT) && 
				(changes.contains(ImageChange.MAINCOLOR) || 
				 changes.contains(ImageChange.SECONDARYCOLOR) || 
				 changes.contains(ImageChange.THIRDCOLOR))) {
			changeCounter--; // Colorchanges seems to actually have an effect on the vertices, and font changes may for sure have an effect on colors changing..
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
		if(target != null && newTarget != null) {
			if(target.getRed() 	 != newTarget.getRed() ||
			   target.getGreen() != newTarget.getGreen() || 
			   target.getBlue()  != newTarget.getBlue()) {
				return false;
			}
			return true;
		}
		if(target == null && newTarget == null) {
			return true;
		}
		return false;
	}
	
	
	
	private void verifyText(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList) {
		int foundCounter = 0;
		ArrayList<GCVResult> validTargetList = createListOfValidResults(targetList, MIN_TEXT_SCORE);
		ArrayList<GCVResult> validNewTargetList = createListOfValidResults(newTargetList, MIN_TEXT_SCORE);
		
		
		for(GCVResult r : validNewTargetList) {
			for(GCVResult tr : validTargetList) {
				System.err.println(r.getDescription() + " - <||> - " + tr.getDescription());
				if(r.getDescription().toString().equalsIgnoreCase(tr.getDescription())) {
					if(verifyTextVertices(tr, r) == false) {
						changes.add(ImageChange.FONT);
					}
					foundCounter++;
					break;
				}
			}
		}
		
		int maxMatches = validTargetList.size() > validNewTargetList.size() ? validTargetList.size() : validNewTargetList.size();
		double matchPercentage = (double)((double)foundCounter/ (double)maxMatches);
		if(matchPercentage < MIN_ACCEPTED_TEXT_MATCH) {
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
			System.out.println("ALL VERTICES DO NOT MATCH");
			return false;
		}
		System.out.println("ALL VERTICES MATCH");
		return true;
	}
	
	
	
	
	
	
	private void verifyLabels(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList, boolean isValidText) {
		if(isValidText == true) {
			verifyGCVList(targetList, newTargetList, MIN_LABEL_SCORE_WHEN_VALID_TEXT, MIN_ACCEPTED_LABEL_MATCH_WHEN_VALID_TEXT, ImageChange.LABEL);
		}else {			
			verifyGCVList(targetList, newTargetList, MIN_LABEL_SCORE, MIN_ACCEPTED_LABEL_MATCH, ImageChange.LABEL);
		}
	}
	private void verifyWebEntities(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList, boolean isValidText) {
		if(isValidText == true) {
			verifyGCVList(targetList, newTargetList, MIN_WEB_SCORE_WHEN_VALID_TEXT, MIN_ACCEPTED_WEB_MATCH_WHEN_VALID_TEXT, ImageChange.WEBENTITIES);
		}else {			
			verifyGCVList(targetList, newTargetList, MIN_WEB_SCORE, MIN_ACCEPTED_WEB_MATCH, ImageChange.WEBENTITIES);
		}
	}

	private void verifyGCVList(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList, double minScore ,double minMatchPercentage, ImageChange change) {
		ArrayList<GCVResult> validTargetList = createListOfValidResults(targetList, minScore);
		ArrayList<GCVResult> validNewList = createListOfValidResults(newTargetList, minScore);
		
		int maxMatches = validTargetList.size() > validNewList.size() ? validTargetList.size() : validNewList.size();
		
		int matches = 0;
		for(GCVResult t : validTargetList) {
			for(GCVResult nt : validNewList) {
//				System.out.println("COMPARING DESCRIPTIONS: " + t.getDescription() + " AGAINST -> " + nt.getDescription());
				if(t.getDescription().equalsIgnoreCase(nt.getDescription())) {
					matches++;
					break;
				}
			}
		}
//		DecimalFormat matchPercentage = new DecimalFormat(".##");
//		matchPercentage = 
		double matchPercentage = (double)((double)matches/(double)maxMatches); //matches / maxMatches;
		System.out.println("MATCHES (" + matches + "/" + maxMatches + ")");
		System.out.println("MATCHING PERCENTAGE - " + change.name() + " : " + matchPercentage + " Threshold : " + minMatchPercentage);
		if(matchPercentage < minMatchPercentage) {
			changes.add(change);
		}
		if(change.equals(ImageChange.LABEL) && matchPercentage >= CONFIDENT_LABEL_THRESHOLD) {
			veryConfidentLabelMatch = true;
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
