package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public interface DataFilterInterface {
	
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
  public String transform (String raw);
}
