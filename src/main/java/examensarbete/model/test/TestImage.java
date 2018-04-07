package examensarbete.model.test;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public interface TestImage {
	
	Image getImage();
	
	int getImageWidth();
	int getImageHeight();
	
	String getImagePath();
	void setImagePath(String imagePath);
	
	Point getCoordinates();
	void setCoordinates(Point position);
	
	Rectangle getBounds();

	int getResolutionX();
	void setResolutionX(int resolutionX);
	
	int getResolutionY();
	void setResolutionY(int resolutionY);
	
	String toString();
	
	boolean compareTestImage(TestImage imageToCompare);
	
}
