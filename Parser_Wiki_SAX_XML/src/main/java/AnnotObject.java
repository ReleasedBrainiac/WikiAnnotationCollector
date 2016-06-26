package main.java;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple object for storing the depencies between annotations and corresponding urls
 * @author T.Turke
 *
 */
public class AnnotObject
{
	private String annotedWord;
	private String annotedSentence;
	private List<String> correspondingURLs = new ArrayList<String>();
	
	
	public AnnotObject(String annotedSentence)
	{
		this.annotedSentence = annotedSentence;
		this.annotedWord = null;
		this.correspondingURLs = null;
	}
	
	/**
	 * Constructor just keep the given content
	 * @param annotedSentence
	 * @param annotedWord
	 * @param correspondingURLs
	 */
	public AnnotObject(String annotedSentence, String annotedWord, List<String> correspondingURLs)
	{
		this.annotedSentence = annotedSentence;
		this.annotedWord = annotedWord;
		this.correspondingURLs = correspondingURLs;
	}
	
	public String getAnnotedSentence() {
		return annotedSentence;
	}

	public void setAnnotedSentence(String annotedSentence) {
		this.annotedSentence = annotedSentence;
	}

	public String getAnnotedWord() {
		return annotedWord;
	}
	public void setAnnotedWord(String annotedWord) {
		this.annotedWord = annotedWord;
	}
	public List<String> getCorrespondingURLs() {
		return correspondingURLs;
	}
	public void setCorrespondingURLs(List<String> correspondingURLs) {
		this.correspondingURLs = correspondingURLs;
	}
	
	public String toString()
	{
		return "Annotation [["+this.annotedWord+"]] correspond to these Url(s) -> "+this.correspondingURLs;
	}
	
}