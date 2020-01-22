package edu.cmu.eberly.filters.cell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterJSON2XML extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterJSON2XML(FilterConfig aConfig) {
		super (aConfig);
		setName ("json2xml");
	}
	
	/**
	 * @param test
	 * @return
	 */
	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			//System.out.println ("Input is not a JSON object, trying JSON array: " + ex.getMessage());
			// edited, to include @Arthur's comment
			// e.g. in case JSONArray is valid as well...
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				//System.out.println (ex1.getMessage());
				return false;
			}
		}
		
		return true;
	}	
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		if (isJSONValid (raw)==true) {
			System.out.println("Found valid JSON, transforming ...");
		  JSONObject json = new JSONObject(raw);
		  String xml = "<xml>"+XML.toString(json)+"</xml>";
		  debug ("From: " + raw);
		  debug ("To: " + xml);
		  return(xml);
		} 
		
		return raw;
	}
}
