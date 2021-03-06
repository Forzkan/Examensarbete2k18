package examensarbete.google.cloud.vision;

import java.awt.Point;
import java.util.ArrayList;


public class GCVResult {
	private String description;
	private String mid;
	
	private float score;
	private float confident;
	private float topicality;
	
	private ArrayList<Point> verticesPoints = new ArrayList<Point>();
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getConfident() {
		return confident;
	}

	public void setConfident(float confident) {
		this.confident = confident;
	}

	public float getTopicality() {
		return topicality;
	}

	public void setTopicality(float topicallity) {
		this.topicality = topicallity;
	}

	
	public ArrayList<Point> getVerticesPoints() {
		return verticesPoints;
	}

	public void setVerticesPoints(ArrayList<Point> verticesPoints) {
		this.verticesPoints = verticesPoints;
	}

	public void addVerticesAsPoints(Point point) {
		verticesPoints.add(point);
	}

	
	
}
