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
	private Point position;
	@Override
	public Point getPosition() {
		return position;
	}
	@Override
	public void setPosition(Point position) {
		this.position = position;
	}
	
	
	private String imagePath;
	@Override
	public String getImagePath() {
		return imagePath;
	}
	@Override
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
		setImage(imagePath);
	}
	
	
	// OVERWRITTEN METHODS WHICH ARE NOT SAVED.	
	@JsonIgnore
	private BufferedImage image;
	
	@Override
	public Image getImage() {
		if(image == null) {
			setImage(imagePath);
		}
		return (Image)image;
	}
	
	@Override
	public int getImageWidth() {
		return image.getWidth();
	}
	
	@Override
	public int getImageHeight() {
		return image.getHeight();
	}

	
	@JsonIgnore
	@Override
	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, getImageWidth(), getImageHeight());
	}
	
	
	
	// Private help methods.
	private void setImage(String imagePath) {
		try {
			File img = new File(imagePath);
			image = ImageIO.read(img);
		} catch (Exception e) {
			System.out.println("Error when creating image from path: " + imagePath);
		}
	}



	// Empty Construct for Jackson.. TODO:: Consider using Lombok.
	public TestImageImpl() {}
	



}
