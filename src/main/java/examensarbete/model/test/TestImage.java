package examensarbete.model.test;

import java.awt.Image;
//import java.awt.Point;
import org.opencv.core.Point;

public interface TestImage {
	
	String pathToImage();
	Image getImage();
	int getWidth();
	int getHeight();
	Point getImageCoordinates();
	String toString();
}
