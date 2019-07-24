package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterTrim extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterTrim (FilterConfig aConfig) {
		super (aConfig);
		setName ("trim");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw.trim();
	}
}
