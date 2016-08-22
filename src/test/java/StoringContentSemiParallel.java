import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class stores the gathered annotations from "enwiki.~ ~.xml" file and save them to another structured XML file. 
 * [semi parallelized]
 * @author T.Turke
 *
 */
public class StoringContentSemiParallel 
{
	/**
	 * Constructor initialize the content storing process. 
	 * [semi parallel]
	 * @param pathXML
	 * @param rootElement
	 * @param aeList
	 */
	public StoringContentSemiParallel(String pathXML, String rootElement, List<AnnotedEntitySemiParallel> aeList, String destination)
	{
		System.out.println("Storing Content!");		
		saveAllToXML(destination+pathXML, rootElement, aeList);
		saveSentencesToXML(destination+"sentences-"+pathXML, rootElement, aeList);
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * This method construct and fill XML + Domain and finally save the stuff.
	 * [semi parallel]
	 * @param pathXML
	 * @param rootElement
	 * @param aeList
	 */
	public void saveSentencesToXML(String pathXML, String rootElement, List<AnnotedEntitySemiParallel> aeList) 
	{
	    Document dom;
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    
	    try {
	        // use factory to get an instance of document builder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        // create instance of DOM
	        dom = db.newDocument();

	        // create the root element
	        Element rootElem = dom.createElement(rootElement);
	        
//	        IntStream.range(0, aeList.size()).parallel().forEach(id -> 
//			{
//				synchronized (this) 
//				{
//					try 
//	                {
//	    	        	AnnotedEntitySemiParallel curAnnotEnt = aeList.get(id);
//	    	        	
//	    	        	if(curAnnotEnt.getAnnotedObjects().size() > 0)
//	    	        	{        	
//	    			        List<AnnotObject> aoList = curAnnotEnt.getAnnotedObjects();
//	    			        Element objectElem = dom.createElement("AnnotationNotification");
//    			        	Element sentenceElem  = dom.createElement("Sentence");
//	    			        
//	    			        for(int j = 0; j < aoList.size(); j++)
//	    			        {
//	    			        	AnnotObject ao = aoList.get(j);
//	    				        rootElem.appendChild(objectElem);
//	    			        	sentenceElem.appendChild(dom.createTextNode(ao.getAnnotedSentence()));
//	    			        	objectElem.appendChild(sentenceElem);
//	    			        }
//	    	        	}
//	                } catch (Exception e) {e.printStackTrace();}
//				} 
//            });
	        
	        for (int i = 0; i < aeList.size(); i++) 
	        {
	        	AnnotedEntitySemiParallel curAnnotEnt = aeList.get(i);
	        	
	        	if(curAnnotEnt.getAnnotedObjects().size() > 0)
	        	{        	
			        List<AnnotObject> aoList = curAnnotEnt.getAnnotedObjects();
			        Element objectElem = dom.createElement("AnnotationNotification");
		        	Element sentenceElem  = dom.createElement("Sentence");
			        
			        for(int j = 0; j < aoList.size(); j++)
			        {
			        	AnnotObject ao = aoList.get(j);
				        rootElem.appendChild(objectElem);
			        	
			        	if(j > 0)
			        	{
			        		if(!aoList.get(j-1).getAnnotedSentence().contains(ao.getAnnotedSentence()))
			        		{
			        			sentenceElem.appendChild(dom.createTextNode(ao.getAnnotedSentence()));
					        	objectElem.appendChild(sentenceElem);
			        		}	
			        	}else{
			        		sentenceElem.appendChild(dom.createTextNode(ao.getAnnotedSentence()));
				        	objectElem.appendChild(sentenceElem);
			        	}
			        	
			        	
			        	
			        }
	        	}
			}
	        
	        

	        dom.appendChild(rootElem);

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(pathXML)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.out.println(ioe.getMessage());
	        }
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * This method construct and fill XML + Domain and finally save the stuff.
	 * [NOT parallelized because of the occurring concurrency between simple objects!]
	 * @param pathXML
	 * @param rootElement
	 * @param aeList
	 */
	public void saveAllToXML(String pathXML, String rootElement, List<AnnotedEntitySemiParallel> aeList) 
	{
	    Document dom;
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    
	    try {
	        // use factory to get an instance of document builder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        // create instance of DOM
	        dom = db.newDocument();

	        // create the root element
	        Element rootElem = dom.createElement(rootElement);
	        
	        for(int iter = 0; iter < aeList.size(); iter++)
	        {
	        	AnnotedEntitySemiParallel curAnnotEnt = aeList.get(iter);
				
				try 
                {
    	        	if(curAnnotEnt.getAnnotedObjects().size() > 0)
    	        	{        	
    			        
    			        //Node for annotaions
    			        List<AnnotObject> aoList = curAnnotEnt.getAnnotedObjects();
    			        Element objectElem = null;
			        	Element sentenceElem  = null;
			        	Element wordElem = null;
			        	Element urlElem = null;
    			        
    			        for(int j = 0; j < aoList.size(); j++)
    			        {
    			        	AnnotObject ao = aoList.get(j);
    			        	List<String> annots = ao.getAnnotedWords();
    			        	List<String> urls = ao.getCorrespondingURLs();
    			        	
    			        	// Set Notification Object
    			        	objectElem = dom.createElement("AnnotationNotification");
    				        rootElem.appendChild(objectElem);
    				        
    				        // Set Sentence Object
    				        sentenceElem  = dom.createElement("Sentence");
    			        	sentenceElem.appendChild(dom.createTextNode(ao.getAnnotedSentence()));
    			        	objectElem.appendChild(sentenceElem);
    			        	
    			        	// Set Annotation Objects
    				        for(int k = 0; k < annots.size(); k++)
    				        {				        	
    				        	wordElem = dom.createElement("Annotation");
    				        	wordElem.appendChild(dom.createTextNode(annots.get(k)));
    				        	sentenceElem.appendChild(wordElem);
    				        } 
    				        
    				        // Set Url Objects
    				        for(int k = 0; k < urls.size(); k++)
    				        {
    			        		urlElem = dom.createElement("Url");
    			        		urlElem.appendChild(dom.createTextNode(urls.get(k)));
    			        		sentenceElem.appendChild(urlElem);
    				        } 
    			        }
    	        	}
                	
                } catch (Exception e) {
                    e.printStackTrace();
                }
	        }
	        
	        dom.appendChild(rootElem);

	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(pathXML)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.out.println(ioe.getMessage());
	        }
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
	}
}
