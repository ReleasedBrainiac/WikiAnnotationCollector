package main.java;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * This class is used for the gathering of text annotation from given "Wikipedia dump file" [Linear]
 * and the generation of there corresponding url's  
 * @author T.Turke
 */
public class AnnotatedEntity 
{
	private List<AnnotObject> annotedObjects = new ArrayList<AnnotObject>();
	private List<String> sentencesLinear = new ArrayList<String>();
	private List<String> sentencesParallel = new ArrayList<String>();
	
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
//		this.sentencesLinear = getAnnotedTextOnlyLinear(annotedText);
		this.sentencesParallel = getAnnotedTextOnlyParallel(annotedText);
		
		
		for (int i = 0; i < getSentencesLinear().size(); i++) 
		{
			getAnnotationsAndUrls(getSentencesLinear().get(i), annotedObjects);
		}
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * This class is used for the gathering of text annotation from given "Wikipedia dump file" [Parallel]
	 * and the generation of there corresponding url's  
	 * @author T.Turke
	 */
	public List<String> getAnnotedTextOnlyParallel(String text)
	{
		// Regex multiline reference 	=> http://www.rexegg.com/regex-quickstart.html
		// MARKUP reference 			=> https://en.wikipedia.org/wiki/Help:Wiki_markup
		
		List<String> firstStepOut = new ArrayList<String>(); 
		List<String> content = new ArrayList<String>();
		List<String> rexBib = new ArrayList<String>();
		int[] wantedRexes = new int[6];
//		boolean isRex = false; 
//		String line;
		
		
		//Necessary regular expressions
		String regexUri = "\\b(http?|https|Image|File)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";		rexBib.add(regexUri);
		String testReg2 = "^[a-zA-Z0-9_.|]*\\[\\]";																	rexBib.add(testReg2);
		String regexHeader = Pattern.quote("==") + "(.*?)" + Pattern.quote("==");									rexBib.add(regexHeader);
		String regexGK = Pattern.quote("{{") + "(.*?)" + Pattern.quote("}}");										rexBib.add(regexGK);
		String regexRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/ref>");									rexBib.add(regexRef);
		String regSpecRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/>");									rexBib.add(regSpecRef);
		String finalRex = "^[\\w]+[A-Za-z,;()\\w´`\'\"\\s]*[\\[]+[A-Za-z,;()\\w\\|\"\\s]*[\\]]+(.*?)[.?!]$";		rexBib.add(finalRex);
		
		wantedRexes[0] = 1;	// regexUri
		wantedRexes[1] = 2;	// testReg2
		wantedRexes[2] = 3;	// regexHeader
		wantedRexes[3] = 4;	// regexGK
		wantedRexes[4] = 5;	// regexRef
		wantedRexes[5] = 6;	// regSpecRef
		
		List<String> sentences = Arrays.asList(text.split("\n"));		
		
		if(sentences.size() > 10)
		{
			
			IntStream.range(0, sentences.size()).parallel().forEach(id -> 
			{
                try 
                {
                	boolean isRex = false;
                	String subSentence = sentences.get(id).replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "");
    				Appendings ape = null;
    				
    				BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
    				iterator.setText(subSentence);
    				int start = iterator.first();
    				
    				for (int end = iterator.next();end != BreakIterator.DONE;start = end, end = iterator.next()) 
    				{
    					String line = subSentence.substring(start,end).replaceAll(regexUri, "").replaceAll("'", "");
    					
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
    										content.add(specificRegEx(ape.getAppendings(), rexBib, wantedRexes));
    									}
    									
    									isRex = false;
    									
    								}else{
    									ape.appending(line);
    								}
    							}
    							
    						}else{
    							
    							if(line.indexOf("*") != 0 && line.contains("[[") && line.contains("]]") && line.contains(".")&& !line.contains("[[File:") && !line.contains("[[Image:"))
    							{
    								content.add(specificRegEx(line, rexBib, wantedRexes));
    							}
    							
    							
    						}
    				}
    				
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
			
			
			//Final collecting "parallel"
			IntStream.range(0, content.size()).parallel().forEach(id -> 
			{
                try {
                	
                	if(finalRex != null && content.get(id) != null)
                	{
                		Pattern pat = Pattern.compile(finalRex);	
        				Matcher m = pat.matcher(content.get(id));

        				while(m.find())
        				{
//        					System.out.println(m.group(0));
        					firstStepOut.add(m.group(0));
        				}
                	}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
			
		}
		
		return firstStepOut;
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	public List<String> getAnnotedTextOnlyLinear(String text)
	{
		// Regex multiline reference 	=> http://www.rexegg.com/regex-quickstart.html
		// MARKUP reference 			=> https://en.wikipedia.org/wiki/Help:Wiki_markup
		
		List<String> firstStepOut = new ArrayList<String>(); 
		List<String> content = new ArrayList<String>();
		List<String> rexBib = new ArrayList<String>();
		int[] wantedRexes = new int[6];
		boolean isRex = false; 
		String line;
		
		long start_millis = System.currentTimeMillis() % 1000;
		long end_millis;
		
		
		//Necessary regular expressions
		String regexUri = "\\b(http?|https|Image|File)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";		rexBib.add(regexUri);
		String testReg2 = "^[a-zA-Z0-9_.|]*\\[\\]";																	rexBib.add(testReg2);
		String regexHeader = Pattern.quote("==") + "(.*?)" + Pattern.quote("==");									rexBib.add(regexHeader);
		String regexGK = Pattern.quote("{{") + "(.*?)" + Pattern.quote("}}");										rexBib.add(regexGK);
		String regexRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/ref>");									rexBib.add(regexRef);
		String regSpecRef = Pattern.quote("<ref") + "(.*?)" + Pattern.quote("/>");									rexBib.add(regSpecRef);
		String finalRex = "^[\\w]+[A-Za-z,;()\\w´`\'\"\\s]*[\\[]+[A-Za-z,;()\\w\\|\"\\s]*[\\]]+(.*?)[.?!]$";		rexBib.add(finalRex);
		
		wantedRexes[0] = 1;	// regexUri
		wantedRexes[1] = 2;	// testReg2
		wantedRexes[2] = 3;	// regexHeader
		wantedRexes[3] = 4;	// regexGK
		wantedRexes[4] = 5;	// regexRef
		wantedRexes[5] = 6;	// regSpecRef
		
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
//										content.add(ape.getAppendings().replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "").replaceAll(regexUri, "").replaceAll(regSpecRef, "").replaceAll("´", "'").replaceAll("`", "'").replaceAll(testReg2, ""));
										content.add(specificRegEx(ape.getAppendings(), rexBib, wantedRexes));
									}
									
									isRex = false;
									
								}else{
									ape.appending(line);
								}
							}
							
						}else{
							
							if(line.indexOf("*") != 0 && line.contains("[[") && line.contains("]]") && line.contains(".")&& !line.contains("[[File:") && !line.contains("[[Image:"))
							{
//								line.replaceAll(regexGK, "").replaceAll(regexRef, "").replaceAll(regexHeader, "").replaceAll(regexUri, "").replaceAll(regSpecRef, "").replaceAll("´", "'").replaceAll("`", "'").replaceAll(testReg2, "");
//								content.add(line);
								content.add(specificRegEx(line, rexBib, wantedRexes));
							}
							
							
						}
				}
			}
			
			for(String str : content)
			{
				Pattern pat = Pattern.compile(finalRex);	
				Matcher m = pat.matcher(str);

				while(m.find())
				{
//					System.out.println(m.group(0));
					firstStepOut.add(m.group(0));
				}
			}
			
		}
		
		end_millis = System.currentTimeMillis() % 1000;
		System.out.println("Processing Time: "+(end_millis-start_millis));
		System.out.println("Content Size: "+firstStepOut.size());
		
		return firstStepOut;
	}
	
	//############################################################################################
	//############################################################################################
	//############################################################################################
	
	/**
	 * This method just wrap a long replacement call
	 * @param input
	 * @param rexBib
	 * @param wantedRexes
	 * @return regEx cleaned String
	 */
	public String specificRegEx(String input, List<String> rexBib, int[] wantedRexes)
	{
		String replacement = "";
		
		for (int i = 0; i < wantedRexes.length; i++) 
		{
//			System.out.println("Rex: "+rexBib.get(wantedRexes[i]-1));
			input.replaceAll(rexBib.get(wantedRexes[i]), replacement);
		}
		
		input.replaceAll("´", "'").replaceAll("`", "'");
		return input;
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
		
		Pattern pat = Pattern.compile(regexStr);	
		Matcher m = pat.matcher(sentence);
		
		//Die Suche nach den Annotations und die Generation der Urls
		while(m.find())
		{
			 String annotation = m.group(1);
			 String[] tmp;
			 correspondingURLs2 = new ArrayList<String>();
			 
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
			 
			 annotedObjects.add(new AnnotObject(sentence, annotation, correspondingURLs2)); 
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
