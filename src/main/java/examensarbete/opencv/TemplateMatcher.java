package examensarbete.opencv;

import org.opencv.core.Core;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import examensarbete.model.test.TestImage;
import examensarbete.model.test.TestImageImpl;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;

public class TemplateMatcher implements ChangeListener {

	Boolean use_mask = false;
    Mat contextImage = new Mat(), targetImage = new Mat();
    int match_method;
    JLabel imgDisplay = new JLabel(), resultDisplay = new JLabel();
	
	public TemplateMatcher() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
    public MatchType run(TestImage contextImage, TestImage targetImage) {
        this.contextImage = Imgcodecs.imread( contextImage.getImagePath(), Imgcodecs.IMREAD_COLOR );
        this.targetImage = Imgcodecs.imread( targetImage.getImagePath(), Imgcodecs.IMREAD_COLOR );
        if(this.contextImage.empty() || this.targetImage.empty()) {
            System.out.println("Can't read one of the images");
            System.exit(-1);
        }
        //Loopa över matchingMethod 6 ggr och använd countern som match_method
//        TestImage[5] matchedImages = {};
        List<TestImage> matchedImages = new ArrayList<TestImage>();
        for( int i = 0; i < 5; i++ ) {
        	matchedImages.add(matchingMethod(i));
        	//Gör inte för 2 - TM CCORR, den är dålig
//        	System.out.println(matchedImages.get(i));
        }
        matchingMethod(0);
        createJFrame();
        //Ifall majoriteten av alla matched TestImage har samma position som target image så har vi en match
        //Annars en NEW_LOC_MATCH
        return MatchType.MATCH;
    }
    
    private void compareMatchImages(List<TestImage> matchedImages) {
    	//Sortera efter x & y värden, returnera den TestImage som gäller
    	//return TestImage;
    }
    
    /**
     * Returns where a match was found
     * @return
     */
    private TestImage matchingMethod(int matchMethod) {
    	//TODO: Skriv ut koordinaterna varje gång den matchar något
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
        Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + targetImage.cols(),
                matchLoc.y + targetImage.rows()), new Scalar(0, 0, 0), 2, 8, 0);
        
        Image tmpImg = matToBufferedImage(img_display);
        ImageIcon icon = new ImageIcon(tmpImg);
        imgDisplay.setIcon(icon);
        result.convertTo(result, CvType.CV_8UC1, 255.0);
        tmpImg = matToBufferedImage(result);
        
        
        icon = new ImageIcon(tmpImg);
        resultDisplay.setIcon(icon);
        
//        return matToBufferedImage(targetImage);

        //Översätt point
        java.awt.Point testImagePoint = new java.awt.Point((int)matchLoc.x, (int)matchLoc.y);
        TestImage matchImage = new TestImageImpl("src/main/resources/tests/test_2/2-part.png", testImagePoint, targetImage.cols(), targetImage.rows());
        System.out.println(matchImage);
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
    public Image matToBufferedImage(Mat m) {
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
        frame.setLayout(new GridLayout(2, 2));
        frame.add(imgDisplay);
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
        frame.add(resultDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public enum MatchType {
		MATCH,
		NEW_LOC_MATCH,
		NO_MATCH
	}
}
