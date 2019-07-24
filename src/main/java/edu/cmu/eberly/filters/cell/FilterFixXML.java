package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterFixXML extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterFixXML (FilterConfig aConfig) {
		super (aConfig);
		setName ("fixxml");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		//debug ("transform ()");
		
		String fix=raw.replaceAll("\\\\\"", "\"");
		//fix=fix.replaceAll("\\\\", "\"");
		fix=fix.replaceAll(">\"", ">");
				
		//debug ("Fixed: " + fix);
		
		return fix;
	}
}
