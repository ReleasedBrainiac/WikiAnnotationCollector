package main.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.IntStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

public class ExecuteParsingDatacollection 
{
	/**
	 * This method extract the article name from given filename or -path.
	 * @param in
	 * @return article name
	 */
	public String wikiArticleName(String in)
	{
		return in.substring(in.lastIndexOf("-")+1, in.lastIndexOf(".xml"));
	}
	
	/**
	 * This method define 1 collector for 1 file.
	 * @param inPath
	 * @param outPath
	 * @param handler
	 * @param reportUpdate
	 * @param rootElem
	 * @param thread_ID
	 */
	public void oneCollectorExecute(String inPath, String outPath, XMLParserSAXStyle handler, int reportUpdate, String rootElem, int thread_ID)
	{
		try 
		{
			System.out.println("START Thread "+thread_ID+" on File => "+inPath+" store in => "+outPath);
			
			// UTF-8 Setup
			File file = new File(inPath);
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			
			// Parsing
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			handler.setReportUpdate(reportUpdate);
			handler.setThread_ID(thread_ID);
			parser.parse(is, handler);
			
			// Storing
			new FileGenerator(outPath);
			new StoringContentSemiParallel(outPath, rootElem, handler.getAnotEnts());
			System.out.println("Done File: "+inPath+" at "+Calendar.getInstance().getTime());
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FAILED File: "+inPath+" at "+Calendar.getInstance().getTime());
		}	
	}
	
	
	
	/**
	 * START ENGINE
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("START: "+Calendar.getInstance().getTime());
		
		String outFile = "full-enwiki-text-file.txt";												//TODO for end content
		String dataSource = "C:/Users/Subadmin/Desktop/Raw Dataset/";
		SupportingFileContent sfc = new SupportingFileContent();
		ArrayList<String> filepaths = sfc.getFolderFileName(dataSource);
		String[] infiles = new String[filepaths.size()];
		String[] outFilesPara = new String[filepaths.size()];
		ExecuteParsingDatacollection initEDP = new ExecuteParsingDatacollection();					//For initial
		ExecuteParsingDatacollection [] epds = new ExecuteParsingDatacollection[filepaths.size()];
		
		
		for (int i = 0; i < filepaths.size(); i++) 
		{
			infiles[i] = dataSource+filepaths.get(i);			
			outFilesPara[i] = "en-wiki-annotations-and-depencies-parallel_"+initEDP.wikiArticleName(infiles[i])+".xml";
			epds[i] = new ExecuteParsingDatacollection();
		}
		
		IntStream.range(0, infiles.length).parallel().forEach(file_ID -> 
		{
			int reportUpdate = 10000;
			
			synchronized(epds[file_ID])
			{
				String inPath = infiles[file_ID];
				String rootElem = "wikiAnnotations_from_"+epds[file_ID].wikiArticleName(inPath);
				String outPath = outFilesPara[file_ID];

				epds[file_ID].oneCollectorExecute(inPath, outPath, new XMLParserSAXStyle(), reportUpdate, rootElem, file_ID);
			}
		});
		
		System.out.println("END ALL at "+Calendar.getInstance().getTime());
	}

}
