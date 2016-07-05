package main.java;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Common SAX-XML-Parser take a enwiki sentence xml and create a textfile.
 * @author T.Turke
 *
 */
public class ParserXMLToText extends DefaultHandler 
{
	private boolean isTextTag =false;
	private static String text = null;
	public StringBuilder everything = new StringBuilder();
	private int reportUpdate = 500;
	private int run = 0;
	
	public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	
	
	/**
	 * Override: Now check existence of the tag your searching for.
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		text = "";
		isTextTag = false;
		if(qName.equalsIgnoreCase("Sentence")){
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
		switch (qName) 
		{
			case "Sentence":
			{
				everything.append(text);

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
		System.out.println("Run => "+run+" | Time: "+dateFormat.format(Calendar.getInstance().getTime()));
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################

	public int getReportUpdate() {
		return reportUpdate;
	}

	public void setReportUpdate(int reportUpdate) {
		this.reportUpdate = reportUpdate;
	}

	public StringBuilder getEverything() {
		return everything;
	}

	public void setEverything(StringBuilder everything) {
		this.everything = everything;
	}
	
	public String getFulltext()
	{
		return everything.toString();
	}
}

