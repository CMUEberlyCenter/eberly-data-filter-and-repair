package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterToUpper extends FilterBase implements DataFilterInterface {

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
