package main.java;

import java.util.regex.Pattern;

/**
 * This class is the REX class for multiline depencies which needed to be REXED.
 * @author T.Turke
 *
 */
public class Appendings 
{

	private String appendings = "";
	private String rex = null;
	
	/**
	 * Constructor gather regEx start and end tags
	 * @param rexStart
	 * @param rexEnd
	 */
	public Appendings(String rexStart, String rexEnd)
	{
		this.rex = Pattern.quote(rexStart) + "(.*?)" + Pattern.quote(rexEnd);
	}
	
	/**
	 * Simple appending
	 * @param text
	 */
	public void appending(String text)
	{
			this.appendings += text;
	}
	
	/**
	 * Delete the expression from appending. Could be empty depending on the input!
	 * @return cleaned final text
	 */
	public String getCleanContent()
	{
		return getAppendings().replaceAll(getRex(), "");
	}
	
	
	
	public String getAppendings() {return appendings;}
	
	public void setAppendings(String appendings) {this.appendings = appendings;}

	public String getRex() {return rex;}

	public void setRex(String rex) {this.rex = rex;}
}
