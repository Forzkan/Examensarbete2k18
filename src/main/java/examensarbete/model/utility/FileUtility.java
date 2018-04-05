package examensarbete.model.utility;

import java.io.File;
import java.net.URL;

import examensarbete.model.properties.PropertiesHandler;
import examensarbete.model.properties.TTProperties;

public class FileUtility {
	
	
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
        	 return fileName.substring(fileName.lastIndexOf(".")+1);
        }else {
        	return "";
        }
    }
    
    public static String getNameWithoutJsonEnd(File file) {
    	return file.getName().replaceAll(".json", "");
    }
    
    public static String getNameWithoutPngEnd(File file) {
    	return file.getName().replaceAll(".png", "");
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
    	String fullFilePath = fullDirPath + "\\" + getUniqueImageName(fullDirPath, testName + "_SI"); // SI for Snap Image.
    	return fullFilePath;
    }
    
    public static String createUniqueContextImageFilePath(String groupName, String testName) {
    	System.out.println(groupName + " - - - " + testName);
    	String fullDirPath = createFilePathAndNeededDirs(groupName, testName);
    	String fullFilePath = fullDirPath + "\\" + getUniqueImageName(fullDirPath, testName + "_CI"); // CI for Context Image.
    	return fullFilePath;
    }
    
    
    private static String createFilePathAndNeededDirs(String groupName, String testName) {
    	String testCaseDir =  PropertiesHandler.properties.getProperty(TTProperties.TESTCASE_DIRECTORY.toString());
    	System.out.println(testCaseDir);
    	String fullDirPath = testCaseDir + "\\" + groupName + "\\images";
    	if(!new File(fullDirPath).exists()) {
    		File dir = new File(fullDirPath);
    		dir.mkdirs();
    	}
    	return fullDirPath;
    }
    
    private static String getUniqueImageName(String dirPath, String defaultImageName) {
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
    	for(File f : dir.listFiles()) {
    		if(f.getName().replaceAll(getFileExtension(f), "").equals(name)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public static String getProjectRoot() {
    	return System.getProperty("user.dir");
    }
    
}
