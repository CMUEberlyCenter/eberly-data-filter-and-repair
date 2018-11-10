package edu.cmu.eberly;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author vvelsen
 */
public class RepairTools {

	protected boolean useDebugging = false;
	protected Options options = new Options();

	protected Boolean useStOut=true;
	protected PrintWriter writer = null;
	
	protected String splitCharacter = ",";

	protected int targetColumn = -1;

	protected String operation = "NOP";	
	
	protected String inputFile = "";
	protected String outputFile = "";	
	
	/**
	 * @param aMessage
	 */
	protected void debug(String aMessage) {
		if (useDebugging == true) {
			System.out.println(aMessage);
		}
	}

	/**
	 * @param aMessage
	 */
	protected void warn(String aMessage) {
		System.out.println("Warning: " + aMessage);
	}

	/**
	 * 
	 */
	public void help() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("DataFiltering", options);
	}

	/**
	 * @param row
	 * @return
	 */
	protected String rowToString(String[] row) {
		StringBuffer formatter = new StringBuffer();

		for (int i = 0; i < row.length; i++) {
			if (i != 0) {
				formatter.append(splitCharacter);
			}

			formatter.append(row[i]);
		}

		return formatter.toString();
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
	protected void closeOutput () {
		if (writer!=null) {
			writer.close();
		}
	}
	
	/**
	 * 
	 * @param aLine
	 */
	protected Boolean writeToOutput (String aLine) {
		if (useStOut==false) {
		  if (writer==null) {
			  try {
					writer = new PrintWriter(outputFile, "UTF-8");
				} catch (FileNotFoundException e) {
          warn ("Error opening output file ("+outputFile+"): " + e.getMessage());
					return (false);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return (false);
				}
		  }
		  
		  writer.write(aLine+"\n");
		  
		  return (true);
		}
		
		System.out.println(aLine);
		
		return (true);
	}
}