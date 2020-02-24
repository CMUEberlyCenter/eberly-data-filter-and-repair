package edu.cmu.eberly;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 * @author vvelsen
 */
public class ArgTools {

	/**
	 * 
	 */
	public static void debugCommandOptions (CommandLine cmd) {
		
		Option [] options=cmd.getOptions();
		
		for (int i=0;i<options.length;i++) {
			Option testOption=options [i];
			System.out.println ("Option ["+i+"] " + testOption.getArgName() + ": " + testOption.getValue());
		}
	}
	
	/**
	 * 
	 */
	public static void debugCommandArguments (CommandLine cmd) {

	}	
}
