package examensarbete.model.test;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class TestImageImpl implements TestImage{

	// USED BY JACKSON, AND IS STORED IN JSON.
	private Point coordinates;
	private int resolutionX; 
	private int resolutionY;
	private String imagePath;
	
	
	
	// GETTERS AND SETTERS, ALSO USED BY JACKSON.
	/**
	 * Gets the resolution X which the image was taken in.
	 */
	@Override
	public int getResolutionX() {
		return resolutionX;
	}
	/**
	 * Set the resolution X which the image was taken in.
	 * @param resolutionX
	 */
	public void setResolutionX(int resolutionX) {
		this.resolutionX = resolutionX;
	}
	
	/**
	 * Set the resolution Y which the image was taken in.
	 * @param resolutionX
	 */
	@Override
	public int getResolutionY() {
		return resolutionY;
	}
	/**
	 * Gets the resolution Y which the image was taken in.
	 */
	public void setResolutionY(int resolutionY) {
		this.resolutionY = resolutionY;
	}
	
	/**
	 * Gets the Image 0,0 position in the Screen coordinates position of the screen, 
	 * and with the resolution the image was taken in.
	 */
	@Override
	public Point getCoordinates() {
		return coordinates;
	}
	@Override
	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}
	
	
	@Override
	public String getImagePath() {
		return imagePath;
	}
	@Override
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
		setImage(imagePath);
	}
	
	
	
	
	// OVERWRITTEN METHODS WHICH ARE NOT SAVED AND THEREFORE NOT USED BY JACKSON.	
	@JsonIgnore 
	private Image image;
	
	@Override
	public Image getImage() {
		if(image == null) {
			setImage(imagePath);
		}
		return (Image)image;
	}
	
	@Override
	public int getImageWidth() {
		return ((BufferedImage)image).getWidth();
	}
	
	@Override
	public int getImageHeight() {
		return ((BufferedImage)image).getHeight();
	}

	@JsonIgnore
	@Override
	public Rectangle getBounds() {
		return new Rectangle(coordinates.x, coordinates.y, getImageWidth(), getImageHeight());
	}
	
	
	
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		String lineBreak = System.getProperty("line.separator");
		stringBuilder.append("TestImageImpl" + lineBreak);
		stringBuilder.append("x:" + coordinates.x + lineBreak);
		stringBuilder.append("y:" + coordinates.y + lineBreak);
		stringBuilder.append("width:" + getImageWidth() + lineBreak);
		stringBuilder.append("height:" + getImageHeight());
		return stringBuilder.toString();
	}
	
	
	
	
	// PRIVATE HELP METHODS.
	private void setImage(String imagePath) {
		try {
			File img = new File(imagePath);
			image = ImageIO.read(img);
		} catch (Exception e) {
			System.out.println("Error when creating image from path: " + imagePath);
		}
	}
	
	
	
	
	

	// CONSTRUCTORS
	public TestImageImpl(String path, Point coordinates, int width, int height) {
		setImagePath(path);
		this.coordinates = coordinates;
//		this.width = width;
//		this.height = height;
	}
	
	
	/**
	 * Accepts a buffered image.
	 * @param image
	 * @param coordinates
	 */
	public TestImageImpl(BufferedImage image, Point coordinates) {
		this.image = image;
		setCoordinates(coordinates);
	}
	
	
	// Empty Constructor for Jackson.. TODO:: Consider using Lombok.
	public TestImageImpl() {}

	


}