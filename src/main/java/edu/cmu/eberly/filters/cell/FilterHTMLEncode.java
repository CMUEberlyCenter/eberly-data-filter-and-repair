package edu.cmu.eberly.filters.cell;

import edu.cmu.eberly.FilterConfig;

/**
 * @author vvelsen
 */
public class FilterHTMLEncode extends CellFilterBase implements CellFilterInterface {

	/**
	 * 
	 */
	public FilterHTMLEncode(FilterConfig aConfig) {
		super(aConfig);
		setName("htmlencode");
	}

	/**
	 * @param s
	 * @return
	 */
	public String escapeHTML(String s) {
		StringBuilder out = new StringBuilder(Math.max(16, s.length()));
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
				out.append("&#");
				out.append((int) c);
				out.append(';');
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * 
	 */
	@Override
	public String transform(String raw) {
		return escapeHTML(raw);
	}
}
