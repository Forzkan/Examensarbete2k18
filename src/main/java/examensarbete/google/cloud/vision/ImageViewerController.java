package examensarbete.google.cloud.vision;

import java.awt.Point;
import java.net.URL;

import examensarbete.model.test.TestImage;
import examensarbete.model.utility.FileUtility;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ImageViewerController {

	// FXML VARIABLES.
	// Using a Pane because it does not layout its children.
	@FXML
	private Pane rootPane, imagePane;
	@FXML
	private ImageView imageView;
	
	// The image to be displayed.
	private Stage stage;
	private TestImage image;
	private Point newCoordinates;
	private Circle circle;
	
	@FXML
	public void initialize() {
		
		imageView.setSmooth(true);
		// Add image to imageview, with proper sizes.
		URL imageURL = FileUtility.getImageUrlFromPath(image.getImagePath());
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
		
		
		// draw the current click position.
		circle = new Circle();
		circle.setRadius(10);
		circle.setStyle("-fx-background-color : transparent;");
		circle.setStyle("-fx-border-color : red;");
		circle.setStyle("-fx-border-width : 3;");
		
		// Create coordinates to place it.
		int x = image.getImageWidth() / 2;
		int y = image.getImageHeight() / 2;
		circle.setCenterX(x);
		circle.setCenterY(y);
		
		imagePane.getChildren().add(circle);
		
		// Add OnClick Listener to the image view.
		imageView.setOnMouseClicked(event -> {
			// Update circle position.
			circle.setCenterX(event.getX());
			circle.setCenterY(event.getY());
		});
	}
	
	public ImageViewerController(Stage stage, TestImage image) {
		this.stage = stage;
		this.image = image;
	}
	
	
	@FXML
	private void onOKButton() {
		image.setCoordinates(newCoordinates);
		stage.close();
	}
	
	@FXML
	private void onCancelButton() {
		// Close the window, and let the existing (default - center of image) coordinates be set.
		// pretty much do nothing.
		// 
	}
	
}
