package opencvtest;

import java.awt.Image;
import java.awt.Point;

public interface TestImage {
	
	String pathToImage();
	Image getImage();
	int getImageHeight();
	int getImageWidth();
	Point getImageCoordinates();
}
