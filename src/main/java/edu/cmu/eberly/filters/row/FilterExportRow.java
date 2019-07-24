package edu.cmu.eberly.filters.row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.CommandLine;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterExportRow extends RowFilterBase implements RowFilterInterface {

	/**
	 * 
	 */
	public FilterExportRow (FilterConfig aConfig) {
		super (aConfig);
		setName ("export");
	}	
	
	/**
	 * 
	 */	
	@Override
	public String[] transform(String[] raw, Long index) {
	  debug ("Exporting to: " + raw [1] + "-"+raw[0]);
	  
		File file = new File("/tmp/export-"+index+"-"+raw[1]+"-"+raw[0]+".xml");
		try {
			//file.mkdirs();
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return (raw);
		}
		
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(file, "UTF-8");
		} catch (FileNotFoundException e) {
			warn("Error opening output file (" + file.getAbsolutePath() + "): " + e.getMessage());
			return (raw);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return (raw);
		}

	  writer.write(raw [raw.length-1]);
	  
	  writer.close();
		
		return raw;
	}

	/**
	 * 
	 */	
	@Override
	public void parseArgs(CommandLine cmd) {
		// TODO Auto-generated method stub	
	}
}
