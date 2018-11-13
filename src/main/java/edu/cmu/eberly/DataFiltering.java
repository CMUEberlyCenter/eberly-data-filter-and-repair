package edu.cmu.eberly;

import java.io.*;
import java.util.Arrays;

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
		
		File input = new File(inputFile);
		
		if (input.exists()==false) {
			warn ("Input file does not exist: " + inputFile);
			return;
		}

		BufferedReader br = new BufferedReader(new FileReader(input));
		
		Long index = 0L;
		Long indexOriginal = 0L;
		int headerLength=0;
		String st="";
		String [] headers = null;
		String [] current=null;
		String [] previous=null;
		
		st = br.readLine();
		
		// Process header
		if (st!=null) {
			headers = st.split(inputCharacter);
			
			headerLength=headers.length;
						
			// Write the header directly to the output file (in the requested format of course)
			if (writeToOutput(rowToString(headers))==false) {
				closeOutput ();	
				br.close();
				return;
			}			
						
			debug ("Evaluating ranges ...");
			
			if (evaluateRanges (headerLength)==false) {		
				closeOutput ();	
				br.close();				
				return;
			}
			
			targetColumns=RangeParser.parseIntRanges(targetColumnString);
			
			if (targetColumns.size()==0) {
				warn("Unable to extract valid column indices");
				closeOutput ();	
				br.close();						
				return;
			}
			
			if (targetColumns.get(targetColumns.size()-1)>headerLength) {
				closeOutput ();	
				br.close();						
				return;
			}
			
			debug ("Valid column ranges round, proceeding ...");
		} else {
			warn("Input file does not seem to contain any data");
			closeOutput ();	
			br.close();				
			return;
		}
		
		index++;
		indexOriginal++;
		
		Boolean repair=false;

		// Process body
		while ((st = br.readLine()) != null) {
			if (repair==false) {
			  previous=current;
			}
			
			current=st.split(inputCharacter);
		
			if (current.length==0) {
				warn("Found row with length 0 at row "+indexOriginal+", skipping ...");
			} else {				
				
				// We're back to normal so we should now be able to inspect what's in previous
				if (headerLength==current.length) {
					repair=false;
					
					if (previous!=null) {
						
						// First run a repair over the total row, we might have to collapse a few cells
						if (previous.length>headerLength) {
							// Currently we can only run a repair operation on one target column. We would need
							// to do a scan of the entire file first to do auto-repair operations on a set of
							// columns
							if (targetColumns.size()==1) {
								int target=targetColumns.get(0);
								int difference=previous.length-headerLength;
								
								target--;
								
								debug ("Collapsing " + difference + " cells, starting at: " + target + ", for header length: " + headerLength + ", and previous length: " + previous.length);
								
								// collapsing cells, starting at the target column
								String [] beforeCols=Arrays.copyOfRange(previous,0,target);
								
								debug ("Check, previous length: " + previous.length);
								
								String [] targetCols=Arrays.copyOfRange(previous,target,target+difference);
								String [] afterCols=Arrays.copyOfRange(previous,target+difference,previous.length);
								
								String collapsed=rowToString(targetCols,inputCharacter);
								
								debug ("Collapsed: " + collapsed);
								
								beforeCols[beforeCols.length - 1] = collapsed;
								
								String [] first=concatenate (beforeCols,afterCols);
								
								previous=first;
							} else {
								warn("There are more than one target columns provided, we can only run auto-repair functionality on one");
							}
						}
						
						// Then run the list of desired filters over the resulting cells
            if (applyTransforms (previous)==false) {
          		closeOutput ();	
          		br.close();		            	
            }
            
					  index++;
					}
				} else {
					// We have less cells than are indicated by the header, this means we assume
					// that this is the result of spurious newlines. In this case we collect the
					// data until we're back into a regular situation and then try to repair
					if (headerLength>current.length) {
						repair=true;
						previous [previous.length-1]=(previous [previous.length-1]+" "+st.trim());
					} else {
						// We have too many cells in this row, which means a bad delimiter generated a bunch too many
						if (headerLength<current.length) {
							
						}
					}
				}
			}
			
			indexOriginal++;
		}
		
		// Catch up with ourselves because we only write 'previous' to disk
		if (headerLength==current.length) {
      applyTransforms (current);
		}
		
		closeOutput ();	
		
		br.close();
	}

	/**
	 * @param aRow
	 */
	private Boolean applyTransforms(String[] aRow) {
		for (int k=0;k<targetColumns.size();k++) {
			Integer targetColumn=targetColumns.get(k);
			
			targetColumn--;
			
			if (targetColumn>=0) {								
		    String raw=aRow [targetColumn];
		  
		    String formatted=transform(raw);
		  
		    aRow [targetColumn]=formatted;						
			} else {
        return (false);
			}
		}
		
    if (writeToOutput(rowToString (aRow))==false) {
  	  return (false);
    }	
    
    return (true);
	}

	/**
	 * @param headerLength
	 * @return
	 */
	private boolean evaluateRanges(int headerLength) {
    if (RangeParser.isValidIntRangeInput(targetColumnString)==false) {
    	warn ("Provided columns are not in a readable format");
    	return (false);
    }
		
		return true;
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
		options.addOption("w", "overwrite", false, "Overwrite existing file if it exists");
		options.addOption("v", "verbose", false, "Show verbose log output");
		options.addOption("if", "iformat", true, "Input delimiter character, use t for tab and c for comma. Default is c (comma). Any other character or string will be used as-is");
		options.addOption("of", "oformat", true, "Output delimiter character, use t for tab and c for comma. Default is c (comma). Any other character or string will be used as-is");
		options.addOption("t", "target", true, "Target column to modify, numeric index You can specify a single index, a comma separated list of indices, a range such as 1-4 or a combination");
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
		
		if (cmd.hasOption("w")) {
			overwriteOut = true;
		}		

		if (cmd.hasOption("t")) {
			targetColumnString=cmd.getOptionValue("t", "");

			if (RangeParser.isValidIntRangeInput(targetColumnString)==false) {
				warn("Invalid or missing target column");
				return (false);
			}
			
			//RangeParser.printIntRanges (targetColumnString);
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

		if (cmd.hasOption("if") == true) {
			if (cmd.getOptionValue("if", "t").equalsIgnoreCase("t") == true) {
				inputCharacter = "\t";
				debug("Set input delimiter character to tab");
			} else {
				if (cmd.getOptionValue("if", "t").equalsIgnoreCase("c") == true) {
					inputCharacter = ",";
					debug("Set input delimiter character to comma");
				} else {
					inputCharacter = cmd.getOptionValue("if", "\t");
				}
			}
		}

		if (cmd.hasOption("of") == true) {
			if (cmd.getOptionValue("of", "t").equalsIgnoreCase("t") == true) {
				outputCharacter = "\t";
				debug("Set output delimiter character to tab");
			} else {
				if (cmd.getOptionValue("of", "t").equalsIgnoreCase("c") == true) {
					outputCharacter = ",";
					debug("Set output delimiter character to comma");
				} else {
					outputCharacter = cmd.getOptionValue("of", "\t");
				}
			}
		}	else {
			// If no output delimiter is specified, use the input one
			outputCharacter=inputCharacter;
		}
		
		inputFile = cmd.getOptionValue("i", "");
		
		// Do this last because we might otherwise have a chicken and egg
		// situation
		
		init();
		
		if (cmd.hasOption("p")) {
			operation = cmd.getOptionValue("p", "NOP");
			if (operation.isEmpty() == true) {
				warn("Invalid or missing cell operation");
				return (false);
			}
			
			buildFilters (operation);
		}		
		
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
				//filter.showFilters ();
				//filter.showAvailable ();
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
