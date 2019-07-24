package edu.cmu.eberly;

import java.io.*;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import edu.cmu.eberly.filters.cell.CellFilterInterface;
import edu.cmu.eberly.filters.row.RowFilterInterface;

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
						
			// Write the header directly to the output file (using the requested delimiter)
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
		
		index++; // Skip header
		indexOriginal++; // Skip header
		
		Boolean repair=false;

		// Process body of data
		while ((st = br.readLine()) != null) {
			if (repair==false) {
			  previous=current;
			}
			
			current=st.split(inputCharacter);
		
			//debug ("Processing (origs): ["+(indexOriginal+1)+"] " + current.length + "\n");
			
			if (current.length==0) {
				warn("Found row with length 0 at row "+indexOriginal+", skipping ...");
			} else {
				// We're back to normal so we should now be able to inspect what's in previous
				if (current.length>=headerLength) {
					repair=false;
					
					// We have more cells than headers, so regardless we need to collapse assuming
					// the collapse happens at the last/right-most cell
					if (current.length>headerLength) {
						String [] temp=new String[headerLength];
						
						for (int i=0;i<headerLength;i++) {
							temp [i]="";
						}
						
						int tempIndex=0;
						int actualIndex=0;

						while (tempIndex<current.length) {
							if (tempIndex<headerLength) {
							  temp [actualIndex]=current [tempIndex];
							  actualIndex++;
							} else {
								String concat=temp [actualIndex-1] + inputCharacter + current [tempIndex];
								temp [actualIndex-1]=concat;
							}
							
							tempIndex++;
						}
						
						current=temp;
					}
					
					//debug ("Processing (fixed): ["+(indexOriginal+1)+"] " + current.length + "\n");
					
					if (previous!=null) {
						// First run a repair over the total row, we might have to collapse a few cells
						if (previous.length>headerLength) {
							// Currently we can only run a repair operation on one target column. We would need
							// to do a scan of the entire file first to do auto-repair operations on a set of
							// columns
							if (targetColumns.size()==1) {
								// Where should the repair take place
								int target=targetColumns.get(0);
								
								// Find the repair window/length
								int difference=previous.length-headerLength;
								
								// Users provide column indices starting at 1
								target--;
								
								debug ("Collapsing " + difference + " cells, starting at: " + target + ", for header length: " + headerLength + ", and previous length: " + previous.length);
								
								// collapsing cells, starting at the target column
								String [] beforeCols=Arrays.copyOfRange(previous,0,target);
								
								debug ("Check, previous length: " + previous.length);
								
								String [] targetCols=Arrays.copyOfRange(previous,target,target+difference);
								String [] afterCols=Arrays.copyOfRange(previous,target+difference,previous.length);
								
								String collapsed=rowToString(targetCols,inputCharacter);
								
								debug ("Collapsed: " + collapsed);
								
								// Add the repaired cell after the before columns effectively replacing itself
								beforeCols[beforeCols.length - 1] = collapsed;
								
								// Finally concatenate the two lists to create the replacement list
								String [] first=concatenate (beforeCols,afterCols);
								
								// Overwrite the original since it's now repaired
								previous=first;
							} else {
								warn("There are more than one target columns provided, we can only run auto-repair functionality on one");
							}
						}
						
						// Then run the list of desired filters over the resulting cells
            if (applyTransforms (previous,indexOriginal)==false) {
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
					}/* else {
						if (headerLength<current.length) {
						  // We have too many cells in this row, which means a bad delimiter generated a bunch too many. 
							// This will be/should be picked up by the repair code above.
						}
					}*/
				}
			}
			
			indexOriginal++;
			
			/*
			if (indexOriginal>54) {
				closeOutput ();	
				br.close();
				System.exit(1);
			}
			*/
		}
		
		// Catch up with ourselves because we only write 'previous' to disk
		if (headerLength==current.length) {
      applyTransforms (current,indexOriginal);
		}
		
		closeOutput ();	
		
		br.close();
		
		for (int i=0;i<cellFilters.size();i++) {
			CellFilterInterface aFilter=cellFilters.get(i);
			aFilter.postProcess(this);
		}
	}

	/**
	 * @param aRow
	 */
	private Boolean applyTransforms(String[] aRow, Long index) {
		
		transformRow (aRow,index);
		
		for (int k=0;k<targetColumns.size();k++) {
			Integer targetColumn=targetColumns.get(k);
			
			// Transform to base 0
			targetColumn--;
			
			if (targetColumn>=0) {								
		    String raw=aRow [targetColumn];
		  
		    String formatted=transformCell(raw);
		  
		    aRow [targetColumn]=formatted;						
			} else {
        return (false);
			}
		}
		
		/*
		for (int k=0;k<aRow.length;k++) {
			if (k<targetColumns.size()-1) {
				
			}
		}
		*/
				
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
	private String transformCell(String raw) {
		for (int i=0;i<cellFilters.size();i++) {
			CellFilterInterface aFilter=cellFilters.get(i);
			raw=aFilter.transform(raw);
		}
				
		return raw;
	}
	
	/**
	 * @param raw
	 * @return
	 */
	private String [] transformRow(String [] raw, Long index) {
		for (int i=0;i<rowFilters.size();i++) {
			RowFilterInterface aFilter=rowFilters.get(i);
			raw=aFilter.transform(raw, index);
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
		options.addOption("s", "salt", false, "Provide a salt string for those filters that encrypt, randomize or hash");
		options.addOption("if", "iformat", true, "Input delimiter character, use t for tab and c for comma. Default is c (comma). Any other character or string will be used as-is");
		options.addOption("of", "oformat", true, "Output delimiter character, use t for tab and c for comma. Default is c (comma). Any other character or string will be used as-is");
		options.addOption("t", "target", true, "Target column to modify, numeric index You can specify a single index, a comma separated list of indices, a range such as 1-4 or a combination");
		options.addOption("pc", "cell_operation (cell)", true, "The operation to perform on a cell, one of: json2xml, xml2json, trim, tolower, toupper, hashcode, removewhitespace, removenewline, htmlencode, fixxml, removeboundingquotes. Separate with | to run multiple filters. Filters are executed left to right as they are specified in this argument");
		options.addOption("pr", "row_operation (row)", true, "The operation to perform on an entire row (tbd). Separate with | to run multiple filters. Filters are executed left to right as they are specified in this argument");
	
		// >-------------------------------------------------------------------------------------
		// Run basic tests

		if (args.length == 0) {
			debug ("No arguments provided");
			return (false);
		}

		CommandLineParser parser = new DefaultParser();

		CommandLine cmd = null;

		// Parse the options passed as command line arguments
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			debug ("Can't parse arguments: " + e.getMessage());
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
		
		debug ("We're fully configured, executing ...");
		
		init();
		
		if (cmd.hasOption("pc")) {
			operation = cmd.getOptionValue("pc", "NOP");
			if (operation.isEmpty() == true) {
				warn("Invalid or missing cell operation");
				return (false);
			}
			
			buildCellFilters (operation,cmd);
		}	
		
		if (cmd.hasOption("pr")) {
			operation = cmd.getOptionValue("pr", "NOP");
			if (operation.isEmpty() == true) {
				warn("Invalid or missing cell operation");
				return (false);
			}
			
			buildRowFilters (operation,cmd);
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
