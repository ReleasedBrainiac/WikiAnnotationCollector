package main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used for the gathering of text annotation from given "Wikipedia dump file" 
 * and the generation of there corresponding url's  
 * @author T.Turke
 */
public class AnnotatedEntity 
{
	private List<AnnotObject> annotedObjects = new ArrayList<AnnotObject>();
	
	/**
	 * Initials constructor no parameters
	 */
	public AnnotatedEntity(){}
	
	/**
	 * Initials constructor with parameters initialize the gathering of 
	 * annotations and the creation of there corresponding url's.
	 * @param annotedText
	 */
	public AnnotatedEntity(String annotedText)
	{
		getAnnotationsAndUrls(annotedText, annotedObjects);
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * This method generates annotated objects, which gather all annotations of a given Text 
	 * and keep the generated corresponding Url's of the gathered annotations. 
	 * @param text
	 * @param annotedObjects
	 * @return a list of AnnotObject's
	 */
	public void getAnnotationsAndUrls(String text, List<AnnotObject> annotedObjects)
	{
		String regexStr = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");
		List<String> annotedWords2 = new ArrayList<String>();
		List<String> correspondingURLs2 = new ArrayList<String>();
		List<String> sentences = Arrays.asList(text.split("\\."));		
		
		for (int i = 0; i < sentences.size(); i++) 
		{
			if(!sentences.contains("File:") || !sentences.contains("Image:"))
			{
				Pattern pat = Pattern.compile(regexStr);	
				Matcher m = pat.matcher(sentences.get(i));
				
				//Die Suche nach den Annotations und die Generation der Urls
				while(m.find())
				{
					 String annotation = m.group(1);
					 String[] tmp;
					 correspondingURLs2 = new ArrayList<String>();
					 annotedWords2.add(annotation);
					 
					 if(annotation.contains("|"))
					 {
						 tmp = annotation.split("\\|");
						 
						 for(String annot : tmp){
							 if(!annot.contains(" "))
							 {		correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annot);}
							 else{	correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annot.replace(" ", "_"));}  
						 }

					 }else{
						 
						 if(!annotation.contains(" "))
						 {		correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annotation);}
						 else{	correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annotation.replace(" ", "_"));} 
					 } 
					 
					 annotedObjects.add(new AnnotObject(annotation, correspondingURLs2)); 
				}
			}
		}
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	public List<AnnotObject> getAnnotedObjects() {
		return annotedObjects;
	}

	public void setAnnotedObjects(List<AnnotObject> annotedObjects) {
		this.annotedObjects = annotedObjects;
	}
}
