package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterRemoveBoundingQuotes extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterRemoveBoundingQuotes (FilterConfig aConfig) {
		super (aConfig);
		setName ("removeboundingquotes");
	}
	
	/**
	 * Here is the regex broken down: ^\"|\"$. | means "or". It will thus match either ^\" or \"$. ^ matches start 
	 * of string and $ matches end of string. ^\" means match a quote at the start of the string and \"$ matches a 
	 * quote at the end of the string.
	 */
	@Override
	public String transform(String raw) {
		return raw.replaceAll("^\"|\"$", "");
	}
}
