package main.java;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
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

	/**
	 * Override: Now check existence of the tag your searching for.
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		if(qName.equalsIgnoreCase("text")) isTextTag=true;
	}

	/**
	 * Override: Now you got the text now go ahead and use it
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
		switch (qName) 
		{
			case "text":
			{
				anotEnts.add(new AnnotatedEntity(text));
			}	
		}
	}

	/**
	 * Override: Now do text abstraction and generation.
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		if(isTextTag){
			text = new String(ch, start, length);
		}
		isTextTag = false;
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
		
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		XMLParserSAXStyle handler = new XMLParserSAXStyle();
		parser.parse(new File(args[0]), handler);
		String fileName = "en-wiki-annotations-and-depencies.xml";	// = args[1] //is also a possible option to implement.
		String rootElement = "wikiAnnotations";						// = args[2] //is also a possible option to implement.
		
		//generate xml file
		new FileGenerator(fileName);
		
		//save content to it
		new StoringContent(fileName, rootElement, anotEnts);
		
	}
}
