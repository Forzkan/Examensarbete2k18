package examensarbete.robert.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

//import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
//import javafx.stage.Screen;
import javafx.stage.Stage;
//Imports the Google Cloud client library

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
//import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import examensarbete.google.cloud.vision.GCVImageResult;
import examensarbete.model.jsonHandler.JsonHandler;
import examensarbete2k18.model.properties.PropertiesHandler;
import examensarbete2k18.model.properties.TTProperties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//import javax.imageio.ImageTypeSpecifier;


public class TestController {
	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private AnchorPane mainMenuPane;

	@FXML
	private VBox informationVBox;

	@FXML
	private AnchorPane footerPane;

	@FXML
	private AnchorPane headerPane;

	@FXML
	private Text selectedFolderPath;

	@FXML
	private Button mainMenuBtn111;

	@FXML
	private Text numerOfImagesInFolder;

	@FXML
	private Button selectFolderButton;

	@FXML
	private Button pressLeft;

	@FXML
	private Button pressRight;

	@FXML
	private Text imageName;

	@FXML
	private ImageView currentImage;
	private int currentImageIndex = 0;
	GoogleCloudVision gCloudVision;
	
	@FXML
	private Button runButton;
	
	
	Stage primaryStage;
	PropertiesHandler propertiesHandler;
	
	public TestController(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.gCloudVision = new GoogleCloudVision();
		PropertiesHandler.InitializePropertiesHandler();
	}

	private File selectedDirectory;

	
	
	@FXML
	void selectFolderPressed(ActionEvent event) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Robert 2k18");
		File defaultDirectory = new File(PropertiesHandler.properties.getProperty(TTProperties.DEFAULT_SELECT_DIR.toString()));
		chooser.setInitialDirectory(defaultDirectory);

		selectedDirectory = chooser.showDialog(primaryStage);
		
		if(selectedDirectory != null) {
			selectedFolderPath.setText(selectedDirectory.getAbsolutePath());
			onFolderSelected();
		}
		


	}

	private ArrayList<LoadedImage> imageFiles = new ArrayList<LoadedImage>();
	
	private void onFolderSelected() {
		int counter = 0;
		File[] directoryListing = selectedDirectory.listFiles();
		if (directoryListing != null) {

			for (File child : directoryListing) {

				if (!child.isDirectory()) {
					// Do something with child
					System.out.println(child.getName());
					System.out.println(child.getAbsoluteFile());
					
					String[] splitName = child.getName().split("\\.");
					int index = 1;
					if(splitName.length > 1) {
						index = splitName.length -1;
					}
					String fileEnding = splitName[index];
					if (isAllowedFileEnding(fileEnding)) {
						counter++;
						imageFiles.add(new LoadedImage(child));
					}
				}
			}
		}
		setDisplayedImage();
		numerOfImagesInFolder.setText("" + counter);
		
	}
	
	private File displayedImage;

	private boolean isAllowedFileEnding(String fe) {
		switch (fe) {
		case "png":
			break;
		case "jpeg":
			break;
		case "jpg":
			break;
		case "PNG":
			break;
		default:
			return false;
		}
		return true;
	}
	
	
	private void setDisplayedImage() {
		if(imageFiles.size() > 0) {
			Image img;
			try {
				img = new Image(imageFiles.get(currentImageIndex).getImage().toURI().toURL().toString());
				currentImage.setImage(img);
				imageName.setText(imageFiles.get(currentImageIndex).getImage().getName() + " (" + (currentImageIndex+1) + "/" + imageFiles.size() +") ");
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}
			displayedImage = imageFiles.get(currentImageIndex).getImage();
		}
	}
	
	private void displayCurrentImageFeatures() {
		informationVBox = new VBox();
		displayVisionResponseForCurrentImage();
	}
	
	@FXML
	private void leftButtonPressed() {
		if(currentImageIndex >= 1) {
			currentImageIndex--;
			setDisplayedImage();
			displayCurrentImageFeatures();
		}
	}
	
	@FXML
	private void rightButtonPressed() {
		if(currentImageIndex < imageFiles.size() - 1) {
			currentImageIndex++;
			setDisplayedImage();
			displayCurrentImageFeatures();
		}
	}
	
	@FXML
	private void runButtonPressed() {
		GCVImageResult result = new GCVImageResult();
		result.setImagePath(getPathToDisplayedImage());
		
		try{
//			testCloudVisionAPI();
//			gCloudVision.detectWebDetections(getPathToDisplayedImage(), System.out);			
			
			
			// TEMPORARILY COMMENTED OUT
//			gCloudVision.detectCropHints(getPathToDisplayedImage(), System.out,result);
//			gCloudVision.detectLogos(getPathToDisplayedImage(), System.out,result);
//			gCloudVision.detectProperties(getPathToDisplayedImage(), System.out,result);
//			gCloudVision.detectText(getPathToDisplayedImage(), System.out,result);
//			gCloudVision.detectWebEntities(getPathToDisplayedImage(), System.out,result);
//			gCloudVision.detectLabels(getPathToDisplayedImage(), System.out,result);
			
			JsonHandler handler = new JsonHandler();
			GCVImageResult loadedResult = handler.readGCVImageResult(PropertiesHandler.properties.getProperty(TTProperties.DEFAULT_SELECT_DIR.toString()) + "\\GCV_TEST.json");
			handler.saveGCVImageResult(PropertiesHandler.properties.getProperty(TTProperties.DEFAULT_SELECT_DIR.toString()), "GCV_TEST_LOADED", loadedResult);

		}catch(Exception e) {
			System.out.println("SOMETHING WENT WRONG WITH THE CLOUD VISION API.");
			System.out.println(e.getMessage());
		}

	}

	
	private String getPathToDisplayedImage() {
		return displayedImage.getAbsolutePath().toString();
	}
	
	
	// https://cloud.google.com/vision/docs/detecting-web
	@SuppressWarnings("unused")
	private void testCloudVisionAPI() throws IOException, Exception {
		
		if (imageFiles.size() > 0) {
			try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

				// The path to the image file to annotate
				String fileName = getPathToDisplayedImage();
				System.out.println("THE FILENAME AND PATH IS:" + fileName);
				// Reads the image file into memory
				Path path = Paths.get(fileName);
				byte[] data = Files.readAllBytes(path);
				ByteString imgBytes = ByteString.copyFrom(data);

				// Builds the image annotation request
				List<AnnotateImageRequest> requests = new ArrayList<>();
				com.google.cloud.vision.v1.Image img = com.google.cloud.vision.v1.Image.newBuilder()
						.setContent(imgBytes).build();
				Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
				AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img)
						.build();
				requests.add(request);

				// Performs label detection on the image file
				BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
				
				addVisionResponseToImage(response);
				displayVisionResponseForCurrentImage();
//				List<AnnotateImageResponse> responses = response.getResponsesList();
//				if(responses != null) {
//					
//				}
//				for (AnnotateImageResponse res : responses) {
//					if (res.hasError()) {
//						System.out.printf("Error: %s\n", res.getError().getMessage());
//						return;
//					}
//
//					for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
//						annotation.getAllFields().forEach((k, v) -> System.out.printf("%s : %s\n", k, v.toString()));
//					}
//				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	
	private void addVisionResponseToImage(BatchAnnotateImagesResponse response) {
		// Add to current image. 
		imageFiles.get(currentImageIndex).setResponses(response);	
	}
	
//	 System.out.printf("%s : %s\n", k, v.toString())
	private void displayVisionResponseForCurrentImage() {
		if(imageFiles.get(currentImageIndex).hasFeatures()) {
			List<AnnotateImageResponse> responses = imageFiles.get(currentImageIndex).getResponses().getResponsesList();
			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					System.out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}
	
				for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
					
					annotation.getAllFields().forEach((k, v) -> {
						Pane pane = new Pane();
						Text text = new Text();
						text.setText(k + " : " + v);
						pane.getChildren().add(text);
//						System.out.printf("%s : %s\n", k, v.toString());
						informationVBox.getChildren().add(pane);
					});
				}
			}
		}
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//
