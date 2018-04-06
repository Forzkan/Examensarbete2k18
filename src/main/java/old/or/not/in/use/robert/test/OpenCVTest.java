package old.or.not.in.use.robert.test;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCVTest {

	
	public static void main(String[] args) {
		// LOAD THE OPENCV NATIVE LIBRARY.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		

		// The class Mat represents an n-dimensional dense numerical single-channel or multi-channel array.
		// It can be used to store real or complex-valued vectors and matrices, grayscale or color images, 
		// voxel volumes, vector fields, point clouds, tensors, histograms. 
		// For more details check out the OpenCV page.
		Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
		
		
		// Simply print the mat.
		System.out.println("mat = " + mat.dump());
		
		
		
	}
	
	
}
