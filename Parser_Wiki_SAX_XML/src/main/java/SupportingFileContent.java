package main.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
		return new ArrayList<File>(Arrays.asList(f.listFiles()));
	}
	
	
	public void write(String outFile, String data)
	{
		try
		{
    		File file =new File(outFile);
    		if(!file.exists()){file.createNewFile();}
    		
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
    		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    		bufferWritter.write(data);
    		bufferWritter.close();
	        System.out.println("Done");
	        
    	}catch(IOException e){
    		e.printStackTrace();
    	}
		
		/*
		 try 
		  	{
    			Files.write(Paths.get("myfile.txt"), "the text".getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
    			//exception handling left as an exercise for the reader
			}
		 */
		
		
		/*
		 try(FileWriter fw = new FileWriter("outfilename", true);
    		BufferedWriter bw = new BufferedWriter(fw);
    		PrintWriter out = new PrintWriter(bw))
		{
    		out.println("the text");
    		//more code
    		out.println("more text");
    		//more code
		} catch (IOException e) {
    	//exception handling left as an exercise for the reader
		}
		 */
	}
	
	
	public static void main(String[] args) 
	{
		String outFile = "full-enwiki-text-file.txt";
		SupportingFileContent sfc = new SupportingFileContent();
		System.out.println(sfc.getFolderFileName("C:/Users/Subadmin/Desktop/Raw Dataset"));

	}

}
