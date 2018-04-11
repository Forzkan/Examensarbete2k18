package examensarbete.google.cloud.vision;

import java.util.ArrayList;


public class GCVImageResult {

	private String imagePath;
	
	private OpenCVShape openCVShape;
	private DominantGCVColor dominantColor;
	private DominantGCVColor secondaryColor;
	private DominantGCVColor thirdColor;
	
	private ArrayList<GCVResult> labelResults;
	private ArrayList<GCVResult> webResults;
	private ArrayList<GCVResult> propertiesResults;
	private ArrayList<GCVResult> webEntitiesResults;
	private ArrayList<GCVResult> textResults;
	private ArrayList<GCVResult> logoResults;
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public OpenCVShape getOpenCVShape() {
		return openCVShape;
	}
	public void setOpenCVShape(OpenCVShape openCVShape) {
		this.openCVShape = openCVShape;
	}
	public DominantGCVColor getDominantColor() {
		return dominantColor;
	}
	public void setDominantColor(DominantGCVColor dominantColor) {
		this.dominantColor = dominantColor;
	}
	public DominantGCVColor getSecondaryColor() {
		return secondaryColor;
	}
	public void setSecondaryColor(DominantGCVColor secondaryColor) {
		this.secondaryColor = secondaryColor;
	}
	public DominantGCVColor getThirdColor() {
		return thirdColor;
	}
	public void setThirdColor(DominantGCVColor thirdColor) {
		this.thirdColor = thirdColor;
	}
	public ArrayList<GCVResult> getLabelResults() {
		return labelResults;
	}
	public void setLabelResults(ArrayList<GCVResult> labelResults) {
		this.labelResults = labelResults;
	}
	public ArrayList<GCVResult> getWebResults() {
		return webResults;
	}
	public void setWebResults(ArrayList<GCVResult> webResults) {
		this.webResults = webResults;
	}
	public ArrayList<GCVResult> getPropertiesResults() {
		return propertiesResults;
	}
	public void setPropertiesResults(ArrayList<GCVResult> propertiesResults) {
		this.propertiesResults = propertiesResults;
	}
	public ArrayList<GCVResult> getWebEntitiesResults() {
		return webEntitiesResults;
	}
	public void setWebEntitiesResults(ArrayList<GCVResult> webEntitiesResults) {
		this.webEntitiesResults = webEntitiesResults;
	}
	public ArrayList<GCVResult> getTextResults() {
		return textResults;
	}
	public void setTextResults(ArrayList<GCVResult> textResults) {
		this.textResults = textResults;
	}
	public ArrayList<GCVResult> getLogoResults() {
		return logoResults;
	}
	public void setLogoResults(ArrayList<GCVResult> logoResults) {
		this.logoResults = logoResults;
	}
	
		
	
	
	
}
