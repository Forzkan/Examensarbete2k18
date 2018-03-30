package examensarbete.model.utility;

import java.io.File;

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
    
}
