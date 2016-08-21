import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;

/**
 * This class collect all desired types of sentences from given enwiki-article.xml and store it into another structured xml.
 * @author T.Turke
 */
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
	 * This method define 1 collector for 1 file. It can be used in a thread structure.
	 * @param inPath
	 * @param outPath
	 * @param handler
	 * @param reportUpdate
	 * @param rootElem
	 * @param thread_ID
	 */
	public void oneCollectorExecute(String inPath, String outPath, XMLParserSAXStyle handler, int reportUpdate, String rootElem, int thread_ID, String destination)
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
			new StoringContentSemiParallel(outPath, rootElem, handler.getAnotEnts(), destination);
			System.out.println("Done File: "+inPath+" at "+Calendar.getInstance().getTime());
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FAILED File: "+inPath+" at "+Calendar.getInstance().getTime());
		}	
	}
}
