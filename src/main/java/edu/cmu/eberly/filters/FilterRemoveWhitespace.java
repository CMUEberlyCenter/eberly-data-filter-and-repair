package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterRemoveWhitespace extends FilterBase implements DataFilterInterface {

	/**
	 * 
	 */
	public FilterRemoveWhitespace () {
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
