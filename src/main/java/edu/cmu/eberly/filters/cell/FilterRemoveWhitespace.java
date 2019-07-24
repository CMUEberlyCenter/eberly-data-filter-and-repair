package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterRemoveWhitespace extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterRemoveWhitespace (FilterConfig aConfig) {
		super (aConfig);
		setName ("removewhitespace");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw.replaceAll("\\s+","");
	}
}
