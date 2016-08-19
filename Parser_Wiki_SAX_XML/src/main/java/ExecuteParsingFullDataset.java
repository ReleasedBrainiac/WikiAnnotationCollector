package main.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;

/**
 * This class creates the dataset as appended text stored as text file. 
 * The content of the dataset is gathered from given XML file containing sentences with annotations. 
 * @author T.Turke
 *
 */
public class ExecuteParsingFullDataset 
{
	
	/**
	 * This methode generate a fulltext from given xml content.
	 * @param inFile
	 * @param outFile
	 * @param handler
	 * @param reportUpdate
	 */
	public void executeDatsetConstruction(String inFile, String outFile, ParserXMLToText handler, int reportUpdate)
	{
		try 
		{
			System.out.println("START on File => "+inFile+" store in => "+outFile);
			
			// UTF-8 Setup
			File file = new File(inFile);
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			
			// Parsing
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			handler.setReportUpdate(reportUpdate);
			parser.parse(is, handler);
			
			// Storing
			SupportingFileContent sfc = new SupportingFileContent();
			sfc.writeListCommonUTF8(outFile, handler.getLines());
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FAILED File: "+inFile+" at "+Calendar.getInstance().getTime());
		}	
	}

	/**
	 * START ENGINE Dataset creation
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("START: "+Calendar.getInstance().getTime());
		
		String dataSource = "C:/Users/Subadmin/Desktop/Cleaned/";
		String outFile = "C:/Users/Subadmin/Desktop/small-data-set.txt";
		int reportUpdate = 10000;
		
		SupportingFileContent sfc = new SupportingFileContent();
		ArrayList<File> inFiles = sfc.getFolderFiles(dataSource);
		ExecuteParsingFullDataset epfd = new ExecuteParsingFullDataset();
		
		for(int a_it = 0; a_it < inFiles.size(); a_it++)
		{
			new ExecuteParsingFullDataset().executeDatsetConstruction(inFiles.get(a_it).getAbsolutePath(), outFile, new ParserXMLToText(), reportUpdate);
		}

		System.out.println("END: "+Calendar.getInstance().getTime());
	}
}
