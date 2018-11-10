package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterXML2JSON extends FilterBase implements DataFilterInterface {

	/**
	 * 
	 */
	public FilterXML2JSON () {
		setName ("xml2json");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw;
	}
}
