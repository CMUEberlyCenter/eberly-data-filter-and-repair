package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterReplace extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterReplace (FilterConfig aConfig) {
		super (aConfig);
		setName ("replace");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw.replaceAll("_", ",");
	}
}
