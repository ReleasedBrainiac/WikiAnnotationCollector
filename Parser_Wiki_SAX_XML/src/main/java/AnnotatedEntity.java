package main.java;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
	public AnnotatedEntity(String annotedText, boolean isFullTextAbstraction)
	{
		if(isFullTextAbstraction)
		{
			getAnnotedTextOnly(annotedText, annotedObjects);	
			
		}else{
			getAnnotationsAndUrls(annotedText, annotedObjects);
		}
		
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	public void getAnnotedTextOnly(String text, List<AnnotObject> annotedObjects)
	{
		List<String> content = new ArrayList<String>(); 
		String line;
		boolean isRex = false;
		
		//TODO Regex start end reference tags over multiple lines => http://www.rexegg.com/regex-quickstart.html
		//TODO Wichtig MARKUP -> https://en.wikipedia.org/wiki/Help:Wiki_markup
		
		
		String regexStr = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");
		String regexUri = "\\b(http?|https|Image|File)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		String regexHeader = Pattern.quote("==") + "(.*?)" + Pattern.quote("==");
		String regexGK = Pattern.quote("{{") + "(.*?)" + Pattern.quote("}}");
		String regexRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/ref>");
		String regSpecRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/>");

		
		List<String> sentences = Arrays.asList(text.split("\n"));		
		
		if(sentences.size() > 10)
		{
			for(int k = 0; k < sentences.size(); k++)
			{
				String subSentence = sentences.get(k).replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "");
				Appendings ape = null;
				
				BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
				iterator.setText(subSentence);
				int start = iterator.first();
				
				for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
				{
					line = subSentence.substring(start,end).replaceAll(regexUri, "").replaceAll("'", "");
					
					if(isRex || line.contains("</ref>") || line.contains("<ref") || line.contains("{{") || line.contains("}}"))
						{
							if(line.contains("{{"))
							{
								isRex = true;
								ape = new Appendings("{{","}}");
								ape.appending(line);
							}
							
							if(line.contains("<ref"))
							{
								isRex = true;
								ape = new Appendings("<ref","</ref>");
								ape.appending(line);
							}
							
							if(isRex && ape != null)
							{
								if(line.contains("</ref>") || line.contains("}}") || line.contains("/>"))
								{
									//TODO beim bereinigen den regEx ersetzen falls der endTag anders ist bei der REF!
									
									ape.appending(line);
									
									if(ape.getAppendings().contains("[[") && ape.getAppendings().contains("]]") && ape.getAppendings().contains(".") && !ape.getAppendings().contains("[[File:") && !ape.getAppendings().contains("[[Image:"))
									{
										content.add(ape.getAppendings().replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "").replaceAll(regexUri, "").replaceAll(regSpecRef, ""));
									}
									
									isRex = false;
									
								}else{
									ape.appending(line);
								}
							}
							
						}else{
							
							if(line.indexOf("*") != 0 && line.contains("[[") && line.contains("]]") && line.contains(".")&& !line.contains("[[File:") && !line.contains("[[Image:"))
							{
								line.replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "").replaceAll(regexUri, "").replaceAll(regSpecRef, "").replaceAll("", " ");
								content.add(line);
							}
							
							
						}
				}
			}
			
			for(String str : content)
			{
				System.out.println(str);
			}
			
			System.out.println("DONE [content size : "+content.size()+"]");
			
		}
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	public String cleaningContent(String line, boolean isRex, Appendings ape)
	{	
		String retStr = null;
		
//		if(isRex || line.contains("</ref>") || line.contains("<ref") || line.contains("{{") || line.contains("}}"))
//			{
//				if(line.contains("{{"))
//				{
//					isRex = true;
//					ape = new Appendings("{{","}}");
//					ape.appending(line);
//				}
//				
//				if(line.contains("<ref"))
//				{
//					isRex = true;
//					ape = new Appendings("<ref","</ref>");
//					ape.appending(line);
//				}
//				
//				if(isRex && ape != null)
//				{
//					if(line.contains("</ref>") || line.contains("}}"))
//					{
//						ape.appending(line);
//						
//						System.out.println("APE _-> "+ape.getCleanContent());
//						
//						content.add(ape.getCleanContent());
//						
//						
//						isRex = false;
//						
//					}else{
//						ape.appending(line);
//					}
//				}else{
////					System.out.println("APE null or no regEX discovered!");
//				}
//				
//			}else{
//				
//				if(line.contains("[[") && line.contains("]]"))
//				{
//					if(line.indexOf("*") != 0)
//					{
//							
//						content.add(line);
//					}
//				}else{
//					//Skip the unused Crap
//				}
//				
//				
//			}	
		
		return retStr;
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
//		List<String> annotedWords2 = new ArrayList<String>();
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
//					 annotedWords2.add(annotation);
					 
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
