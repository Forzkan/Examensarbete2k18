package examensarbete.model.test;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class TestImageImpl implements TestImage{

	private String path;
	private int x;
	private int y;
	private int width;
	private int height;
	
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

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	
	public Rectangle getBounds() {
		return new Rectangle(x,y,width,height);
	}
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public Point getImageCoordinates() {
		return new Point(x,y);
	}
	
	
	public TestImageImpl() {
		
	}
	
	
	
	
	@Override
	public String pathToImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getImageHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getImageWidth() {
		// TODO Auto-generated method stub
		return 0;
	}



}
