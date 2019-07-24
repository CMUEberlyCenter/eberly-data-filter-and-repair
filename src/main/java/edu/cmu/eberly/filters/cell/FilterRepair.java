package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterRepair extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterRepair (FilterConfig aConfig) {
		super (aConfig);
		setName ("repair");
	}
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return raw.replaceAll(config.inputCharacter, config.repairCharacter);
	}
}
