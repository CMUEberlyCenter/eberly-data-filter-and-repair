package edu.cmu.eberly.filters;

/**
 * @author vvelsen
 */
public class FilterRepair extends FilterBase implements DataFilterInterface {

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
