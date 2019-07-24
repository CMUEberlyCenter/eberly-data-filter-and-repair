package edu.cmu.eberly.filters.row;

import org.apache.commons.cli.CommandLine;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class RowFilterBase implements RowFilterInterface {

	private String filterName = "base";
	protected FilterConfig config = new FilterConfig (); // Make sure we have something
	protected boolean useDebugging = false;
	
	/**
	 * @param aConfig
	 */
	public RowFilterBase (FilterConfig aConfig) {
		config=aConfig;
	}

	/**
	 * @param aMessage
	 */
	protected void debug(String aMessage) {
		if (useDebugging == true) {
			System.out.println(aMessage);
		}
	}

	/**
	 * @param aMessage
	 */
	protected void warn(String aMessage) {
		System.out.println("Warning: " + aMessage);
	}
	
	/**
	 * 
	 * @param aValue
	 */
	public void setUseDebugging (Boolean aValue) {
		useDebugging=aValue;
	}
	
	/**
	 * 
	 */
	public void setName(String aName) {
		filterName=aName;
	}

	/**
	 * 
	 */
	public String getName() {
		return (filterName);
	}

	/**
	 * 
	 */
	@Override
	public void parseArgs(CommandLine cmd) {		
	}

	@Override
	public String[] transform(String[] raw, Long index) {
		// TODO Auto-generated method stub
		return null;
	}
}
