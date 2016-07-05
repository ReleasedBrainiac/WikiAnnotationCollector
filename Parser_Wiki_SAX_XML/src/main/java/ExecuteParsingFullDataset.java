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
	
	public void executeDatsetConstruction(String inFile, String outFile, ParserXMLToText handler, int reportUpdate, int breakpoint)
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
			handler.setBreakpoint(breakpoint);
			parser.parse(is, handler);
			
			// Storing
			SupportingFileContent sfc = new SupportingFileContent();
			sfc.writeCommonUTF8(outFile, handler.getFulltext(), breakpoint);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FAILED File: "+inFile+" at "+Calendar.getInstance().getTime());
		}	
	}

	/**
	 * Just testing
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("START: "+Calendar.getInstance().getTime());
		
		String dataSource = "C:/Users/Subadmin/Desktop/Cleaned/";
		String outFile = "C:/Users/Subadmin/Desktop/Cleaned Fulltext/full-enwiki-text-file.txt";
		int reportUpdate = 10000;
		int breakpoint = 6;
		
		SupportingFileContent sfc = new SupportingFileContent();
		ArrayList<File> inFiles = sfc.getFolderFiles(dataSource);
		ExecuteParsingFullDataset epfd = new ExecuteParsingFullDataset();
		ParserXMLToText pxtt = new ParserXMLToText();
		
		
		for(int a_it = 0; a_it < inFiles.size(); a_it++)
		{
			epfd.executeDatsetConstruction(inFiles.get(a_it).getAbsolutePath(), outFile, pxtt, reportUpdate, breakpoint);
		}

		System.out.println("END: "+Calendar.getInstance().getTime());

	}
}
