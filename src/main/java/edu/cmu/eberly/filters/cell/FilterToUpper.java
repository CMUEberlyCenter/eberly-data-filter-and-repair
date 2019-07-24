package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterToUpper extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterToUpper (FilterConfig aConfig) {
		super (aConfig);
		setName ("toupper");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw.toUpperCase();
	}
}
