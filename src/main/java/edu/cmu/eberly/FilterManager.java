package edu.cmu.eberly;

import java.util.ArrayList;
import java.util.Hashtable;

import edu.cmu.eberly.filters.DataFilterInterface;
import edu.cmu.eberly.filters.FilterJSON2XML;
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
	public FilterManager () {
		addFilter (new FilterJSON2XML ());
		addFilter (new FilterXML2JSON ());
	}

	/**
	 * 
	 */
	public void showFilters () {
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
		
		filters=new ArrayList<DataFilterInterface> ();

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
