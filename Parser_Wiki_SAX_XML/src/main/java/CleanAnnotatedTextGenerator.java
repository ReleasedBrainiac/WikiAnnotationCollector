package main.java;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
public class CleanAnnotatedTextGenerator extends DefaultHandler 
{
	private static boolean isSentence =false;
	private static String text = null;
	public static List<String> textFragments = new ArrayList<String>();
	
	
	/**
	 * Override: Now check existence of the tag your searching for.
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		text = "";
		isSentence = false;
		if(qName.equalsIgnoreCase("Sentence")){
			isSentence=true; 
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
			case "Sentence":
			{	
				String t = text;
				
				//TODO gather sentences and build up the text -> textFragments
				//TODO stream the text into the file
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
		if(isSentence)
		{	
			text += new String(ch, start, length);
		}
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
		//String path = args[0];
		String path = "C:/Users/Tobia/Desktop/en-wiki-annotations-and-depencies.xml";
		
		//For UTF-8 Setup
		File file = new File(path);
		InputStream inputStream= new FileInputStream(file);
		Reader reader = new InputStreamReader(inputStream,"UTF-8");
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		
		//Init Parser
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		CleanAnnotatedTextGenerator handler = new CleanAnnotatedTextGenerator();
		parser.parse(is, handler);
		
		//Dateien-Bezeichner festlegen
		
		String fileName = null;
		new FileGenerator(fileName);												// = args[1] //is also a possible option to implement.

		//TODO Storing as *.txt file -> textFragments
	}
}
