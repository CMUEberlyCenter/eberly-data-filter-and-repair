package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterToUpper extends FilterBase implements DataFilterInterface {

	/**
	 * 
	 */
	public FilterToUpper () {
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
