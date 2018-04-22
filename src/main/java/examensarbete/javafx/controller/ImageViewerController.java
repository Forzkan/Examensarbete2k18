package examensarbete.javafx.controller;

import java.awt.Point;
import java.net.URL;

import examensarbete.model.test.TestImage;
import examensarbete.model.utility.FileUtility;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ImageViewerController {

	// FXML VARIABLES.
	// Using a Pane because it does not layout its children.
	@FXML
	private AnchorPane rootPane;
	@FXML
	private AnchorPane buttonPane;
	@FXML
	private Pane imagePane;
	@FXML
	private ImageView imageView;
	
	// The image to be displayed.
	private TestImage image;
	private Point newCoordinates;
	private Circle circle;
	
	private boolean onlyDisplayImage = false;
	private String imagePath = "";
	
	@FXML
	public void initialize() {
		
			imageView.setSmooth(true);
			// Add image to imageview, with proper sizes.
			if(onlyDisplayImage == false) {
				URL imageURL = FileUtility.getImageUrlFromPath(imagePath);
				imageView.setImage(new Image(imageURL.toString()));
								
				imageView.setFitWidth(image.getImageWidth());
				imageView.setFitHeight(image.getImageHeight());
				
				// The image should always be the actual size.
				imagePane.setMinWidth(image.getImageWidth());
				imagePane.setPrefWidth(image.getImageWidth());
				imagePane.setMaxWidth(image.getImageWidth());
				
				imagePane.setMinHeight(image.getImageHeight());
				imagePane.setPrefHeight(image.getImageHeight());
				imagePane.setMaxHeight(image.getImageHeight());
				
				rootPane.setMinWidth(image.getImageWidth());
				rootPane.setPrefWidth(image.getImageWidth());
				rootPane.setMaxWidth(image.getImageWidth());
				System.out.println(buttonPane.getWidth() + " | " + 50);
				rootPane.setMinHeight(image.getImageHeight() + 50);
				rootPane.setPrefHeight(image.getImageHeight() + 50);
			}else {
				imageView = otherImgView;
			}


			
			if(onlyDisplayImage == false) {	
				// draw the current click position.
				circle = new Circle();
				circle.setRadius(6);
				circle.setStrokeWidth(2);
				circle.setStroke(Paint.valueOf("red"));
				circle.setFill(Paint.valueOf("#1e92ff00"));
				
				// Create coordinates to place it.
				int x = image.getImageWidth() / 2;
				int y = image.getImageHeight() / 2;
				circle.setCenterX(x);
				circle.setCenterY(y);
				newCoordinates = new Point();
				newCoordinates.setLocation((image.getImageWidth() / 2),
										    (image.getImageHeight() / 2));
				imagePane.getChildren().add(circle);
				imageView.setOnMouseMoved(event -> {
		//			System.out.println(event.getX() + " || " + event.getY());
		//			System.out.println(image.getCoordinates().getX() + event.getX() + " || " +image.getCoordinates().getY() + event.getY());
				});
				
				// Add OnClick Listener to the image view.
				imageView.setOnMouseClicked(event -> {
					// Update circle position.
					circle.setCenterX(event.getX());
					circle.setCenterY(event.getY());
		//			newCoordinates.setLocation((int)image.getCoordinates().getX() + event.getX(), (int)image.getCoordinates().getY() + event.getY());
					newCoordinates.setLocation(event.getX(), event.getY()); // We are only after the offset for now.
					
				});
			}
	}
	
	public ImageViewerController(TestImage image) {
		this.image = image;
		this.imagePath = image.getImagePath();
	}
	
	
	public ImageViewerController(String imagePath, boolean onlyDisplayImage) {
		this.imagePath = imagePath;
		this.onlyDisplayImage = onlyDisplayImage;
	}
	
	
	private ImageView otherImgView;
	public ImageViewerController(ImageView otherImgView, boolean onlyDisplayImage) {
		this.otherImgView = otherImgView;
		this.onlyDisplayImage = onlyDisplayImage;
	}

	@FXML
	private void onOKButton() {
		if(onlyDisplayImage == false) {
			image.setClickOffset(newCoordinates);
		}
		((Stage) rootPane.getScene().getWindow()).close();
	}
	
	@FXML
	private void onCancelButton() {
		// Close the window, and let the existing (default - center of image) coordinates be set.
		// pretty much do nothing.
		((Stage) rootPane.getScene().getWindow()).close();
	}
	
}
