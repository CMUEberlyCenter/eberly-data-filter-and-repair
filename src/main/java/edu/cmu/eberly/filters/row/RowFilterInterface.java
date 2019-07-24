package edu.cmu.eberly.filters.row;

import org.apache.commons.cli.CommandLine;

public interface RowFilterInterface {
	
	/**
	 * 
	 * @param aValue
	 */
	public void setUseDebugging (Boolean aValue);
	
	/**
	 * @param aName
	 */
	public void setName (String aName);
	
	/**ß
	 * @return
	 */
	public String getName ();
	
	/**
	 * @param raw
	 * @return
	 */
  public String [] transform (String [] raw, Long index);

  /**
   * @param cmd
   */
	public void parseArgs(CommandLine cmd);
}
