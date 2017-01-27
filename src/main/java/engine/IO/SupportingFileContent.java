package engine.IO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class handle the whole dataset textfile creation functions we need.
 * @author T.Turke
 */
public class SupportingFileContent 
{	
	/**
	 * Get all folder file names
	 * @param initialPath
	 * @return list of folder file names
	 */
	public ArrayList<String> getFolderFileName(String initialPath)
	{
		File f = new File(initialPath);
		return new ArrayList<String>(Arrays.asList(f.list()));
	}
	
	/**
	 * Get all folder files
	 * @param initialPath
	 * @return list of folder files
	 */
	public ArrayList<File> getFolderFiles(String initialPath)
	{
		File f = new File(initialPath);
		
		ArrayList<File> output = new ArrayList<File>();
		File[] raw = f.listFiles();
		
		
		for (int i = 0; i < raw.length; i++) 
		{
			if(raw[i].isFile())
			{
				output.add(raw[i]);
			}
		}
		
		return output;
	}
	
	/**
	 * Easy file writer
	 * @param outFile
	 * @param string
	 */
	public void writeCommon(String outFile, String data)
	{
		try(FileWriter fw = new FileWriter(outFile, true);BufferedWriter bw = new BufferedWriter(fw);PrintWriter out = new PrintWriter(bw))
		{
			out.write(data);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * Easy file writer with UTF-8 encoding
	 * @param outFile
	 * @param data
	 * @param breakPoint
	 */
	public void writeCommonUTF8(String outFile, String data)
	{
		try(FileOutputStream fos = new FileOutputStream(outFile, true); Writer out = new OutputStreamWriter(fos, "UTF8");)
		{
			out.write(data);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * Easy file writer with UTF-8 encoding
	 * @param outFile
	 * @param data
	 */
	public void writeListCommonUTF8(String outFile, ArrayList<String> data)
	{
		try(FileWriter fw = new FileWriter(outFile, true); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8")); PrintWriter out = new PrintWriter(bw))
		{
			
			for (int i = 0; i < data.size(); i++) 
			{
				out.write(data.get(i));
				out.write(" \n");
			}
			
			out.close();
			bw.close();
			fw.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Easy file writer with UTF-8 encoding
	 * @param outFile
	 * @param data
	 */
	public void writeListCommonUTF8_SB(String outFile, StringBuilder data)
	{
		try(FileWriter fw = new FileWriter(outFile, true); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8")); PrintWriter out = new PrintWriter(bw))
		{
			out.write(data.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
