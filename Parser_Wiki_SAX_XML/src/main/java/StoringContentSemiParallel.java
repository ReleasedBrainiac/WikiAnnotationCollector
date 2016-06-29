package main.java;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

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
	public StoringContentSemiParallel(String pathXML, String rootElement, List<AnnotatedEntity> aeList)
	{
		System.out.println("Storing Content!");
		saveToXML(pathXML, rootElement, aeList);
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
	public void saveToXML(String pathXML, String rootElement, List<AnnotatedEntity> aeList) 
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
	        
	        IntStream.range(0, aeList.size()).parallel().forEach(id -> 
			{
				synchronized (this) 
				{
					try 
	                {
	                	
	                	//Node for text
	    	        	AnnotatedEntity curAnnotEnt = aeList.get(id);
	    	        	
	    	        	if(curAnnotEnt.getAnnotedObjects().size() > 0)
	    	        	{        	
	    			        
	    			        //Node for annotaions
	    			        List<AnnotObject> aoList = curAnnotEnt.getAnnotedObjects();
	    			        for(int j = 0; j < aoList.size(); j++)
	    			        {
	    			        	Element objectElem = dom.createElement("AnnotationNotification");
	    				        rootElem.appendChild(objectElem);
	    				        
	    			        	AnnotObject ao = aoList.get(j);
	    			        	
	    			        	Element sentenceElem  = dom.createElement("Sentence");
	    			        	sentenceElem.appendChild(dom.createTextNode(ao.getAnnotedSentence()));
	    			        	objectElem.appendChild(sentenceElem);
	    			        	
	    			        	List<String> annots = ao.getAnnotedWords();
	    			        	Element wordElem = null;
	    				        for(int k = 0; k < annots.size(); k++)
	    				        {				        	
	    				        	wordElem = dom.createElement("Annotation");
	    				        	wordElem.appendChild(dom.createTextNode(annots.get(k)));
	    				        	objectElem.appendChild(wordElem);
	    				        } 
	    			       
	    				        List<String> urls = ao.getCorrespondingURLs();
	    				        Element subElem = null;
	    				        for(int k = 0; k < urls.size(); k++)
	    				        {
	    				        	//Subnode for annotaions corresponding urls
	    			        		subElem = dom.createElement("Url");
	    			        		subElem.appendChild(dom.createTextNode(urls.get(k)));
	    			        		objectElem.appendChild(subElem);
	    				        } 
	    			        }
	    	        	}
	                	
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
				}
				
                
            });

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
