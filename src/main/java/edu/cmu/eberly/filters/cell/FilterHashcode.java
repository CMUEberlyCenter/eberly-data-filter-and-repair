package edu.cmu.eberly.filters.cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.apache.commons.cli.CommandLine;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

//import edu.cmu.eberly.ArgTools;
import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterHashcode extends CellFilterBase implements CellFilterInterface {

	public static String salt = "";

	private Hashtable<String, String> mapping = new Hashtable<String, String>();

	/**
	 * 
	 */
	public FilterHashcode(FilterConfig aConfig) {
		super(aConfig);
		setName("hashcode");
		FilterHashcode.salt = getRandomString();
	}

	/**
	 * 
	 */
	@Override
	public void parseArgs(CommandLine cmd) {
    debug ("parseArgs ("+cmd.getOptions().length+")");
        
		if (cmd.hasOption("s")) {
			String backupSalt=getRandomString();
			debug ("Backup salt: " + backupSalt);
			FilterHashcode.salt = cmd.getOptionValue("s", backupSalt);
			debug("Using command line provided salt: " + FilterHashcode.salt);
		} else {
			debug("No salt provided, using randomly generated salt: " + FilterHashcode.salt);
		}
	}

	/**
	 * @param aSalt
	 */
	public void setSalt(String aSalt) {
		FilterHashcode.salt = aSalt;
	}

	/**
	 * @return
	 */
	public String getSalt() {
		return (FilterHashcode.salt);
	}

	/**
	 * https://www.baeldung.com/java-random-string
	 */
	private String getRandomString() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 16;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
        int randomLimitedInt = leftLimit + (int) 
          (random.nextFloat() * (rightLimit - leftLimit + 1));
        buffer.append((char) randomLimitedInt);
    }
    String generatedString = buffer.toString();
 
    return (generatedString);
	}

	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		String input=raw + FilterHashcode.salt;
		HashCode hashCode = Hashing.sha256().hashString(input, StandardCharsets.UTF_8);
		String sha256hex = hashCode.toString();

		mapping.put(sha256hex, raw);

		return (sha256hex);
	}

	/**
	 * 
	 */
	@Override
	public void postProcess(FilterConfig aConfig) {
		debug("postProcess ()");

		File file = new File(aConfig.outputFile + ".hashmap.csv");

		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		PrintWriter writer = null;

		try {
			writer = new PrintWriter(file, "UTF-8");
		} catch (FileNotFoundException e) {
			warn("Error opening output file (" + file.getAbsolutePath() + "): " + e.getMessage());
			;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}

		// get keys() from Hashtable and iterate
		Enumeration<String> enumeration = mapping.keys();

		// iterate using enumeration object
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			writer.write(key + "," + mapping.get(key) + "\n");
		}

		writer.close();
	}
}
