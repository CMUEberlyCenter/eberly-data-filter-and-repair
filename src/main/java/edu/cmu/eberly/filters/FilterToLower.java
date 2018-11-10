package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterToLower extends FilterBase implements DataFilterInterface {

	/**
	 * 
	 */
	public FilterToLower () {
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
