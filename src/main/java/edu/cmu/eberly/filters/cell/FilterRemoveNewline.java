package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterRemoveNewline extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterRemoveNewline(FilterConfig aConfig) {
		super(aConfig);
		setName("removenewline");
	}

	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw.replaceAll("\\r?\\n", " ");
	}
}
