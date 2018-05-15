package examensarbete.model.test;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import examensarbete.google.cloud.vision.GCVImageResult;
import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;
import examensarbete.model.utility.FileUtility;


public class TestImageImpl implements TestImage{

	// USED BY JACKSON, AND IS STORED IN JSON.
	private Point coordinates;
	private int resolutionX; 
	private int resolutionY;
	private String imagePath;
	private GCVImageResult imageGCVResults;
	private Point clickOffset;
	private Point coordinateOffset;
	
	// OVERWRITTEN METHODS WHICH ARE NOT SAVED AND THEREFORE NOT USED BY JACKSON.	
	@JsonIgnore 
	private Image image;
	
	// CONSTRUCTORS
	public TestImageImpl(String path, Point coordinates, int width, int height) {
		setImagePath(path);
		this.coordinates = coordinates;
//		this.width = width;
//		this.height = height;
		initializePoints();
	}
	
	/**
	 * Accepts a buffered image.
	 * @param image
	 * @param coordinates
	 */
	public TestImageImpl(BufferedImage image, Point coordinates) {
		this.image = image;
		
		setCoordinates(coordinates);
		initializePoints();
	}
	
	// GETTERS AND SETTERS, ALSO USED BY JACKSON.	
	@Override
	public GCVImageResult getImageGCVResults() {
		return imageGCVResults;
	}
	
	@Override
	public void setImageGCVResults(GCVImageResult imageGCVResults) {
		this.imageGCVResults = imageGCVResults;
	}
	

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
	@Override
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
	@Override
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
	public Point getClickOffset() {
		return clickOffset;
	}

	@Override
	public void setClickOffset(Point clickOffset) {
		this.clickOffset = clickOffset;
	}
	
	@Override
	public Point getCoordinateOffset() {
		return coordinateOffset;
	}

	@Override
	public void setCoordinateOffset(Point coordinateOffset) {
		this.coordinateOffset = coordinateOffset;
	}
	
	@JsonIgnore
	@Override
	public String getFullImagePath() {
		return PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + imagePath;
	}
	
	@Override
	public String getImagePath() {
		return /*FileUtility.getProjectRoot() + "//" +*/ imagePath;
	}

	@Override
	public void setImagePath(String imagePath) {
		this.imagePath = FileUtility.absoluteToRelativePath(imagePath); 
		setImage(this.imagePath);
	}
	
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


	/**
	 * Returns the bounds including a potential offset which is used for images not manually 'snapped'.
	 */
	@JsonIgnore
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)(coordinates.getX() + coordinateOffset.getX()), 
				             (int)(coordinates.getY() + coordinateOffset.getY()), 
				             getImageWidth(), 
				             getImageHeight());
	}
	
	
	@JsonIgnore
	@Override
	public Point getClickCoordinates() {
		Point p = new Point();
		p.setLocation(coordinates.getX() + coordinateOffset.getX() + clickOffset.getX(),
					  coordinates.getY() + coordinateOffset.getY() + clickOffset.getY());
		return p;
	}
	
	
	@JsonIgnore
	@Override
	public Point getImageScreenCoordinates() {
		Point p = new Point();
		p.setLocation(coordinates.getX() + coordinateOffset.getX(),
					  coordinates.getY() + coordinateOffset.getY());
		return p;
	}
	
	
	/**
	 * @return boolean - True if the images have identical dimensions and location 
	 */
	@Override
	public boolean compareTestImage(TestImage imageToCompare) {
		if(this.getImageScreenCoordinates().x == imageToCompare.getImageScreenCoordinates().x && 
			this.getImageScreenCoordinates().y == imageToCompare.getImageScreenCoordinates().y && 
			this.getImageWidth() == imageToCompare.getImageWidth() && 
			this.getImageHeight() == imageToCompare.getImageHeight()) {
			return true;
		}
		return false;
	}
	
	// PRIVATE HELP METHODS.
	private void setImage(String imagePath) {
		try {
			File img = new File(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + "//" + imagePath);
			image = ImageIO.read(img);
		} catch (Exception e) {
			System.out.println("Error when creating image from path: " + PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()) + "//" + imagePath);
		}
	}
	
	@Override
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

	// Empty Constructor for Jackson.. TODO:: Consider using Lombok.
	public TestImageImpl() {initializePoints();}

	
	private void initializePoints() {
		if(clickOffset == null) {
			clickOffset = new Point(0,0);
		}
		if(coordinateOffset == null) {
			coordinateOffset = new Point(0,0);
		}
		if(coordinates == null) {
			coordinates = new Point(0,0);
		}
	}


}
