package examensarbete.model.test;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public interface TestImage {
	
	Image getImage();
	
	int getImageWidth();
	int getImageHeight();
	
	String toString();
	String getImagePath();
	void setImagePath(String imagePath);
	
	Point getPosition();
	void setPosition(Point position);
	
	Rectangle getBounds();	
}
