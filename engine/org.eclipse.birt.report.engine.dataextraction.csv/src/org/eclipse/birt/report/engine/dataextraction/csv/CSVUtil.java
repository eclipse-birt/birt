/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.dataextraction.csv;

/**
 * Utility class for CSV.
 */
public class CSVUtil {
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String QUOTE = "\""; //$NON-NLS-1$

	/**
	 * Quotes a value in CSV format converter. Here are the rules:
	 * <ol>
	 * <li>Fields with given separator must be delimited with double-quote
	 * characters.</li>
	 * <li>Fields that contain double quote characters must be surrounded by
	 * double-quotes, and the embedded double-quotes must each be represented by a
	 * pair of consecutive double quotes.</li>
	 * <li>A field that contains embedded line-breaks must be surrounded by
	 * double-quotes.</li>
	 * <li>Fields with leading or trailing spaces must be delimited with
	 * double-quote characters.</li>
	 * <li>Null values are represented by empty strings without quotes</li>
	 * 
	 * @param value value to quote
	 * @param sep   CSV separator, to check whether the value contains it
	 * @return the value quoted in CSV format
	 */
	public static String quoteCSVValue(String value, String sep) {
		if (value == null) {
			return null;
		} else if (value.length() == 0) {
			return QUOTE + QUOTE;
		}

		// escape quotes
		value = value.replaceAll(QUOTE, QUOTE + QUOTE);

		boolean needQuote = false;
		needQuote = (value.indexOf(sep) != -1) || (value.indexOf(QUOTE) != -1) || (value.indexOf('\n') != -1) // line
																												// break
				|| value.startsWith(" ") || value.endsWith(" ") //$NON-NLS-1$ //$NON-NLS-2$
				|| value.startsWith("\t") || value.endsWith("\t"); //$NON-NLS-1$ //$NON-NLS-2$
		if (needQuote) {
			value = QUOTE + value + QUOTE;
		}

		return value;
	}

	/**
	 * Creates a row in CSV format from the given values and separator. The returned
	 * row includes the newline character.
	 * 
	 * @param values values of the columns
	 * @param sep    separator to use
	 * @return CSV-formatted row using the given separator and values
	 */
	public static String makeCSVRow(String[] values, String sep, boolean addCR) {
		StringBuffer buf = new StringBuffer(values.length * 10);
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				buf.append(sep);
			}

			String value = CSVUtil.quoteCSVValue(values[i], sep);
			if (value != null) {
				buf.append(value);
			}
		}
		if (addCR) {
			buf.append(CR);
		}
		buf.append(LF);
		return buf.toString();
	}

}
