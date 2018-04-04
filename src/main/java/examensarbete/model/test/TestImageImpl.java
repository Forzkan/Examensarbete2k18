package examensarbete.model.test;

import java.awt.Image;
import java.awt.Point;
//import org.opencv.core.Point;
import java.awt.Rectangle;

public class TestImageImpl implements TestImage{

	private String path;
	private int x;
	private int y;
	private int width;
	private int height;
	private Point coordinates;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	
	public Rectangle fetchTheBounds() {
		return new Rectangle(x,y,width,height);
	}
	public void setsTheBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public Point getImageCoordinates() {
		return new Point(x,y);
	}
	
	
	public TestImageImpl(String path, Point coordinates, int width, int height) {
		this.path = path;
		this.coordinates = coordinates;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public String pathToImage() {
		return this.path;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		String lineBreak = System.getProperty("line.separator");
		stringBuilder.append("TestImageImpl" + lineBreak);
		stringBuilder.append("x:" + coordinates.x + lineBreak);
		stringBuilder.append("y:" + coordinates.y + lineBreak);
		stringBuilder.append("width:" + width + lineBreak);
		stringBuilder.append("height:" + height);
		return stringBuilder.toString();
	}

}
