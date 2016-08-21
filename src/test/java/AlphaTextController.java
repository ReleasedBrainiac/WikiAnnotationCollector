/**
 * Alphanumeric text control class for text manipulation.
 * If you got special literals here you can convert them.
 * Just add a port of the construct to the code.
 * 
 * Attention: This class is currently not in use!
 * 
 * Inspired by Michael Hoffmann!
 * 
 * @author TTurke
 *
 */
public class AlphaTextController 
{
	private String input;
	
	/**
	 * Constructor keep the input
	 * @param txt
	 */
	public AlphaTextController(String txt)
	{
		this.input = txt;
	}
	
	
	/**
	 * Central Method of the Class
	 * This class convert literals inside the input in a way you like.
	 * @return converted text
	 */
	public String alphaControll()
	{
		char[] chars_in = getInput().toCharArray();
		String output = "";
		
		for (int i = 0; i < chars_in.length; i++) 
		{
			if(Character.isLetterOrDigit(chars_in[i]))
			{
				if("ªàáâãäå".contains(chars_in[i]+""))
				{
					output += "a";
				}else if("èéêë".contains(chars_in[i]+""))
				{
					output += "e";
				}else if("ìíîï".contains(chars_in[i]+""))
				{
					output += "i";
				}else if("òóôõöøº".contains(chars_in[i]+""))
				{
					output += "o";
				}else if("µ".contains(chars_in[i]+""))
				{
					output += "mu";
				}else if("ß".contains(chars_in[i]+""))
				{
					output += "sz";
				}else if("æ".contains(chars_in[i]+""))
				{
					output += "ae";
				}else if("ç".contains(chars_in[i]+""))
				{
					output += "c";
				}else if("ð".contains(chars_in[i]+""))
				{
					output += "d";
				}else if("ñ".contains(chars_in[i]+""))
				{
					output += "n";
				}else if("ý".contains(chars_in[i]+""))
				{
					output += "y";
				}else if("þ".contains(chars_in[i]+""))
				{
					output += "p";
				}else if("-.,\n!?;[]|<>\"%/ ".contains(chars_in[i]+""))
				{
					output += chars_in[i];
				}
			}		
		}
		
		return output;
	}
	
	
	public String getInput()
	{
		return input;
	}
	
	
	public static void main(String[] args) 
	{

	}

}
