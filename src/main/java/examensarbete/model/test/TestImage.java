package examensarbete.model.test;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import examensarbete.google.cloud.vision.GCVImageResult;

public interface TestImage {
	
	Image getImage();
	
	int getImageWidth();
	int getImageHeight();
	
	GCVImageResult getImageGCVResults();
	void setImageGCVResults(GCVImageResult imageGCVResults);
	
	
	String getImagePath();
	void setImagePath(String imagePath);
	
	Point getCoordinates();
	void setCoordinates(Point position);
	
	Point getClickOffset();
	void setClickOffset(Point clickOffset);

	Point getCoordinateOffset();
	void setCoordinateOffset(Point coordinateOffset);
	
	
	
	Rectangle getBounds();

	int getResolutionX();
	void setResolutionX(int resolutionX);
	
	int getResolutionY();
	void setResolutionY(int resolutionY);
	
	String toString();
	
	boolean compareTestImage(TestImage imageToCompare);

	Point getClickCoordinates();

	Point getImageScreenCoordinates();

	String getFullImagePath();



	
	
	
}
