import java.io.File; 
import java.io.IOException; 

/**
 * Simple file generator. Common known in java.
 * @author T.Turke
 */
public class FileGenerator 
{ 
	/**
	 * Constructor simply create the file if not exist!
	 * @param path
	 */
	public FileGenerator(String path)
	{
		if(checkFile(new File(path))) System.out.println(path + " erzeugt"); 
	}
	
	/**
	 * This method check file existence.
	 * @param file
	 * @return isExisting
	 */
    private boolean checkFile(File file) 
    { 
        if (file != null) { 
            try { 
                file.createNewFile(); 
            } catch (IOException e) { 
                System.err.println("Error creating " + file.toString()); 
            } 
            if (file.isFile() && file.canWrite() && file.canRead()) 
                return true; 
        } 
        return false; 
    } 
} 