package edu.cmu.eberly.filters.cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.apache.commons.cli.CommandLine;

import com.google.common.hash.Hashing;

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

		if (cmd.hasOption("s")) {
			salt = cmd.getOptionValue("s", getRandomString());
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
	 * 
	 */
	private String getRandomString() {
		byte[] array = new byte[16];
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		return (generatedString);
	}

	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		/*
		 * Integer code=raw.hashCode(); return code.toString();
		 */

		String sha256hex = Hashing.sha256().hashString(raw + FilterHashcode.salt, StandardCharsets.UTF_8).toString();

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
