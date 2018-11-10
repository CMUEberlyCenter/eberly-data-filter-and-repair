package edu.cmu.eberly.filters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * @author vvelsen
 */
public class FilterJSON2XML extends FilterBase implements DataFilterInterface {

	/**
	 * 
	 */
	public FilterJSON2XML () {
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
			// edited, to include @Arthur's comment
			// e.g. in case JSONArray is valid as well...
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
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
		  JSONObject json = new JSONObject(raw);
		  String xml = "<xml>"+XML.toString(json)+"</xml>";
		  debug ("From: " + raw);
		  debug ("To: " + xml);
		  return(xml);
		} 
		
		return raw;
	}
}
