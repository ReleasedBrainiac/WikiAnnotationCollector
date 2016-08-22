import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Common SAX-XML-Parser take a enwiki sentence xml and create a text file.
 * 
 * Attention: There seem to be some errors with the variable "text" during clear => lead to some duplicated contents.
 * @author T.Turke
 */
public class ParserXMLToText extends DefaultHandler 
{
	
	private String regexStr = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");
	private boolean isTextTag =false;
	private static String text;
	private int reportUpdate = 500;
	private int run = 0;

	public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	public ArrayList<String> lines = new ArrayList<String>();
	public int max_sized_annotation = -1;
	
	/**
	 * Override: Now check existence of the tag your searching for.
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		text ="";
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
				Pattern pattern = Pattern.compile(regexStr);
				Matcher matcher = pattern.matcher(text);
				
				if (matcher.find()){ max_sized_annotation = Math.max(max_sized_annotation, matcher.group(1).length()+4);}
				if(run % reportUpdate == 0){runAndTime(run);}
				
				//TODO umdenken
				
				if(lines.size()>1)
				{
					if(!lines.get(lines.size()-1).equals(text))
					{
						if(text.contains(lines.get(lines.size()-1)))
						{
							lines.add(text.replace(lines.get(lines.size()-1), ""));
						}else{
						}
							lines.add(text);
						}
				}else{
					lines.add(text);
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

	public ArrayList<String> getLines() {
		return lines;
	}

	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}
}

