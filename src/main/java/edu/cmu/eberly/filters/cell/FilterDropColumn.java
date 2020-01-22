package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterDropColumn extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterDropColumn (FilterConfig aConfig) {
		super (aConfig);
		setName ("drop");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return " ";
	}
}

