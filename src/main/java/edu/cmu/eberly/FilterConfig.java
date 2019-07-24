package edu.cmu.eberly;

/**
 * @author vvelsen
 */
public class FilterConfig {
	
	public String inputCharacter = ","; // This is the delimiter character in the input file
	public String replaceCharacter = "_"; // Use this character to repair superfluous characters
  public String outputCharacter = inputCharacter; // Use this character as the delimiter in the output file
  public String repairCharacter = "_"; // Whenever we repair a cell, use this character to replace any 'bad' characters found
}
