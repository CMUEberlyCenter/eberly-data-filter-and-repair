package edu.cmu.eberly.filters.cell;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.commons.cli.CommandLine;

import com.google.common.hash.Hashing;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterHashcode extends CellFilterBase implements CellFilterInterface {

	public static String salt="";
		
	/**
	 * 
	 */
	public FilterHashcode (FilterConfig aConfig) {
		super (aConfig);
		setName ("hashcode");
		FilterHashcode.salt=getRandomString ();
	}
	
	/**
	 * 
	 */
	@Override
	public void parseArgs(CommandLine cmd) {		
	
		if (cmd.hasOption("s")) {
			salt=cmd.getOptionValue("s", getRandomString ());
		}
	}	
	
	/**
	 * @param aSalt
	 */
	public void setSalt (String aSalt) {
		FilterHashcode.salt=aSalt;
	}
	
	/**
	 * @return
	 */
	public String getSalt () {
		return (FilterHashcode.salt);
	}
	
	/**
	 * 
	 */
	private String getRandomString() {
    byte[] array = new byte[16];
    new Random().nextBytes(array);
    String generatedString = new String(array, Charset.forName("UTF-8"));
    return(generatedString);
  }
	
	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		/*
		Integer code=raw.hashCode();
		return code.toString();
		*/
		
		String sha256hex = Hashing.sha256().hashString(raw + FilterHashcode.salt, StandardCharsets.UTF_8).toString();
		
		return (sha256hex);
	}
}
