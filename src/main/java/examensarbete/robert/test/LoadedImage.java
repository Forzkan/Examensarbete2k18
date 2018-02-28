package examensarbete.robert.test;

import java.io.File;

import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;

public class LoadedImage {

	private BatchAnnotateImagesResponse responses;
	private File image;
	
	public LoadedImage(File image) {
		this.image = image;
	}
	
	
	public BatchAnnotateImagesResponse getResponses() {
		return responses;
	}
	public void setResponses(BatchAnnotateImagesResponse responses) {
		this.responses = responses;
	}
	public File getImage() {
		return image;
	}

	
	public boolean hasFeatures() {
		
		if(responses != null) {
			return true;
		}
		return false;
	}

	
	
}
