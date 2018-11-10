package edu.cmu.eberly.filters;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.*;
import org.xml.sax.SAXException;

import edu.cmu.eberly.filters.DataFilterInterface;

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
		Boolean validXML=false;
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder=null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e){}
		
		if (dBuilder!=null) {
			
	    try {
				dBuilder.parse(raw);
				validXML=true;
			} catch (SAXException e) {
			} catch (IOException e) {
			}
	    
	    if (validXML==true) {
			 return (XML.toJSONObject(raw).toString());
	    }
		}
		
		return (raw);
	}
}
