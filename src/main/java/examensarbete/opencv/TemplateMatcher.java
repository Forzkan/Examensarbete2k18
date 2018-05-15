package examensarbete.opencv;

import org.opencv.core.Core;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import examensarbete.model.test.TestImage;
import examensarbete.model.test.TestImageImpl;
import examensarbete.model.test.MatchType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;
import java.util.Map.Entry;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;

public class TemplateMatcher implements ChangeListener {

    Mat contextImage = new Mat(), targetImage = new Mat();
    int match_method;
    JLabel templateMatchResultDisplay = new JLabel();
    private static final int MATCHING_METHODS_IN_USE = 6;
    private static final int MINIMUM_IDENTICAL_MATCHES = 3;
	
    private TestImage testResultImage;
    
	public TemplateMatcher() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public TestImage getResultTestImage() {
		return testResultImage;
	}
	
    public void runVisualComparison(TestImage contextImage, TestImage targetImage) {
        setupTestImages(contextImage, targetImage);
        matchingMethod(0);
        createJFrame();
    }
    
    public MatchType findTargetImage(TestImage contextImage, TestImage targetImage) {
        setupTestImages(contextImage, targetImage);
        
    	List<TestImage> matchedImages = new ArrayList<TestImage>();
    	matchedImages.add(matchingMethod(0));
    	matchedImages.add(matchingMethod(1));
    	//Skipping matching method 2 - TM CCORR since it has proven to give bad results for our use.
    	//With the new sorting we'll run TM CORR anyway
    	matchedImages.add(matchingMethod(2));
    	matchedImages.add(matchingMethod(3));
    	matchedImages.add(matchingMethod(4));
    	matchedImages.add(matchingMethod(5));
    	
//    	matchedImages = sortMatchedImages(matchedImages);
    	
    	MatchType result = MatchType.NO_MATCH;
    	
    	//If there is any disparity in the images resulting from the different matching methods we deem it to be a failure to find the targetImage
//    	for( int i = 1; i < 5; i++ ) {
//        	if(!matchedImages.get(0).compareTestImage(matchedImages.get(i))) {
//        		result = MatchType.NO_MATCH;
//        		break;
//        	}
//        }
    	
//    	int identicalMatches = getIdenticalImages(matchedImages);
//    	if(identicalMatches >= 3) {
//    		result = MatchType.MATCH;
//    	}
    	
    	if(sortMatchedImages(matchedImages).get(0).identicalImages >= MINIMUM_IDENTICAL_MATCHES) {
    		result = MatchType.MATCH;
    	}
    	
    	if(result == MatchType.MATCH && !targetImage.compareTestImage(matchedImages.get(0))) {
    		result = MatchType.LOCATION_CHANGED_MATCH;
    	}

    	testResultImage = sortMatchedImages(matchedImages).get(0).testImage;
    	return result;
    }
    
    /**
     * 
     * @param matchedImages
     * @return Returns an int representing the amount of matched images that are identical 
     */
    private int getIdenticalImages(List<TestImage> matchedImages) {
    	int identicalImages = 0;
    	//TODO: Egentligen så bör man jämföra alla bilder med varandra, inte bara [0] mot alla andra
    	//Matcha alla bilder mot alla andra, lägg in dom i ett set likt Set[bild[0], <antal matchningar>] etc, sen så kan man bara sortera settet och få ut den med flest matchningar
    	for( int i = 1; i < MATCHING_METHODS_IN_USE; i++ ) {
        	if(matchedImages.get(0).compareTestImage(matchedImages.get(i))) {
        		identicalImages++;
        	}
        }
    	//Since it doesn't compare to itself always add one
    	identicalImages++;
    	return identicalImages;
    }
    
    /**
     * 
     * @param matchedImages
     * @return List of MatchedImage where the first element is the one with the most identical matches
     */
    private List<MatchedImage> sortMatchedImages(List<TestImage> matchedImages) {
//    	List<TestImage> sortedMatches = new ArrayList<TestImage>();
//    	Collections.sort(sortedMatches, new Comparator<TestImage>() {
//    		public int compare(TestImage testImage1, TestImage testImage2) {
//    			return -1;
//    		}
//    	});
//    	return sortedMatches;
////    	return matchedImages;
    	
    	
    	//TODO: Egentligen så bör man jämföra alla bilder med varandra, inte bara [0] mot alla andra
    	//Matcha alla bilder mot alla andra, lägg in dom i ett set likt Set[bild[0], <antal matchningar>] etc, sen så kan man bara sortera settet och få ut den med flest matchningar
    	
//    	Map<TestImage, Integer> sortedImages = new TreeMap<TestImage, Integer>(new Comparator<TestImage>() {
//    		public int compare(TestImage testImage1, TestImage testImage2) {
//    			if() {
//    				
//    			}
//    			return -1;
//    		}
//    	});
    	
    	
    	List<MatchedImage> sortedMatchImages = new ArrayList<MatchedImage>();
    	
    	for( int i = 0; i < MATCHING_METHODS_IN_USE; i++ ) {
    		sortedMatchImages.add(new MatchedImage(matchedImages.get(i),0));
    		for (int j = 0; j < MATCHING_METHODS_IN_USE; j++) {
    			if(matchedImages.get(i).compareTestImage(matchedImages.get(j))) {
    				sortedMatchImages.get(i).identicalImages += 1;
            	}
			}
        }
    	
    	Collections.sort(sortedMatchImages, new Comparator<MatchedImage>() {
    		public int compare(MatchedImage m1, MatchedImage m2) {
    			int result;
    			if(m1.identicalImages < m2.identicalImages) {
    				result = 1;
    			} else {
    				result = -1;
    			}
    			return result;
    		}
    	});
    	
//    	List<TestImage> sortedTestImages = new ArrayList<TestImage>();
//    	
//    	for (MatchedImage matchImage : sortedMatchImages) {
//			sortedTestImages.add(matchImage.testImage);
//		}
    	
//    	return sortedTestImages;
    	return sortedMatchImages;
    }
    
    private void setupTestImages(TestImage contextImage, TestImage targetImage) {
    	this.contextImage = Imgcodecs.imread( contextImage.getFullImagePath(), Imgcodecs.IMREAD_COLOR );
        this.targetImage = Imgcodecs.imread( targetImage.getFullImagePath(), Imgcodecs.IMREAD_COLOR );
        if(this.contextImage.empty() || this.targetImage.empty()) {
        	if(this.contextImage.empty()) {
        		System.out.println("Can't read context image");
        		System.out.println(contextImage.getFullImagePath());
        	}
        	if(this.targetImage.empty()) {
        		System.out.println("Can't read target image");
        		System.out.println(targetImage.getFullImagePath());
        	}
            System.exit(-1);
        }
    }
    
    /**
     * Returns where a match was found
     * @return TestImage
     */
    private TestImage matchingMethod(int matchMethod) {
        Mat result = new Mat();
        Mat img_display = new Mat();
        contextImage.copyTo( img_display );
        int result_cols =  contextImage.cols() - targetImage.cols() + 1;
        int result_rows = contextImage.rows() - targetImage.rows() + 1;
        result.create( result_rows, result_cols, CvType.CV_32FC1 );

        Imgproc.matchTemplate( contextImage, targetImage, result, matchMethod);
        Core.normalize( result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat() );
        
        Point matchLoc;
        
        Core.MinMaxLocResult mmr = Core.minMaxLoc( result );
        //  For all the other methods, the higher the better
        if( matchMethod  == Imgproc.TM_SQDIFF || matchMethod == Imgproc.TM_SQDIFF_NORMED ){ 
        	matchLoc = mmr.minLoc; 
    	} else {
    		matchLoc = mmr.maxLoc;
    	}
        
        Imgproc.rectangle(img_display, matchLoc, new Point(matchLoc.x + targetImage.cols(),
                matchLoc.y + targetImage.rows()), new Scalar(0, 0, 0), 2, 8, 0);
//        Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + targetImage.cols(),
//                matchLoc.y + targetImage.rows()), new Scalar(0, 0, 0), 2, 8, 0);
        
        BufferedImage tmpImg = matToBufferedImage(img_display);
        ImageIcon icon = new ImageIcon(tmpImg);
        templateMatchResultDisplay.setIcon(icon);
        
//        result.convertTo(result, CvType.CV_8UC1, 255.0);
//        tmpImg = matToBufferedImage(result);
        
        java.awt.Point matchImagePoint = new java.awt.Point((int)matchLoc.x, (int)matchLoc.y);
//        TestImage matchImage = new TestImageImpl("src/main/resources/tests/test_2/2-part.png", matchImagePoint, targetImage.cols(), targetImage.rows());
        TestImage matchImage = new TestImageImpl(matToBufferedImage(targetImage), matchImagePoint);
        return matchImage;
    }
    
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            matchingMethod((int)source.getValue());
        }
    }
    
    /**
     * Returns an <Image> based on a <Mat> representation of an image
     * 
     * @param m - Matrix representation of image
     * @return Image
     */
    public BufferedImage matToBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
    
    private void createJFrame() {
        String title = "Source image; Control; Result image";
        JFrame frame = new JFrame(title);
//        frame.setLayout(new GridLayout(2, 2));
        frame.setLayout(new FlowLayout());
        frame.add(templateMatchResultDisplay);
        int min = 0, max = 5;
        JSlider slider = new JSlider(JSlider.VERTICAL, min, max, match_method);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        // Set the spacing for the minor tick mark
        slider.setMinorTickSpacing(1);
        // Customizing the labels
        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("0 - SQDIFF") );
        labelTable.put( new Integer( 1 ), new JLabel("1 - SQDIFF NORMED") );
        labelTable.put( new Integer( 2 ), new JLabel("2 - TM CCORR") );
        labelTable.put( new Integer( 3 ), new JLabel("3 - TM CCORR NORMED") );
        labelTable.put( new Integer( 4 ), new JLabel("4 - TM COEFF") );
        labelTable.put( new Integer( 5 ), new JLabel("5 - TM COEFF NORMED : (Method)") );
        slider.setLabelTable( labelTable );
        slider.addChangeListener(this);
        frame.add(slider);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
//    public enum MatchType {
//		MATCH,
//		NEW_LOC_MATCH,
//		NO_MATCH
//	}
    
    private class MatchedImage {
    	
    	private TestImage testImage;
    	private int identicalImages;
    	
    	MatchedImage(TestImage testImage, int identicalImages){
    		this.testImage = testImage;
    		this.identicalImages = identicalImages;
    	}
    	
//    	public TestImage getTestImage() {
//    		return testImage;
//    	}
//    	
//    	public void setIdenticalMatches(int matches) {
//    		identicalImages = matches;
//    	}
//    	
//    	public int getIdenticalMatches() {
//    		return identicalImages;
//    	}
    	
    }
    
//    private class MapUtil {
//        public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
//            List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
//            list.sort(Entry.comparingByValue());
//
//            Map<K, V> result = new LinkedHashMap<>();
//            for (Entry<K, V> entry : list) {
//                result.put(entry.getKey(), entry.getValue());
//            }
//
//            return result;
//        }
//    }
}
