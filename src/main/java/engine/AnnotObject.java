package engine;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple object for storing the depencies between annotations and corresponding urls
 * @author T.Turke
 *
 */
public class AnnotObject
{
	private String annotedSentence;
	private List<String> annotedWords;
	private List<String> correspondingURLs = new ArrayList<String>();
	
	/**
	 * Constructor just keep the given content multiple annotated Words
	 * @param annotedSentence
	 * @param annotedWords
	 * @param correspondingURLs
	 */
	public AnnotObject(String annotedSentence, List<String> annotedWords, List<String> correspondingURLs)
	{
		this.annotedSentence = annotedSentence;
		this.annotedWords = annotedWords;
		this.correspondingURLs = correspondingURLs;
	}
	
	public String getAnnotedSentence() {
		return annotedSentence;
	}
	public void setAnnotedSentence(String annotedSentence) {
		this.annotedSentence = annotedSentence;
	}
	public List<String> getCorrespondingURLs() {
		return correspondingURLs;
	}
	public void setCorrespondingURLs(List<String> correspondingURLs) {
		this.correspondingURLs = correspondingURLs;
	}
	public List<String> getAnnotedWords() {
		return annotedWords;
	}
	public void setAnnotedWords(List<String> annotedWords) {
		this.annotedWords = annotedWords;
	}	
}