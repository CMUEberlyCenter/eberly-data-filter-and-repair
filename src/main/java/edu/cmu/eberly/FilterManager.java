package edu.cmu.eberly;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.apache.commons.cli.CommandLine;

import edu.cmu.eberly.filters.cell.CellFilterInterface;
import edu.cmu.eberly.filters.cell.FilterFixXML;
import edu.cmu.eberly.filters.cell.FilterHTMLEncode;
import edu.cmu.eberly.filters.cell.FilterHashcode;
import edu.cmu.eberly.filters.cell.FilterJSON2XML;
import edu.cmu.eberly.filters.cell.FilterRemoveBoundingQuotes;
import edu.cmu.eberly.filters.cell.FilterRemoveNewline;
import edu.cmu.eberly.filters.cell.FilterRemoveWhitespace;
import edu.cmu.eberly.filters.cell.FilterRepair;
import edu.cmu.eberly.filters.cell.FilterToLower;
import edu.cmu.eberly.filters.cell.FilterToUpper;
import edu.cmu.eberly.filters.cell.FilterTrim;
import edu.cmu.eberly.filters.cell.FilterXML2JSON;
import edu.cmu.eberly.filters.row.FilterExportRow;
import edu.cmu.eberly.filters.row.RowFilterInterface;

/**
 * @author vvelsen
 */
public class FilterManager extends RepairTools {
	
	protected Hashtable <String,CellFilterInterface>cellFilterList=new Hashtable<String, CellFilterInterface> ();
	protected Hashtable <String,RowFilterInterface>rowFilterList=new Hashtable<String, RowFilterInterface> ();
	
	protected ArrayList <CellFilterInterface>cellFilters=new ArrayList <CellFilterInterface> ();
	protected ArrayList <RowFilterInterface>rowFilters=new ArrayList <RowFilterInterface> ();

	/**
	 * 
	 */
	public void init () {
		debug ("init ()");
		
		addCellFilter (new FilterJSON2XML (this));
		addCellFilter (new FilterXML2JSON (this));
		addCellFilter (new FilterTrim (this));
		addCellFilter (new FilterToUpper(this));
		addCellFilter (new FilterToLower(this));
		addCellFilter (new FilterRemoveWhitespace(this));
		addCellFilter (new FilterHashcode(this));
		addCellFilter (new FilterRepair (this));
		addCellFilter (new FilterRemoveNewline (this));
		addCellFilter (new FilterHTMLEncode (this));
		addCellFilter (new FilterFixXML (this));
		addCellFilter (new FilterRemoveBoundingQuotes (this));
		
		addRowFilter (new FilterExportRow (this));
	}
	
	/**
	 * 
	 */
	public void showAvailable () {
		debug ("Available filters:");
    Set<String> keys = cellFilterList.keySet();
    for(String key: keys){
      debug("Available filter of "+key);
    }		
	}
	
	/**
	 * 
	 */
	public void showFilters () {
		debug ("Assigned (cell) filters:");
		
		for (int i=0;i<cellFilters.size();i++) {
			CellFilterInterface aFilter=cellFilters.get(i);
			System.out.println("Filter ["+i+"]: " + aFilter.getName());
		}
		
		debug ("Assigned (row) filters:");
		
		for (int i=0;i<rowFilters.size();i++) {
			RowFilterInterface aFilter=rowFilters.get(i);
			System.out.println("Filter ["+i+"]: " + aFilter.getName());
		}		
	}
	
	/**
	 * @param aFilter
	 */
	protected void addCellFilter(CellFilterInterface aFilter) {
		cellFilterList.put(aFilter.getName(),aFilter);
		aFilter.setUseDebugging(useDebugging);
	}
	
	/**
	 * @param aFilter
	 */
	protected void addRowFilter(RowFilterInterface aFilter) {
		//debug (aFilter.getName());
		rowFilterList.put(aFilter.getName(),aFilter);
		aFilter.setUseDebugging(useDebugging);
	}	

	/**
	 * @param cmd 
	 */
	protected void buildCellFilters (String aConfig, CommandLine cmd) {
		debug ("buildCellFilters ("+aConfig+")");
		
		String [] list=aConfig.split("\\|");
		
		// Create a clean list  of filters to be applied and make sure we have at least the
		// repair filter included
		cellFilters=new ArrayList<CellFilterInterface> ();

    for (int i=0;i<list.length;i++) {
    	String filterName=list [i].toLowerCase().trim();
    	debug ("Adding filter with name: " + filterName);
    	CellFilterInterface testFilter=cellFilterList.get(filterName);
    	if (testFilter!=null) {
    		debug ("Filter found, adding to active list ...");
    		testFilter.parseArgs (cmd);
    		cellFilters.add(testFilter);
    	}
    }
	}	
	
	/**
	 * @param cmd 
	 */
	protected void buildRowFilters (String aConfig, CommandLine cmd) {
		debug ("buildRowFilters ("+aConfig+")");
		
		String [] list=aConfig.split("\\|");
		
		// Create a clean list  of filters to be applied and make sure we have at least the
		// repair filter included
		cellFilters=new ArrayList<CellFilterInterface> ();

    for (int i=0;i<list.length;i++) {
    	String filterName=list [i].toLowerCase().trim();
    	debug ("Adding filter with name: " + filterName);
    	RowFilterInterface testFilter=rowFilterList.get(filterName);
    	if (testFilter!=null) {
    		debug ("Filter found, adding to active list ...");
    		testFilter.parseArgs (cmd);
    		rowFilters.add(testFilter);
    	}
    }
	}		
}
