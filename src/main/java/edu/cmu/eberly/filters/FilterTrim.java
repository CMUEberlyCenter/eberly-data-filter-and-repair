package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterTrim extends FilterBase implements DataFilterInterface {

	/**
	 * 
	 */
	public FilterTrim () {
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
