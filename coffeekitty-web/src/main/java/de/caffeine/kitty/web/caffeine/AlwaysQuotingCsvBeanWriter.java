package de.caffeine.kitty.web.caffeine;

import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class AlwaysQuotingCsvBeanWriter extends CsvBeanWriter {
	private final CsvPreference preference;
	
	
	public AlwaysQuotingCsvBeanWriter(Writer writer, CsvPreference preference) {
		super(writer, preference);
		this.preference = preference;
	}


	@Override
	/**
	 * copied from org.supercsv.io.AbstractCsvWriter
	 * @see org.supercsv.io.AbstractCsvWriter
	 */
	protected String escapeString(String csvElement) {
		String escaped = super.escapeString(csvElement);
		
		if (!StringUtils.startsWith(escaped, String.valueOf((char)preference.getQuoteChar()))) {
			return (char)preference.getQuoteChar() + escaped + (char)preference.getQuoteChar()	;
		}
		
		return escaped;
	}
	
}
