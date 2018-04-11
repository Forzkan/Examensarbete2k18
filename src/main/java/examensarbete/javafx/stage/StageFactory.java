package examensarbete.javafx.stage;

import java.io.IOException;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageFactory {

	private final static String preferencesFXML = "fxml/Preferences.fxml";
	private final static String mainFXML = "fxml/ActionWorkArea.fxml";
	private final static String newTestFXML = "fxml/NewTestDialog.fxml";
	private final static String imageViewerFXML = "fxml/ImageViewer.fxml";

	public Stage openStage(TTStage tt_stage, Object controller) {
		switch (tt_stage) {
		case MAIN:
			return openStage(mainFXML, "TT", controller);

		case NEW_TEST:
			return openStage(newTestFXML, "Create New Test", controller);

		case PREFERENCES:
			return openStage(preferencesFXML, "Preferences", controller);
		case IMAGEVIEWER:
			return openStage(imageViewerFXML, "Image Viewer - Set click position.", controller);

		default:
			throw new NullPointerException("Invalid type of TTStage. Cannot open stage.");
		}
	}

	private Stage openStage(String fxmlPath, String title, Object controller) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
			fxmlLoader.setController(controller);
			Parent parent = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
			return stage;
		} catch (IOException e) {
			System.out.println("Could not open: " + fxmlPath + ".");
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	private Stage getNonOpenedStage(String fxmlPath, String title, Object controller) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
			fxmlLoader.setController(controller);
			Parent parent = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			return stage;
		} catch (IOException e) {
			System.out.println("Could not open: " + fxmlPath + ".");
			System.out.println(e.getMessage());
		}
		return null;
	}

	public Stage getStage(TTStage tt_stage, Object controller) {
		switch (tt_stage) {
		case MAIN:
			return getNonOpenedStage(mainFXML, "TT", controller);
		case NEW_TEST:
			return getNonOpenedStage(newTestFXML, "Create New Test", controller);
		case PREFERENCES:
			return getNonOpenedStage(preferencesFXML, "Preferences", controller);
		case IMAGEVIEWER:
			return getNonOpenedStage(imageViewerFXML, "Image Viewer - Set click position.", controller);

		default:
			throw new NullPointerException("Invalid type of TTStage. Cannot open stage.");
		}
	}
}
