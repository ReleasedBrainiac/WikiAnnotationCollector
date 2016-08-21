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
				if("�������".contains(chars_in[i]+""))
				{
					output += "a";
				}else if("����".contains(chars_in[i]+""))
				{
					output += "e";
				}else if("����".contains(chars_in[i]+""))
				{
					output += "i";
				}else if("�������".contains(chars_in[i]+""))
				{
					output += "o";
				}else if("�".contains(chars_in[i]+""))
				{
					output += "mu";
				}else if("�".contains(chars_in[i]+""))
				{
					output += "sz";
				}else if("�".contains(chars_in[i]+""))
				{
					output += "ae";
				}else if("�".contains(chars_in[i]+""))
				{
					output += "c";
				}else if("�".contains(chars_in[i]+""))
				{
					output += "d";
				}else if("�".contains(chars_in[i]+""))
				{
					output += "n";
				}else if("�".contains(chars_in[i]+""))
				{
					output += "y";
				}else if("�".contains(chars_in[i]+""))
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
