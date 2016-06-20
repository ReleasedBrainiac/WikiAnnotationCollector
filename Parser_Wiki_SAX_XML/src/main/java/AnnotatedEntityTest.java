package main.java;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * JUnit Test for the Tool. Common Known in Java. Feel free to edit (unused/deprecated)!
 * @author T.Turke
 */
public class AnnotatedEntityTest 
{
	//Test String
	public static String exampleText = "As part of the political turmoil of the 1790s in the "
			+ "wake of the [[French Revolution]], [[William Godwin]] "
			+ "developed the first expression of [[anarchist schools of "
			+ "thought|modern anarchist thought]].";
	
	@Test
	public void blaTest(){
		
		AnnotatedEntity ae = new AnnotatedEntity();
		List<AnnotObject> annotedObjects = new ArrayList<AnnotObject>();
		
		
		ae.getAnnotationsAndUrls(exampleText, annotedObjects);
		
		assertEquals(annotedObjects.get(0).getAnnotedWord(), "French Revolution");
		assertEquals(annotedObjects.get(0).getCorrespondingURLs().get(0), "https://en.wikipedia.org/wiki/French_Revolution");
		assertEquals(annotedObjects.get(1).getAnnotedWord(), "William Godwin");
		assertEquals(annotedObjects.get(1).getCorrespondingURLs().get(0), "https://en.wikipedia.org/wiki/William_Godwin");
		assertEquals(annotedObjects.get(2).getAnnotedWord(), "anarchist schools of thought|modern anarchist thought");
		assertEquals(annotedObjects.get(2).getCorrespondingURLs().get(0), "https://en.wikipedia.org/wiki/anarchist_schools_of_thought");
		assertEquals(annotedObjects.get(2).getCorrespondingURLs().get(1), "https://en.wikipedia.org/wiki/modern_anarchist_thought");

	}
}
