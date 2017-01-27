package engine;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
	private List<AnnotedEntitySemiParallel> anotEnts = new ArrayList<AnnotedEntitySemiParallel>();
	private boolean isTextTag =false;
	private static String text = null;
	private int reportUpdate = 500;
	private int thread_ID = -1;
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
		switch (qName) 
		{
			case "text":
			{
				anotEnts.add(new AnnotedEntitySemiParallel(text));	//semi parallel

				if(run % reportUpdate == 0)
				{
					runAndTime(run, getThread_ID());
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
	public void runAndTime(int run, int thread_ID)
	{		
		System.out.println("Thread => "+thread_ID+" | Run => "+run+" | Time: "+dateFormat.format(Calendar.getInstance().getTime()));
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################

	public List<AnnotedEntitySemiParallel> getAnotEnts() {
		return anotEnts;
	}

	public void setAnotEnts(List<AnnotedEntitySemiParallel> anotEnts) {
		this.anotEnts = anotEnts;
	}

	public int getReportUpdate() {
		return reportUpdate;
	}

	public void setReportUpdate(int reportUpdate) {
		this.reportUpdate = reportUpdate;
	}

	public int getThread_ID() {
		return thread_ID;
	}

	public void setThread_ID(int thread_ID) {
		this.thread_ID = thread_ID;
	}
	
	
}
