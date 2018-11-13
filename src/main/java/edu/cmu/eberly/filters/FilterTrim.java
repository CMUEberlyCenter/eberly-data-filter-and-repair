package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterTrim extends FilterBase implements DataFilterInterface {

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
