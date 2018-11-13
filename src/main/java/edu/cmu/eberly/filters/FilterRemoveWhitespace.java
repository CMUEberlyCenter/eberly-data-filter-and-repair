package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterRemoveWhitespace extends FilterBase implements DataFilterInterface {

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
