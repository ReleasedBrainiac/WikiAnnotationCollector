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
import java.util.Date;
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
public class XMLParserSAXStyle extends DefaultHandler 
{
	private static List<AnnotatedEntity> anotEnts = new ArrayList<AnnotatedEntity>();
	private static String text = null;
	private static boolean isTextTag =false;
	public static int run = 0;
	
	public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	

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
				anotEnts.add(new AnnotatedEntity(text));
				if(run % 1000 == 0)
				{
					runAndTime(run);
				}
			}	
		}
	}

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
	
	/**
	 * For performance testing
	 * @param run
	 */
	public void runAndTime(int run)
	{		
		
		System.out.println("Run Nr.: "+run+" and Time: "+dateFormat.format(cal.getTime()));
	}

	/**
	 * Central Main-method for the tool.
	 * @param args
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException 
	{
		final Date START_TIME = cal.getTime();
		System.out.println("Start: "+START_TIME);
		System.out.println("Desired elements = 19875");
		
		//String path = args[0];
		String path = "C:/Users/Tobia/Desktop/Texteressourcen f�r Arbeit/enwiki-latest-pages-articles1.xml";
		
		//For UTF-8 Setup
		File file = new File(path);
		InputStream inputStream= new FileInputStream(file);
		Reader reader = new InputStreamReader(inputStream,"UTF-8");
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		
		//Init Parser
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		XMLParserSAXStyle handler = new XMLParserSAXStyle();
		parser.parse(is, handler);
		
//		String fileName = "en-wiki-annotations-and-depencies.xml";	// = args[1] //is also a possible option to implement.
		String fileName = "en-wiki-annotations-and-depencies-parallel.xml";	// = args[1] //is also a possible option to implement.
		String rootElement = "wikiAnnotations";						// = args[2] //is also a possible option to implement.
		
		//generate xml file
		new FileGenerator(fileName);
		
		//save content to it
		new StoringContent(fileName, rootElement, anotEnts);
		
		final Date END_TIME = cal.getTime();
		System.out.println("End: "+END_TIME);
		
		System.out.println("Time needed: "+(END_TIME.getTime()-START_TIME.getTime()));
		System.out.println("Items: "+run);
		
		
	}
}
