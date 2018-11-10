package edu.cmu.eberly;

import java.io.*;

import edu.cmu.eberly.filters.DataFilterInterface;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

/**
 * @author vvelsen
 */
public class DataFiltering extends FilterManager {
	
	/**
	 * 
	 */
	public DataFiltering () {
		super();
	}
	
	/**
	 * 
	 * @param args
	 */
	public void run() throws Exception {
		debug("run ()");
		
		// Users specify the column index as a range starting from 1, we need to shift it back one
		targetColumn--;
		
		if (targetColumn<0) {
			warn("Invalid column target index");
			return;
		}

		File input = new File(inputFile);

		BufferedReader br = new BufferedReader(new FileReader(input));
		
		Long index = 0L;
		Long indexOriginal = 0L;
		String st="";
		String[] headers = null;
		int headerLength=0;
		String [] current=null;
		String [] previous=null;
		
		st = br.readLine();
		
		// Process header
		if (st!=null) {
			headers = st.split(splitCharacter);
			
			headerLength=headers.length;
			
			if (targetColumn > headerLength) {
				warn("Target column is greater than available number of columns (out of bounds)");
				closeOutput ();	
				br.close();
				return;
			}
			
			// Write the header directly to the output file
			if (writeToOutput(st)==false) {
				closeOutput ();	
				br.close();
				return;
			}			
			
			debug("Found " + headers.length + " columns, with repair index: " + (targetColumn+1) + " => " + headers [targetColumn]);
		}
		
		index++;
		indexOriginal++;
		
		Boolean repair=false;

		// Process body
		while ((st = br.readLine()) != null) {
			if (repair==false) {
			  previous=current;
			}
			
			current=st.split(splitCharacter);
		
			if (current.length==0) {
				warn("Found row with length 0 at row "+indexOriginal+", skipping");
			} else {
				//debug ("Check, header length " + headerLength + ", row length: " + row.length);
				
				if (headerLength==current.length) {
					repair=false;
					if (previous!=null) {
				    String raw=previous [targetColumn];
				  
				    String formatted=transform(raw);
				  
				    previous [targetColumn]=formatted;
				
				    if (writeToOutput(rowToString (previous))==false) {
						  closeOutput ();	
						  br.close();
				  	  return;
				    }
		
					  index++;
					}
				} else {
					//warn ("Potentially corrupt row: "  +indexOriginal + " attempting repair ...");
					if (headerLength>current.length) {
						repair=true;
						previous [previous.length-1]=(previous [previous.length-1]+" "+st.trim());
					}
				}
			}
			
			
			
			indexOriginal++;
		}
		
		closeOutput ();	
		
		br.close();		
	}

	/**
	 * @param raw
	 * @return
	 */
	private String transform(String raw) {
		for (int i=0;i<filters.size();i++) {
			DataFilterInterface aFilter=filters.get(i);
			raw=aFilter.transform(raw);
		}
				
		return raw;
	}

	/**
	 * @param args
	 */
	public Boolean configure(String[] args) {
		options.addOption("h", "help", true, "Command line help");
		options.addOption("i", "input", true, "Load data from input file");
		options.addOption("o", "output", true, "Write data to output file, or if not provided write to stdout");
		options.addOption("v", "verbose", false, "Show verbose log output");
		options.addOption("f", "format", true, "Input format, use t for tab and c for comma. Default is c. Any other character or string will be used as-is");
		options.addOption("t", "target", true, "Target column to modify, numeric index");
		options.addOption("p", "operation", true, "The operation to perform, one of: json2xml, xml2json, trim, tolower, toupper, hashcode, removewhitespace. Separate with | to run multiple filters. Filters are executed left to right as they are specified in this argument");

		// >-------------------------------------------------------------------------------------
		// Run basic tests

		if (args.length == 0) {
			return (false);
		}

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd = null;

		// Parse the options passed as command line arguments
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			return (false);
		}

		if (cmd.hasOption("h")) {
			return (false);
		}

		if (cmd.hasOption("v")) {
			useDebugging = true;
		}

		if (cmd.hasOption("t")) {
			targetColumn = Integer.parseInt(cmd.getOptionValue("t", "-1"));
			if (targetColumn == -1) {
				warn("Invalid or missing target column");
				return (false);
			}
		}

		if (cmd.hasOption("p")) {
			operation = cmd.getOptionValue("p", "NOP");
			if (operation.isEmpty() == true) {
				warn("Invalid or missing cell operation");
				return (false);
			}
			
			buildFilters (operation);
		}

		if (cmd.hasOption("i") == false) {
			warn("Please provide an input file");
			return (false);
		}

		if (cmd.hasOption("o") == true) {
			debug ("Output file specified, obtaining ...");
			useStOut=false;
			outputFile = cmd.getOptionValue("o", "");
			if (outputFile.isEmpty()==true) {
				outputFile = cmd.getOptionValue("output", "");				
			}
		}

		if (cmd.hasOption("f") == true) {
			if (cmd.getOptionValue("f", "t").equalsIgnoreCase("t") == true) {
				splitCharacter = "\t";
				debug("Set split character to tab");
			} else {
				if (cmd.getOptionValue("f", "t").equalsIgnoreCase("c") == true) {
					splitCharacter = ",";
					debug("Set split character to comma");
				} else {
					splitCharacter = cmd.getOptionValue("f", "\t");
				}
			}
		}

		inputFile = cmd.getOptionValue("i", "");
		
		return (true);
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DataFiltering filter = new DataFiltering();
		if (filter.configure(args)==true) {
			try {
				filter.showFilters ();
				filter.showAvailable ();
				filter.run();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			filter.help();
		}
	}
}
