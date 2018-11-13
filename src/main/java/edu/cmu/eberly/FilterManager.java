package edu.cmu.eberly;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import edu.cmu.eberly.filters.DataFilterInterface;
import edu.cmu.eberly.filters.FilterHashcode;
import edu.cmu.eberly.filters.FilterJSON2XML;
import edu.cmu.eberly.filters.FilterRemoveWhitespace;
import edu.cmu.eberly.filters.FilterRepair;
import edu.cmu.eberly.filters.FilterToLower;
import edu.cmu.eberly.filters.FilterToUpper;
import edu.cmu.eberly.filters.FilterTrim;
import edu.cmu.eberly.filters.FilterXML2JSON;

/**
 * @author vvelsen
 */
public class FilterManager extends RepairTools {
	
	protected Hashtable <String,DataFilterInterface>filterList=new Hashtable<String, DataFilterInterface> ();
	protected ArrayList <DataFilterInterface>filters=new ArrayList <DataFilterInterface> ();

	/**
	 * 
	 */
	public void init () {
		addFilter (new FilterJSON2XML (this));
		addFilter (new FilterXML2JSON (this));
		addFilter (new FilterTrim (this));
		addFilter (new FilterToUpper(this));
		addFilter (new FilterToLower(this));
		addFilter (new FilterRemoveWhitespace(this));
		addFilter (new FilterHashcode(this));
		addFilter (new FilterRepair (this));
	}
	
	/**
	 * 
	 */
	public void showAvailable () {
		debug ("Available filters:");
    Set<String> keys = filterList.keySet();
    for(String key: keys){
      debug("Available filter of "+key);
    }		
	}
	
	/**
	 * 
	 */
	public void showFilters () {
		debug ("Assigned filters:");
		for (int i=0;i<filters.size();i++) {
			DataFilterInterface aFilter=filters.get(i);
			System.out.println("Filter ["+i+"]: " + aFilter.getName());
		}
	}
	
	/**
	 * @param filterJSON2XML
	 */
	protected void addFilter(DataFilterInterface aFilter) {
		filterList.put(aFilter.getName(),aFilter);
		aFilter.setUseDebugging(useDebugging);
	}

	/**
	 * 
	 */
	protected void buildFilters (String aConfig) {
		debug ("buildFilters ("+aConfig+")");
		
		String [] list=aConfig.split("\\|");
		
		// Create a clean list  of filters to be applied and make sure we have at least the
		// repair filter included
		filters=new ArrayList<DataFilterInterface> ();
		//filters.add(filterList.get("repair"));

    for (int i=0;i<list.length;i++) {
    	String filterName=list [i].toLowerCase();
    	debug ("Adding filter with name: " + filterName);
    	DataFilterInterface testFilter=filterList.get(filterName);
    	if (testFilter!=null) {
    		debug ("Filter found, adding to active list ...");
    		filters.add(testFilter);
    	}
    }
	}	
}
