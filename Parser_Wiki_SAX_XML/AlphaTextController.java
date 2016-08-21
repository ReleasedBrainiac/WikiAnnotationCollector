import java.util.Arrays;

/**
 * Alphanumeric text control class for text manipulation.
 * If you got special literals here you can convert them.
 * Just add a port of the construct to the code.
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
				if("�������".contains(chars_in[i]))
				{
					output += "a";
				}elseif("����".contains(chars_in[i]))
				{
					output += "e";
				}elseif("����".contains(chars_in[i]))
				{
					output += "i";
				}elseif("�������".contains(chars_in[i]))
				{
					output += "o";
				}elseif("�".contains(chars_in[i]))
				{
					output += "mu";
				}elseif("�".contains(chars_in[i]))
				{
					output += "sz";
				}elseif("�".contains(chars_in[i]))
				{
					output += "ae";
				}elseif("�".contains(chars_in[i]))
				{
					output += "c";
				}elseif("�".contains(chars_in[i]))
				{
					output += "d";
				}elseif("�".contains(chars_in[i]))
				{
					output += "n";
				}elseif("�".contains(chars_in[i]))
				{
					output += "y";
				}elseif("�".contains(chars_in[i]))
				{
					output += "p";
				}elseif("-.,\n!?;[]|<>\"%/ ".contains(chars_in[i]))
				{
					output += chars_in[i];
				}
			}		
		}
	}
	
	
	public String getInput()
	{
		return input;
	}
	
	
	public static void main(String[] args) 
	{

	}

}
