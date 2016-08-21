import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * This class is used for the gathering of text annotation from given "Wikipedia dump file" 
 * [semi parallel]
 * and the generation of there corresponding url's  
 * @author T.Turke
 */
public class AnnotedEntitySemiParallel 
{
	private List<AnnotObject> annotedObjects = new ArrayList<AnnotObject>();
	private List<String> sentencesLinear = new ArrayList<String>();
	private List<String> sentencesParallel = new ArrayList<String>();
	
	/**
	 * Initials constructor no parameters
	 */
	public AnnotedEntitySemiParallel(){}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * Initials constructor with parameters initialize the gathering of 
	 * annotations and the creation of there corresponding url's.
	 * @param annotedText
	 */
	public AnnotedEntitySemiParallel(String annotedText)
	{		
		this.sentencesLinear = getAnnotedTextOnlyParallel(annotedText);
		
		IntStream.range(0, getSentencesLinear().size()).parallel().forEach(pos -> 
		{
			synchronized (this) 
			{
				try
				{
					getAnnotationsAndUrls(getSentencesLinear().get(pos), annotedObjects);
                	
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}
        });	
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * This method gather only annotated sentences from given text and return it as list.
	 * @param text
	 * @return list of annotated sentences
	 */
	public List<String> getAnnotedTextOnlyParallel(String text)
	{
		// Regex multiline reference 	=> http://www.rexegg.com/regex-quickstart.html
		// MARKUP reference 			=> https://en.wikipedia.org/wiki/Help:Wiki_markup
		
		List<String> sentences = Arrays.asList(text.split("\n"));
		List<String> firstStepOut = new ArrayList<String>(); 
		List<String> content = new ArrayList<String>();
		boolean isRex = false; 
		String line;
		
		//Necessary regular expressions
		String regexUri = "\\b(http?|https|Image|File)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";		
		String testReg2 = "^[a-zA-Z0-9_.|]*\\[\\]";																	
		String regexHeader = Pattern.quote("==") + "(.*?)" + Pattern.quote("==");
		String regexRK = Pattern.quote("(") + "(.*?)" + Pattern.quote(")");
		String regexGK = Pattern.quote("{{") + "(.*?)" + Pattern.quote("}}");										
		String regexRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/ref>");
		String regexSup = Pattern.quote("<sup") + "(.*?)" + Pattern.quote("/sup>");				// currently not in use but these kind of types occur
		String regSpecRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/>");									
		String finalRex = "^[\\w]+[A-Za-z,;()\\w´`\'\"\\s]*[\\[]+[A-Za-z,;()\\w\\|\"\\s]*[\\]]+(.*?)[.?!]$";		


		//Starting the collecting
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
									ape.appending(line);
									
									if(ape.getAppendings().contains("[[") && ape.getAppendings().contains("]]") && ape.getAppendings().contains(".") && !ape.getAppendings().contains("[[File:") && !ape.getAppendings().contains("[[Image:"))
									{
										String newContent = ape.getAppendings().replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "").replaceAll(regexUri, "").replaceAll(regSpecRef, "").replaceAll("´", "'").replaceAll("`", "'").replaceAll(testReg2, "").replaceAll(regexRK, "");
										if(newContent.length() > 0 && newContent != null && !newContent.contains("*") && newContent.contains("[[") && newContent.contains("]]") && newContent.contains(".")) content.add(newContent);

									}
									
									isRex = false;
									
								}else{
									ape.appending(line);
								}
							}
							
						}else{
							
							if(!line.contains("*") && line.contains("[[") && line.contains("]]") && line.contains(".")&& !line.contains("[[File:") && !line.contains("[[Image:"))
							{
								line.replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "").replaceAll(regexUri, "").replaceAll(regSpecRef, "").replaceAll("´", "'").replaceAll("`", "'").replaceAll(testReg2, "").replaceAll(regexRK, "");
								if(line.length() > 0 && line != null) content.add(line);	
							}
						}
				}
			}
			
			
			IntStream.range(0, content.size()).parallel().forEach(id -> 
			{
				synchronized (this) 
				{
					try
					{
	                	
	                	if(content.get(id) != null)
	                	{
	                		Pattern pat = Pattern.compile(finalRex);	
	        				Matcher m = pat.matcher(content.get(id));

	        				synchronized (this) 
	    					{
	        					while(m.find())
	            				{
	        						if(m.group(0) != null && m.group(0).length() > 0) firstStepOut.add(m.group(0));	
	            				}
	    					}
	                	}else{
	                		System.out.println("Empty: "+content.get(id));
	                	}
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
				}
            });			
		}
		return firstStepOut;
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
	public void getAnnotationsAndUrls(String sentence, List<AnnotObject> annotedObjects)
	{
		String regexStr = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");
		List<String> correspondingURLs2 = new ArrayList<String>();
		List<String> annotations = new ArrayList<String>();	
		
		if(sentence != null)
		{
			Pattern pat = Pattern.compile(regexStr);	
			Matcher m = pat.matcher(sentence);
			
			//Die Suche nach den Annotations und die Generation der Urls
			while(m.find())
			{
				String annotation = m.group(1);
				annotations.add(annotation);
				
				String[] tmp;
				correspondingURLs2 = new ArrayList<String>();
				 
				 
				 
				 if(annotation.contains("|"))
				 {
					 tmp = annotation.split("\\|");
					 
					 for(String annot : tmp)
					 {
						 if(!annot.contains(" "))
						 {		correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annot);}
						 else{	correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annot.replace(" ", "_"));}  
					 }

				 }else{
					 
					 if(!annotation.contains(" "))
					 {		correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annotation);}
					 else{	correspondingURLs2.add("https://en.wikipedia.org/wiki/"+annotation.replace(" ", "_"));} 
				 } 
			}
			annotedObjects.add(new AnnotObject(sentence, annotations, correspondingURLs2)); 
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

	public List<String> getSentencesLinear() {
		return sentencesLinear;
	}

	public void setSentencesLinear(List<String> sentences) {
		this.sentencesLinear = sentences;
	}

	public List<String> getSentencesParallel() {
		return sentencesParallel;
	}

	public void setSentencesParallel(List<String> sentences) {
		this.sentencesParallel = sentences;
	}
	
	
}

