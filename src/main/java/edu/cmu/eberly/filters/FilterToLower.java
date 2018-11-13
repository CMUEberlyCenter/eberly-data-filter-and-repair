package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterToLower extends FilterBase implements DataFilterInterface {

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
