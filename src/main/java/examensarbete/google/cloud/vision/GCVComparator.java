package examensarbete.google.cloud.vision;

import java.awt.Point;
import java.util.ArrayList;

import org.opencv.core.Mat;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import examensarbete.model.test.MatchType;

public class GCVComparator implements ValidChangeAlgorithm{
	
	
	private double MIN_ACCEPTED_TEXT_MATCH = 0.5; // 50%.
	private double MIN_TEXT_SCORE = 0.0; // 0%.
	private boolean allowNewText = false; // a page with several similar buttons could probably big a huuuge mess to deal with if the one button we are after gets removed, or gets a completely new text.
	
	
	private double MIN_ACCEPTED_WEB_MATCH = 0.40; // 70%.
	private double MIN_WEB_SCORE = 0.50;
	
	private double MIN_ACCEPTED_LABEL_MATCH = 0.60; // 70%.
	private double MIN_LABEL_SCORE = 0.65; 
	
	
	private double MIN_ACCEPTED_WEB_MATCH_WHEN_VALID_TEXT = 0.35; // 70%.
	private double MIN_ACCEPTED_LABEL_MATCH_WHEN_VALID_TEXT = 0.35; // 70%.
	
	private double MIN_WEB_SCORE_WHEN_VALID_TEXT = 0.25;
	private double MIN_LABEL_SCORE_WHEN_VALID_TEXT = 0.25; 
	

	private double CONFIDENT_LABEL_THRESHOLD = 0.75; // if over 75% matches, then it is considered to be a very confident label match.
	private boolean veryConfidentLabelMatch = false;
	
	// RULES, 
	// DIFFERENT TEXT = NOK.
	// SAME TEXT, DIFFERENT VERTICES = OK, BUT THEN AT LEAST THE MAIN COLOR SHOULD BE THE SAME,
	// .. AND THE LABEL + WEBENTITIESRESULTS MUST BE OK.
	// 
	
	private static ArrayList<ImageChange> changes;
	
	@Override
	public MatchType isValidChange(GCVImageResult target, GCVImageResult newTarget) {
		// Set all the gcv parameters from the preferences window.
		setGCVParameters();
		
		changes = new ArrayList<ImageChange>();
//		System.out.println("");
		verifyColor(target, newTarget);
		
		verifyText(target.getTextResults(), newTarget.getTextResults());
		
		boolean isValidText = !changes.contains(ImageChange.INVALID_TEXTCHANGE);
		
		verifyLabels(target.getLabelResults(), newTarget.getLabelResults(), isValidText);
		
		verifyWebEntities(target.getWebEntitiesResults(), newTarget.getWebEntitiesResults(), isValidText);
		
		verifyLogos(target.getLogoResults(), newTarget.getLogoResults());
		
		return analyzeIfValidChanges();
	}
	
	
	public boolean performTextMatch(GCVImageResult target, GCVImageResult newTarget) {
		changes = new ArrayList<ImageChange>();
		verifyAllText(target.getTextResults(), newTarget.getTextResults());
		if(changes.contains(ImageChange.INVALID_TEXTCHANGE)) {
			return false;
		}
		return true;
	}
	
	public boolean isValidGCVMatchType(MatchType match) {
		if(match == MatchType.NO_MATCH) {
			return false;
		}
		return true;
	}
	

	private void setGCVParameters() {
		MIN_ACCEPTED_TEXT_MATCH = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minTextMatch.toString())) / 100;
		MIN_TEXT_SCORE =  Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minTextScore.toString())) / 100;
//		private final boolean allowNewText = false; // a page with several similar buttons could probably big a huuuge mess to deal with if the one button we are after gets removed, or gets a completely new text.
		
		
		MIN_ACCEPTED_WEB_MATCH =  Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minWebMatch.toString())) / 100;
		MIN_WEB_SCORE = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minWebScore.toString())) / 100;
		
		MIN_ACCEPTED_LABEL_MATCH = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minLabelMatch.toString())) / 100;
		MIN_LABEL_SCORE =  Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.minLabelScore.toString())) / 100;
		
		
		MIN_ACCEPTED_WEB_MATCH_WHEN_VALID_TEXT = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minWebMatch.toString())) / 100;
		MIN_ACCEPTED_LABEL_MATCH_WHEN_VALID_TEXT = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minLabelMatch.toString())) / 100;
		
		MIN_WEB_SCORE_WHEN_VALID_TEXT = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minWebScore.toString())) / 100;
		MIN_LABEL_SCORE_WHEN_VALID_TEXT = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.wvt_minLabelScore.toString())) / 100; 
		

		CONFIDENT_LABEL_THRESHOLD = Double.parseDouble(PropertiesHandler.properties.getProperty(TTProperties.confidentLabel.toString())) / 100; 
//		System.out.println();
	}



	private void verifyLogos(ArrayList<GCVResult> logoResults, ArrayList<GCVResult> newLogoResults) {
		
		for(GCVResult r : logoResults ) {
//			System.out.println("LOGO: " + r.getDescription() + " SCORE: " + r.getScore());
		}
		for(GCVResult r : newLogoResults ) {
//			System.out.println("LOGO: " + r.getDescription() + " SCORE: " + r.getScore());
		}
//		System.out.println("LOGO RESULTS: ");  // Currently not using logo results because it is not sending back any responses that often... IT EVEN HAD PROBLEMS WITH A YOUTUBE LOGO....... ?!
		
	}



	private MatchType analyzeIfValidChanges() {
		System.out.println("GCV ALGORITHM IN PROGRESS....");
		MatchType change = MatchType.MATCH;
		int changeCounter = 0;

		// TODO:: First attempt, the logic here should be well thought through.
		if(changes.contains(ImageChange.INVALID_TEXTCHANGE) && allowNewText == false) {
			System.out.println("GCV ALGORITHM DONE....");
			return MatchType.NO_MATCH;
		}
		if(changes.contains(ImageChange.MAINCOLOR)  || 
			changes.contains(ImageChange.SECONDARYCOLOR) ||
			changes.contains(ImageChange.THIRDCOLOR)) {
			changeCounter ++;
			change = MatchType.COLOR_CHANGED_MATCH;
		}
		if(changes.contains(ImageChange.FONT)) {
			changeCounter ++;
			change = MatchType.TEXTSTYLE_CHANGED_MATCH;
		}
		if(changes.contains(ImageChange.TEXT)) {
			changeCounter ++;
			change = MatchType.TEXT_CHANGED_MATCH;
		}
		if(changes.contains(ImageChange.LABEL)) {
			changeCounter ++;
			change = MatchType.LABEL_CHANGED_MATCH;
		}
		if(changes.contains(ImageChange.WEBENTITIES)) {
			if(veryConfidentLabelMatch == false) { // if true, leave out the results of web entities, because it seems to vary a lot between very similar images
												   // otherwise... add a change to the counter..
				changeCounter ++;
				change = MatchType.WEB_CHANGED_MATCH;
			}
		}
		if(changes.contains(ImageChange.FONT) && 
				(changes.contains(ImageChange.MAINCOLOR) || 
				 changes.contains(ImageChange.SECONDARYCOLOR) || 
				 changes.contains(ImageChange.THIRDCOLOR))) {
			changeCounter--; // Colorchanges seems to actually have an effect on the vertices, and font changes may for sure have an effect on colors changing..
			return MatchType.TEXTSTYLE_CHANGED_MATCH;
		}
		if(changeCounter > 1) {
			return MatchType.NO_MATCH;
		}
		System.out.println("GCV ALGORITHM DONE.");
		return change;
//		return changeCounter <= 1;
	}
	

	private void verifyColor(GCVImageResult target, GCVImageResult newTarget) {
		// Main color.
		if(isSameColorValues(target.getDominantColor(), newTarget.getDominantColor()) == false) {
			changes.add(ImageChange.MAINCOLOR);
//			System.out.println("MAIN COLOR HAS CHANGED");
		}
		// Secondary color.
		if(isSameColorValues(target.getSecondaryColor(), newTarget.getSecondaryColor()) == false) {
			changes.add(ImageChange.SECONDARYCOLOR);
//			System.out.println("SECONDARY COLOR HAS CHANGED");
		}
		// Third color.
		if(isSameColorValues(target.getThirdColor(), newTarget.getThirdColor()) == false) {
			changes.add(ImageChange.THIRDCOLOR);
//			System.out.println("THIRD COLOR HAS CHANGED");
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
	
	
	private void verifyAllText(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList) {
		int foundCounter = 0;
		ArrayList<GCVResult> validTargetList = createListOfValidResults(targetList, 0);
		ArrayList<GCVResult> validNewTargetList = createListOfValidResults(newTargetList, 0);
		
		
		for(GCVResult r : validNewTargetList) {
			for(GCVResult tr : validTargetList) {
//				System.err.println(r.getDescription() + " - <||> - " + tr.getDescription());
				if(r.getDescription().toString().equalsIgnoreCase(tr.getDescription())) {
					if(verifyTextVertices(tr, r) == false) {
						changes.add(ImageChange.FONT);
					}
					foundCounter++;
					break;
				}
			}
		}
		if(validTargetList.size() > 0 &&  validNewTargetList.size() > 0) {
			int maxMatches = validTargetList.size() > validNewTargetList.size() ? validTargetList.size() : validNewTargetList.size();
			double matchPercentage = (double)((double)foundCounter/ (double)maxMatches);
			if(matchPercentage < MIN_ACCEPTED_TEXT_MATCH) {
				System.out.println("TEXT MATCH FAILED - "+matchPercentage + " / " + MIN_ACCEPTED_TEXT_MATCH);
				changes.add(ImageChange.INVALID_TEXTCHANGE);
			}
		}else if((validTargetList.size() > 0 && validNewTargetList.size() == 0) || (validTargetList.size() == 0 && validNewTargetList.size() > 0)) {
			changes.add(ImageChange.INVALID_TEXTCHANGE);
		}
	}
	
	private void verifyText(ArrayList<GCVResult> targetList, ArrayList<GCVResult> newTargetList) {
		int foundCounter = 0;
		ArrayList<GCVResult> validTargetList = createListOfValidResults(targetList, MIN_TEXT_SCORE);
		ArrayList<GCVResult> validNewTargetList = createListOfValidResults(newTargetList, MIN_TEXT_SCORE);
		
		
		for(GCVResult r : validNewTargetList) {
			for(GCVResult tr : validTargetList) {
//				System.err.println(r.getDescription() + " - <||> - " + tr.getDescription());
				if(r.getDescription().toString().equalsIgnoreCase(tr.getDescription())) {
					if(verifyTextVertices(tr, r) == false) {
						changes.add(ImageChange.FONT);
					}
					foundCounter++;
					break;
				}
			}
		}
		if(validTargetList.size() > 0 &&  validNewTargetList.size() > 0) {
			int maxMatches = validTargetList.size() > validNewTargetList.size() ? validTargetList.size() : validNewTargetList.size();
			double matchPercentage = (double)((double)foundCounter/ (double)maxMatches);
			if(matchPercentage < MIN_ACCEPTED_TEXT_MATCH) {
				System.out.println("TEXT MATCH FAILED - "+matchPercentage + " / " + MIN_ACCEPTED_TEXT_MATCH);
				changes.add(ImageChange.INVALID_TEXTCHANGE);
			}
		}else if((validTargetList.size() > 0 && validNewTargetList.size() == 0) || (validTargetList.size() == 0 && validNewTargetList.size() > 0)) {
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
//			System.out.println("ALL VERTICES DO NOT MATCH");
			return false;
		}
//		System.out.println("ALL VERTICES MATCH");
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
		
		System.out.println("MATCHING PERCENTAGE - " + change.name() + " : " + matchPercentage + " Threshold : " + minMatchPercentage);
		System.out.println("MATCHES (" + matches + "/" + maxMatches + ")");
		
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
