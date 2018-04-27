package examensarbete.model.utility;

import java.io.File;
import java.net.URL;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;

public class FileUtility {
	
	
	private static final String imageExt = ".png";
	private static final String jsonExt = ".json";
	
	
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
        	 return fileName.substring(fileName.lastIndexOf(".")+1);
        }else {
        	return "";
        }
    }
    
    public static String getNameWithoutJsonEnd(File file) {
    	return file.getName().replaceAll(jsonExt, "");
    }
    
    public static String getNameWithoutPngEnd(File file) {
    	return file.getName().replaceAll(imageExt, "");
    }
    
    public static URL getImageUrlFromPath(String path) {
    	String urlPath = "file:///" + path;
    	try{
    		return new URL(urlPath);
    	}catch(Exception e) {
    		System.out.println("Could not create URL from path " + urlPath);
    		return null;
    	}
    }
    
    public static String createUniqueSnapImageFilePath(String groupName, String testName) {
    	String fullDirPath = createFilePathAndNeededDirs(groupName, testName);
//    	System.out.println("FULL DIRR PATH: " + fullDirPath);
    	String fullFilePath = fullDirPath + "\\" + getUniqueImageName(fullDirPath, testName + "_SI"); // SI for Snap Image.
    	return fullFilePath;
    }
    
    public static String createUniqueContextImageFilePath(String groupName, String testName) {
    	String fullDirPath = createFilePathAndNeededDirs(groupName, testName);
    	String fullFilePath = fullDirPath + "\\" + getUniqueImageName(fullDirPath, testName + "_CI"); // CI for Context Image.
    	return fullFilePath;
    }
    
    
    private static String createFilePathAndNeededDirs(String groupName, String testName) {
    	String testCaseDir = PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString());
//    	System.out.println(testCaseDir);
    	String fullDirPath = testCaseDir + "\\" + groupName + "\\" + testName + "\\images";
    	if(!new File(fullDirPath).exists()) {
    		File dir = new File(fullDirPath);
    		dir.mkdirs();
    	}
    	return fullDirPath;
    }
    
    private static String getUniqueImageName(String dirPath, String defaultImageName) {
//    	System.out.println("THE NAME WE CHECK IF ALREADY EXISTS: " + defaultImageName);
    	String ret = defaultImageName;
    	int counter = 1;
    	
    	while(nameAlreadyExist(dirPath, ret)) {
    		ret = defaultImageName + "("+counter+")";
    		counter++;
    	}
    	
    	return ret;
    }
    
    private static boolean nameAlreadyExist(String dirPath, String name) {
    	File dir = new File(dirPath);
//    	System.out.println("LIST OF FILES \n"+dir.listFiles());
    	for(File f : dir.listFiles()) {
//    		System.out.println(f.getAbsolutePath());
//    		System.out.println(f.getName());
    		if(f.getName().replaceAll(imageExt, "").equals(name)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public static String getProjectRoot() {
    	return System.getProperty("user.dir");
    }
    
    
    
    public static void createPathIfNoExisting(String path) {
    	File f = new File(path);
    	if(f.exists() == false) {
    		f.mkdirs();
    	}
    }

	public static String absoluteToRelativePath(String imagePath) {
		System.out.println(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()));
		return imagePath.replace(PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString()),"");
	}
    
    
    
 
    
    
    
    
    
}
