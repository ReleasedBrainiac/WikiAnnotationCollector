package main.java;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Common SAX-XML-Parser take a "enwiki.~ ~.xml" file and gather all consisting annotations which aren't file or image annotations.
 * @author T.Turke
 *
 */
public class XMLParserSAXStyle extends DefaultHandler 
{
	private static List<AnnotatedEntity> anotEnts = new ArrayList<AnnotatedEntity>();
	public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private boolean isParallel = false;
	private static boolean isTextTag =false;
	private static String text = null;
	public static int reportUpdate = 500;
	public static int run = 0;
	
	
	/**
	 * Override: Now check existence of the tag your searching for.
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		text = "";
		isTextTag = false;
		if(qName.equalsIgnoreCase("text")){
			isTextTag=true; 
			run++;
		}
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################

	/**
	 * Override: Now you got the text now go ahead and use it
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
		//TODO nur testcase
		switch (qName) 
		{
			case "text":
			{
				if(isParallel)
				{
					anotEnts.add(new AnnotatedEntity(text));	//semi parallel
				}else{
					anotEnts.add(new AnnotatedEntity(text));	//linear
				}

				if(run % reportUpdate == 0)
				{
					runAndTime(run);
				}
			}	
		}
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################

	/**
	 * Override: Now do text abstraction and generation.
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		if(isTextTag)
		{	
			text += new String(ch, start, length);
		}
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * Report Update
	 * @param current desired node
	 */
	public void runAndTime(int run)
	{		
		System.out.println("Run Nr.: "+run+" and Time: "+dateFormat.format(Calendar.getInstance().getTime()));
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * This set process state [linear or parallel] depending on the boolean.
	 * DEFAULT linear = false
	 * MODIFIED parallel = true 
	 * @param isParallel
	 */
	@SuppressWarnings("static-access")
	public void setProcessState(boolean isParallel)
	{
		this.isParallel = isParallel;
	}
	
	/**
	 * This return process state.
	 * @return DEFAULT linear = false OR MODIFIED parallel = true 
	 */
	public boolean getProcessState()
	{
		return isParallel;
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	
	public void startProcess(String[] infiles, String[] outFiles, boolean isParallel, int reportUpdate, String rootElem)
	{
		IntStream.range(0, infiles.length).parallel().forEach(file_ID -> 
		{
			synchronized(this)
			{
				try 
				{
					System.out.println("START Thread for File => "+infiles[file_ID]);
					
					//For UTF-8 Setup
					File file = new File(infiles[file_ID]);
					InputStream inputStream;
					inputStream = new FileInputStream(file);
					Reader reader = new InputStreamReader(inputStream,"UTF-8");
					InputSource is = new InputSource(reader);
					is.setEncoding("UTF-8");
					
					//Init Parser
					SAXParserFactory parserFactor = SAXParserFactory.newInstance();
					SAXParser parser = parserFactor.newSAXParser();
					XMLParserSAXStyle handler = new XMLParserSAXStyle();
					handler.setProcessState(isParallel);
					handler.reportUpdate = reportUpdate;
					parser.parse(is, handler);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
								
				String rootElement = rootElem;
				String fileName = outFiles[file_ID];
				new FileGenerator(fileName);
				
				if(isParallel)
				{
					new StoringContentSemiParallel(fileName, rootElement, anotEnts);
				}else{
					new StoringContent(fileName, rootElement, anotEnts);
				}
				
				System.out.println("Done File: "+infiles[file_ID]+" at "+Calendar.getInstance().getTime());
			}
		});
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * Central Main-method for the tool.
	 * @param args
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException 
	{
		System.out.println("START: "+Calendar.getInstance().getTime());
		
		XMLParserSAXStyle parser = new XMLParserSAXStyle();
		
		String[] infiles = new String[4];
		infiles[0] = "C:/Users/Subadmin/Desktop/zipped raw Datasets/enwiki-latest-pages-articles1.xml";
		infiles[1] = "C:/Users/Subadmin/Desktop/zipped raw Datasets/enwiki-latest-pages-articles10.xml";
		infiles[2] = "C:/Users/Subadmin/Desktop/zipped raw Datasets/enwiki-latest-pages-articles11.xml";
		infiles[3] = "C:/Users/Subadmin/Desktop/zipped raw Datasets/enwiki-latest-pages-articles12.xml";
		
		String[] outFilesPara = new String[4];
		outFilesPara[0] = "en-wiki-annotations-and-depencies-parallel_1.xml";
		outFilesPara[1] = "en-wiki-annotations-and-depencies-parallel_10.xml";
		outFilesPara[2] = "en-wiki-annotations-and-depencies-parallel_11.xml";
		outFilesPara[3] = "en-wiki-annotations-and-depencies-parallel_12.xml";
		
		String[] outFilesLin = new String[4];
		outFilesLin[0] = "en-wiki-annotations-and-depencies-lin_1.xml";
		outFilesLin[1] = "en-wiki-annotations-and-depencies-lin_10.xml";
		outFilesLin[2] = "en-wiki-annotations-and-depencies-lin_11.xml";
		outFilesLin[3] = "en-wiki-annotations-and-depencies-lin_12.xml";
		
		String rootElement = "wikiAnnotations";										// = args[2] //is also a possible option to implement.
		boolean isParallel = true;
		
		if(isParallel)
		{
			parser.startProcess(infiles, outFilesPara, isParallel, 1000, rootElement);
		}else{
			parser.startProcess(infiles, outFilesLin, isParallel, 1000, rootElement);
		}
		
		
		
		
//		for (int i = 0; i < infiles.length; i++) 
//		{
//			//For UTF-8 Setup
//			File file = new File(infiles[i]);
//			InputStream inputStream= new FileInputStream(file);
//			Reader reader = new InputStreamReader(inputStream,"UTF-8");
//			InputSource is = new InputSource(reader);
//			is.setEncoding("UTF-8");
//			
//			//Init Parser
//			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
//			SAXParser parser = parserFactor.newSAXParser();
//			XMLParserSAXStyle handler = new XMLParserSAXStyle();
//			handler.setProcessState(isParallel);
//			handler.reportUpdate = 1000;
//			parser.parse(is, handler);
//			
//			//Dateien- und Dom-Bezeichner festlegen
//			
//			String fileName = null;
//			
//			if(isParallel)
//			{
//				fileName = outFilesPara[i];
//				new FileGenerator(fileName);
//				new StoringContentSemiParallel(fileName, rootElement, anotEnts);
//			}else{
//				fileName = outFilesLin[i];
//				new FileGenerator(fileName);
//				new StoringContent(fileName, rootElement, anotEnts);
//			}
//		}
		

		System.out.println("END ALL at "+Calendar.getInstance().getTime());
	}
}
