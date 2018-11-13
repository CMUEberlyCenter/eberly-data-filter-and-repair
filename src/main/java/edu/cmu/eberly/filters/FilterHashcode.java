package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterHashcode extends FilterBase implements DataFilterInterface {

	/**
	 * 
	 */
	public FilterHashcode (FilterConfig aConfig) {
		super (aConfig);
		setName ("hashcode");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		Integer code=raw.hashCode();
		return code.toString();
	}
}
