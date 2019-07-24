package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterToLower extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterToLower (FilterConfig aConfig) {
		super (aConfig);
		setName ("tolower");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw.toLowerCase();
	}
}
