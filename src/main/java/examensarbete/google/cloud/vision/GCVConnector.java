package examensarbete.google.cloud.vision;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
//import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.ColorInfo;
//import com.google.cloud.vision.v1.CropHint;
//import com.google.cloud.vision.v1.CropHintsAnnotation;
import com.google.cloud.vision.v1.DominantColorsAnnotation;
import com.google.cloud.vision.v1.EntityAnnotation;
//import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.Vertex;
//import com.google.cloud.vision.v1.ImageContext;
//import com.google.cloud.vision.v1.ImageSource;
//import com.google.cloud.vision.v1.LocationInfo;
//import com.google.cloud.vision.v1.Page;
//import com.google.cloud.vision.v1.Paragraph;
//import com.google.cloud.vision.v1.SafeSearchAnnotation;
//import com.google.cloud.vision.v1.Symbol;
//import com.google.cloud.vision.v1.TextAnnotation;
import com.google.cloud.vision.v1.WebDetection;
import com.google.cloud.vision.v1.WebDetection.WebEntity;
import com.google.cloud.vision.v1.WebDetection.WebImage;
//import com.google.cloud.vision.v1.WebDetection.WebEntity;
import com.google.cloud.vision.v1.WebDetection.WebPage;
//import com.google.cloud.vision.v1.Word;

import com.google.protobuf.ByteString;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GCVConnector {

	
	
	public static GCVImageResult getGCVImageResult(String path) {
		GCVImageResult result = new GCVImageResult();
		result.setImagePath(path);
		try{
			// Connect, fetch and store all GCVResults in the GCVImageResult.
			//GCVConnector.detectCropHints(path,result);
			detectLogos(path,result);
			detectProperties(path,result);
			detectText(path,result);
			detectWebEntities(path,result);
			detectLabels(path,result);
		}catch(Exception e) {
			System.out.println("SOMETHING WENT WRONG WITH THE CLOUD VISION API.");
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * Detects labels in the specified local image.
	 *
	 * @param filePath
	 *            The path to the file to perform label detection on.
	 * @param out
	 *            A {@link PrintStream} to write detected labels to.
	 * @throws Exception
	 *             on errors while closing the client.
	 * @throws IOException
	 *             on Input/Output errors.
	 */
	public static void detectLabels(String filePath, GCVImageResult result) throws Exception, IOException {
		System.out.println("\nSTART LABEL DETECTION::\n ");

		List<AnnotateImageRequest> requests = new ArrayList<>();
		ArrayList<GCVResult> labelResults = new ArrayList<GCVResult>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
//					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}

				// For full list of available annotations, see http://g.co/cloud/vision/docs
				for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
//					annotation.getAllFields().forEach((k, v) -> out.printf("%s : %s\n", k, v.toString()));
					GCVResult gcvResult = new GCVResult();
					gcvResult.setScore(annotation.getScore());
					gcvResult.setConfident(annotation.getConfidence());
					gcvResult.setDescription(annotation.getDescription());
					gcvResult.setTopicality(annotation.getTopicality());
					labelResults.add(gcvResult);
				}
			}
		}result.setLabelResults(labelResults);
		System.out.println("\nEND LABEL DETECTION::\n ");
	}

	
	@SuppressWarnings("unused")
	public void detectWebDetections(String filePath, PrintStream out)
			throws Exception, IOException {
		System.out.println("\nSTART WEB DETECTION::\n ");

		List<AnnotateImageRequest> requests = new ArrayList<>();
		ArrayList<GCVResult> webResults = new ArrayList<GCVResult>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.WEB_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}

				// Search the web for usages of the image. You could use these signals later
				// for user input moderation or linking external references.
				// For a full list of available annotations, see http://g.co/cloud/vision/docs
				WebDetection annotation = res.getWebDetection();
				out.println("Entity:Id:Score");
				out.println("===============");
				for (WebEntity entity : annotation.getWebEntitiesList()) {
					out.println(entity.getDescription() + " : " + entity.getEntityId() + " : " + entity.getScore());
				}
				out.println("\nPages with matching images: Score\n==");
				for (WebPage page : annotation.getPagesWithMatchingImagesList()) {
					out.println(page.getUrl() + " : " + page.getScore());
				}
				out.println("\nPages with partially matching images: Score\n==");
				for (WebImage image : annotation.getPartialMatchingImagesList()) {
					out.println(image.getUrl() + " : " + image.getScore());
				}
				out.println("\nPages with fully matching images: Score\n==");
				for (WebImage image : annotation.getFullMatchingImagesList()) {
					out.println(image.getUrl() + " : " + image.getScore());
				}
			}
		}

		System.out.println("\nEND WEB DETECTION::\n ");
	}

	/**
	 * Detects text in the specified image.
	 *
	 * @param filePath
	 *            The path to the file to detect text in.
	 * @param out
	 *            A {@link PrintStream} to write the detected text to.
	 * @throws Exception
	 *             on errors while closing the client.
	 * @throws IOException
	 *             on Input/Output errors.
	 */
	public static void detectText(String filePath, GCVImageResult result) throws Exception, IOException {
		System.out.println("\nSTART TEXT DETECTION::\n ");
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
		ArrayList<GCVResult> textResults = new ArrayList<GCVResult>();
		
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
//					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}

				// For full list of available annotations, see http://g.co/cloud/vision/docs
				for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//					out.printf("Text: %s\n", annotation.getDescription());
//					out.printf("Position : %s\n", annotation.getBoundingPoly());
//					out.printf("Confidence : %s\n", annotation.getConfidence());
//					out.printf("Score : %s\n", annotation.getScore());
//					out.printf("Topicality : %s\n", annotation.getTopicality());
					GCVResult gcvResult = new GCVResult();
					gcvResult.setScore(annotation.getScore());
					gcvResult.setConfident(annotation.getConfidence());
					gcvResult.setDescription(annotation.getDescription());
//					gcvResult.setBoundingPoly(annotation.getBoundingPoly().getAllFields().values()); //TODO::

					for(Vertex v : annotation.getBoundingPoly().getVerticesList()) {
						gcvResult.addVerticesAsPoints(new Point(v.getX(), v.getY()));
					}
//					annotation.getBoundingPoly().getVerticesList().get(0).getX();
//					Iterator iterator = annotation.getBoundingPoly().getAllFields().values().iterator();
//					while(iterator.hasNext()){
//						System.out.println("THE ITERATOR VALUE : " + iterator.next().toString());
//					}
					gcvResult.setTopicality(annotation.getTopicality());
					textResults.add(gcvResult);
				}
			}
		}
		result.setTextResults(textResults);
		System.out.println("\nEND TEXT DETECTION::\n ");
	}

	/**
	 * Detects logos in the specified local image.
	 *
	 * @param filePath
	 *            The path to the local file to perform logo detection on.
	 * @param out
	 *            A {@link PrintStream} to write detected logos to.
	 * @throws Exception
	 *             on errors while closing the client.
	 * @throws IOException
	 *             on Input/Output errors.
	 */
	public static void detectLogos(String filePath, GCVImageResult result) throws Exception, IOException {
		System.out.println("\nSTART LOGOS DETECTION::\n ");
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.LOGO_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		ArrayList<GCVResult> logoResults = new ArrayList<GCVResult>();
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
//					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}

				// For full list of available annotations, see http://g.co/cloud/vision/docs
				for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
					// out.println(annotation.getDescription());
					GCVResult gcvResult = new GCVResult();
					gcvResult.setScore(annotation.getScore());
					gcvResult.setConfident(annotation.getConfidence());
					gcvResult.setDescription(annotation.getDescription());
					logoResults.add(gcvResult);
				}
			}
		}
		result.setLogoResults(logoResults);
		System.out.println("\nEND LOGOS DETECTION::\n ");
	}

	/**
	 * Detects image properties such as color frequency from the specified local
	 * image.
	 *
	 * @param filePath
	 *            The path to the file to detect properties.
	 * @param out
	 *            A {@link PrintStream} to write
	 * @throws Exception
	 *             on errors while closing the client.
	 * @throws IOException
	 *             on Input/Output errors.
	 */
	public static void detectProperties(String filePath, GCVImageResult result)
			throws Exception, IOException {
		System.out.println("\nSTART PROPERTIES DETECTION::\n ");
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.IMAGE_PROPERTIES).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
//					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}
				int counter = 1;
				// For full list of available annotations, see http://g.co/cloud/vision/docs
				DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
				for (ColorInfo color : colors.getColorsList()) {
					DominantGCVColor c = new DominantGCVColor();
					c.setRed(color.getColor().getRed());
					c.setGreen(color.getColor().getGreen());
					c.setBlue(color.getColor().getBlue());
					c.setFraction(color.getPixelFraction());
					
					if (counter == 1) {
						result.setDominantColor(c);
					} else if (counter == 2) {
						result.setSecondaryColor(c);
					} else if (counter == 3) {
						result.setThirdColor(c);
					} else {
						break;
					}
					// out.printf(
					// "fraction: %f\nr: %f, g: %f, b: %f\n",
					// color.getPixelFraction(),
					// color.getColor().getRed(),
					// color.getColor().getGreen(),
					// color.getColor().getBlue());
					counter++;
				}
			}
		}
		System.out.println("\nEND PROPERTIES DETECTION::\n ");
	}

	// // [START vision_detect_document]
	// /**
	// * Performs document text detection on a local image file.
	// *
	// * @param filePath
	// * The path to the local file to detect document text on.
	// * @param out
	// * A {@link PrintStream} to write the results to.
	// * @throws Exception
	// * on errors while closing the client.
	// * @throws IOException
	// * on Input/Output errors.
	// */
	// public static void detectDocumentText(String filePath, PrintStream out)
	// throws Exception, IOException {
	// List<AnnotateImageRequest> requests = new ArrayList<>();
	//
	// ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
	//
	// Image img = Image.newBuilder().setContent(imgBytes).build();
	// Feature feat =
	// Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
	// AnnotateImageRequest request =
	// AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
	// requests.add(request);
	//
	// try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
	// BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
	// List<AnnotateImageResponse> responses = response.getResponsesList();
	// client.close();
	//
	// for (AnnotateImageResponse res : responses) {
	// if (res.hasError()) {
	// out.printf("Error: %s\n", res.getError().getMessage());
	// return;
	// }
	//
	// // For full list of available annotations, see http://g.co/cloud/vision/docs
	// TextAnnotation annotation = res.getFullTextAnnotation();
	// for (Page page : annotation.getPagesList()) {
	// String pageText = "";
	// for (Block block : page.getBlocksList()) {
	// String blockText = "";
	// for (Paragraph para : block.getParagraphsList()) {
	// String paraText = "";
	// for (Word word : para.getWordsList()) {
	// String wordText = "";
	// for (Symbol symbol : word.getSymbolsList()) {
	// wordText = wordText + symbol.getText();
	// out.format("Symbol text: %s (confidence: %f)\n", symbol.getText(),
	// symbol.getConfidence());
	// }
	// out.format("Word text: %s (confidence: %f)\n\n", wordText,
	// word.getConfidence());
	// paraText = String.format("%s %s", paraText, wordText);
	// }
	// // Output Example using Paragraph:
	// out.println("\nParagraph: \n" + paraText);
	// out.format("Paragraph Confidence: %f\n", para.getConfidence());
	// blockText = blockText + paraText;
	// }
	// pageText = pageText + blockText;
	// }
	// }
	// out.println("\nComplete annotation:");
	// out.println(annotation.getText());
	// }
	// }
	// }
	// // [END vision_detect_document]

	/**
	 * Suggests a region to crop to for a local file.
	 *
	 * @param filePath
	 *            The path to the local file used for web annotation detection.
	 * @param out
	 *            A {@link PrintStream} to write the results to.
	 * @throws Exception
	 *             on errors while closing the client.
	 * @throws IOException
	 *             on Input/Output errors.
	 */
	public static void detectCropHints(String filePath, GCVImageResult result) throws Exception, IOException {

		System.out.println("\nSTART CROP HINTS DETECTION::\n ");
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.CROP_HINTS).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
//					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}

				// For full list of available annotations, see http://g.co/cloud/vision/docs
//				CropHintsAnnotation annotation = res.getCropHintsAnnotation();
//				for (CropHint hint : annotation.getCropHintsList()) {
//					out.println(hint.getBoundingPoly());
//				}
			}
		}
		System.out.println("\nEND CROP HINTS DETECTION::\n ");
	}

	/**
	 * Find web entities given a local image.
	 * 
	 * @param filePath
	 *            The path of the image to detect.
	 * @param out
	 *            A {@link PrintStream} to write the results to.
	 * @throws Exception
	 *             on errors while closing the client.
	 * @throws IOException
	 *             on Input/Output errors.
	 */
	public static void detectWebEntities(String filePath, GCVImageResult result)
			throws Exception, IOException {
		System.out.println("\nSTART WEB ENTITIES DETECTION::\n ");
		// Instantiates a client
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			ArrayList<GCVResult> webEntitiesResult = new ArrayList<GCVResult>();
			// Read in the local image
			ByteString contents = ByteString.readFrom(new FileInputStream(filePath));

			// Build the image
			Image image = Image.newBuilder().setContent(contents).build();

			// Create the request with the image and the specified feature: web detection
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
					.addFeatures(Feature.newBuilder().setType(Type.WEB_DETECTION)).setImage(image).build();

			// Perform the request
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(Arrays.asList(request));
			ArrayList<Float> scores = new ArrayList<Float>();
			ArrayList<String> descriptions = new ArrayList<String>();
			// Display the results
			response.getResponsesList().stream()
					.forEach(r -> r.getWebDetection().getWebEntitiesList().stream().forEach(entity -> {
						scores.add(entity.getScore());
						descriptions.add(entity.getDescription());
						// out.format("Description: %s\n", entity.getDescription());
						// out.format("Score: %f\n", entity.getScore());
					}));

			for (int i = 0; i < scores.size(); i++) {
				GCVResult gcvResult = new GCVResult();
				gcvResult.setScore(scores.get(i));
				gcvResult.setDescription(descriptions.get(i));
				webEntitiesResult.add(gcvResult);
			}
			result.setWebEntitiesResults(webEntitiesResult);
		} catch (Exception e) {
			System.out.println("Error when detecting web entities in the image");
			System.out.println(e.getMessage());
		}
		System.out.println("\nEND WEB ENTITIES DETECTION::\n ");
	}

	// // [START vision_web_entities_include_geo_results]
	// /**
	// * Find web entities given a local image.
	// * @param filePath The path of the image to detect.
	// * @param out A {@link PrintStream} to write the results to.
	// * @throws Exception on errors while closing the client.
	// * @throws IOException on Input/Output errors.
	// */
	// public static void detectWebEntitiesIncludeGeoResults(String filePath,
	// PrintStream out) throws
	// Exception, IOException {
	// // Instantiates a client
	// try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
	// // Read in the local image
	// ByteString contents = ByteString.readFrom(new FileInputStream(filePath));
	//
	// // Build the image
	// Image image = Image.newBuilder().setContent(contents).build();
	//
	// // Enable `IncludeGeoResults`
	// WebDetectionParams webDetectionParams = WebDetectionParams.newBuilder()
	// .setIncludeGeoResults(true)
	// .build();
	//
	// // Set the parameters for the image
	// ImageContext imageContext = ImageContext.newBuilder()
	// .setWebDetectionParams(webDetectionParams)
	// .build();
	//
	// // Create the request with the image, imageContext, and the specified
	// feature: web detection
	// AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
	// .addFeatures(Feature.newBuilder().setType(Type.WEB_DETECTION))
	// .setImage(image)
	// .setImageContext(imageContext)
	// .build();
	//
	// // Perform the request
	// BatchAnnotateImagesResponse response =
	// client.batchAnnotateImages(Arrays.asList(request));
	//
	// // Display the results
	// response.getResponsesList().stream()
	// .forEach(r -> r.getWebDetection().getWebEntitiesList().stream()
	// .forEach(entity -> {
	// out.format("Description: %s\n", entity.getDescription());
	// out.format("Score: %f\n", entity.getScore());
	// }));
	// }
	// }
	// // [END vision_web_entities_include_geo_results]
	//
	//

}
