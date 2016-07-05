package main.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;

/**
 * This class start the process pipe from gathering the wanted stuff to storing as usable dataset.
 * @author T.Turke
 *
 */
public class Execute 
{

	/**
	 * EXECUTION!
	 */
	public static void main(String[] args) 
	{
		System.out.println("GENERAL START: "+Calendar.getInstance().getTime());

		String dataSourceCollection = "C:/Users/Subadmin/Desktop/Raw Dataset/";
		String dataSourceMerge = "C:/Users/Subadmin/Desktop/Cleaned/";
		String dataSourceCollectionDestination = "C:/Users/Subadmin/Desktop/Cleaned/";
		String dataSourceMergeDestination = "C:/Users/Subadmin/Desktop/Cleaned Fulltext/full-enwiki-text-file.txt";
		int reportUpdateMerge = 10000;
		
		
		SupportingFileContent sfc = new SupportingFileContent();
		ArrayList<String> filepaths = sfc.getFolderFileName(dataSourceCollection);
		ArrayList<File> inFiles = sfc.getFolderFiles(dataSourceMerge);
		
		String[] outFilesPara = new String[filepaths.size()];
		String[] infiles = new String[filepaths.size()];
		
		ExecuteParsingDatacollection initEDP = new ExecuteParsingDatacollection();					//For initial
		ExecuteParsingDatacollection [] epds = new ExecuteParsingDatacollection[filepaths.size()];
		ExecuteParsingFullDataset epfd = new ExecuteParsingFullDataset();
		
		ParserXMLToText pxtt = new ParserXMLToText();
		
		
		// Collector execution
		for (int i = 0; i < filepaths.size(); i++) 
		{
			infiles[i] = dataSourceCollection+filepaths.get(i);			
			outFilesPara[i] = "en-wiki-annotations-and-depencies-parallel_"+initEDP.wikiArticleName(infiles[i])+".xml";
			epds[i] = new ExecuteParsingDatacollection();
		}
		
		IntStream.range(0, filepaths.size()).parallel().forEach(file_ID -> 
		{
			// Only change here! Depend on thread structure!
			int reportUpdateCollect = 10000;
			
			synchronized(epds[file_ID])
			{
				String inPath = infiles[file_ID];
				String rootElem = "wikiAnnotations_from_"+epds[file_ID].wikiArticleName(inPath);
				String outPath = outFilesPara[file_ID];
				epds[file_ID].oneCollectorExecute(inPath, outPath, new XMLParserSAXStyle(), reportUpdateCollect, rootElem, file_ID, dataSourceCollectionDestination);
			}
		});
		

		// Merge execution
		for(int a_it = 0; a_it < inFiles.size(); a_it++)
		{
			epfd.executeDatsetConstruction(inFiles.get(a_it).getAbsolutePath(), dataSourceMergeDestination, pxtt, reportUpdateMerge);
		}

		System.out.println("GENERAL END: "+Calendar.getInstance().getTime());
	}

}
