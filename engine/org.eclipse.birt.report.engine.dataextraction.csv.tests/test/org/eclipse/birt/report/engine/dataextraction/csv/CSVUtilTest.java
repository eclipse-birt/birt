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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption;

import junit.framework.TestCase;

public class CSVUtilTest extends TestCase {
	private static final char QUOTE = '"';

	private static final String[] GENERAL_VALUES_INPUT = { null, "", // empty values
			"abc", "123", // regular strings
			"\"abcd\"", "a\"bc\"d", "ab\"cd", // quotes
			"ab\ncd", "ab\r\ncd", "\na\n\nb\r\nc\n", // newlines
			"ab cd", " abcd", "abcd ", // spaces
			"\tabcd", "abcd\t", // tabs
			"abc" };
	private static final String[] GENERAL_VALUES_OUTPUT = { null, "\"\"", // empty values
			"abc", "123", // regular strings
			"\"\"\"abcd\"\"\"", "\"a\"\"bc\"\"d\"", "\"ab\"\"cd\"", // quotes
			"\"ab\ncd\"", "\"ab\r\ncd\"", "\"\na\n\nb\r\nc\n\"", // newlines
			"ab cd", "\" abcd\"", "\"abcd \"", // spaces
			"\"\tabcd\"", "\"abcd\t\"", // tabs
			"abc" };
	private static final String[] GENERAL_ROW_VALUES_OUTPUT = { "", "\"\"", // empty values
			"abc", "123", // regular strings
			"\"\"\"abcd\"\"\"", "\"a\"\"bc\"\"d\"", "\"ab\"\"cd\"", // quotes
			"\"ab\ncd\"", "\"ab\r\ncd\"", "\"\na\n\nb\r\nc\n\"", // newlines
			"ab cd", "\" abcd\"", "\"abcd \"", // spaces
			"\"\tabcd\"", "\"abcd\t\"", // tabs
			"abc" };

	public void testRowSplitWithComma() {
		String sep = ICSVDataExtractionOption.SEPARATOR_COMMA;
		String row = "abc,\"abc\",\"ab\r\nc\",\"ab c\",,\"ab\"\"c\",\"\",\"a,b\tc\"\r\n";
		String[] expected = { "abc", "\"abc\"", "\"ab\r\nc\"", "\"ab c\"", "", "\"ab\"\"c\"", "\"\"", "\"a,b\tc\"" };
		subtestCSVRowSplit(row, expected, sep);
		subtestCSVRowSplit("abc\r\n", new String[] { "abc" }, sep);
		subtestCSVRowSplit("\r\n", new String[] { "" }, sep);
		subtestCSVRowSplit("abc,def\r\n", new String[] { "abc", "def" }, sep);
		subtestCSVRowSplit("abc,\r\n", new String[] { "abc", "" }, sep);
		subtestCSVRowSplit(",abc\r\n", new String[] { "", "abc" }, sep);
	}

	public void testRowSplitWithTabs() {
		String sep = ICSVDataExtractionOption.SEPARATOR_TAB;
		String row = "abc\t\"abc\"\t\"ab\r\nc\"\t\"ab c\"\t\t\"ab\"\"c\"\t\"\"\t\"a,b\tc\"\r\n";
		String[] expected = { "abc", "\"abc\"", "\"ab\r\nc\"", "\"ab c\"", "", "\"ab\"\"c\"", "\"\"", "\"a,b\tc\"" };
		subtestCSVRowSplit(row, expected, sep);
		subtestCSVRowSplit("abc\r\n", new String[] { "abc" }, sep);
		subtestCSVRowSplit("\r\n", new String[] { "" }, sep);
		subtestCSVRowSplit("abc\tdef\r\n", new String[] { "abc", "def" }, sep);
		subtestCSVRowSplit("abc\t\r\n", new String[] { "abc", "" }, sep);
		subtestCSVRowSplit("\tabc\r\n", new String[] { "", "abc" }, sep);
	}

	/**
	 * @param row
	 * @param expected
	 * @param sep
	 */
	private void subtestCSVRowSplit(String row, String[] expected, String sep) {
		String[] values = csvRowSplit(row, sep.charAt(0));
		assertEquals("Extracted values count is correct", expected.length, values.length);
		for (int i = 0; i < values.length; i++) {
			assertEquals("Value with index " + i + " from test case is correct", expected[i], values[i]);
		}
	}

	public void testCommaSeparator() {
		String sep = ICSVDataExtractionOption.SEPARATOR_COMMA;
		subtestQuoteCSVValue(GENERAL_VALUES_INPUT, GENERAL_VALUES_OUTPUT, sep);

		// separator specific
		String[] inputs = { "ab,cd", "1,5", "ab|cd", "ab;cd", "ab\tcd" };
		String[] outputs = { "\"ab,cd\"", "\"1,5\"", "ab|cd", "ab;cd", "ab\tcd" };
		subtestQuoteCSVValue(inputs, outputs, sep);

		subtestMakeCSVRow(GENERAL_VALUES_INPUT, GENERAL_ROW_VALUES_OUTPUT, sep);
		subtestMakeCSVRow(inputs, outputs, sep);
	}

	public void testPipeSeparator() {
		String sep = ICSVDataExtractionOption.SEPARATOR_PIPE;
		subtestQuoteCSVValue(GENERAL_VALUES_INPUT, GENERAL_VALUES_OUTPUT, sep);

		// separator specific
		String[] inputs = { "ab,cd", "ab|cd", "ab;cd", "ab\tcd" };
		String[] outputs = { "ab,cd", "\"ab|cd\"", "ab;cd", "ab\tcd" };
		subtestQuoteCSVValue(inputs, outputs, sep);

		subtestMakeCSVRow(GENERAL_VALUES_INPUT, GENERAL_ROW_VALUES_OUTPUT, sep);
		subtestMakeCSVRow(inputs, outputs, sep);
	}

	public void testSemicolonSeparator() {
		String sep = ICSVDataExtractionOption.SEPARATOR_SEMICOLON;
		subtestQuoteCSVValue(GENERAL_VALUES_INPUT, GENERAL_VALUES_OUTPUT, sep);

		// separator specific
		String[] inputs = { "ab,cd", "ab|cd", "ab;cd", "ab\tcd" };
		String[] outputs = { "ab,cd", "ab|cd", "\"ab;cd\"", "ab\tcd" };
		subtestQuoteCSVValue(inputs, outputs, sep);

		subtestMakeCSVRow(GENERAL_VALUES_INPUT, GENERAL_ROW_VALUES_OUTPUT, sep);
		subtestMakeCSVRow(inputs, outputs, sep);
	}

	public void testTabSeparator() {
		String sep = ICSVDataExtractionOption.SEPARATOR_TAB;
		subtestQuoteCSVValue(GENERAL_VALUES_INPUT, GENERAL_VALUES_OUTPUT, sep);

		// separator specific
		String[] inputs = { "ab,cd", "ab|cd", "ab;cd", "ab\tcd" };
		String[] outputs = { "ab,cd", "ab|cd", "ab;cd", "\"ab\tcd\"" };
		subtestQuoteCSVValue(inputs, outputs, sep);

		subtestMakeCSVRow(GENERAL_VALUES_INPUT, GENERAL_ROW_VALUES_OUTPUT, sep);
		subtestMakeCSVRow(inputs, outputs, sep);
	}

	public void testMakeCSVRowOneColumn() {
		String sep = ICSVDataExtractionOption.SEPARATOR_COMMA;
		String[] values = { "one column only" };
		String row = CSVUtil.makeCSVRow(values, sep, false);
		assertRowValues(row, values, sep);
	}

	public void testMakeCSVRowWithNulls() {
		String sep = ICSVDataExtractionOption.SEPARATOR_COMMA;
		String row = CSVUtil.makeCSVRow(new String[] { null, null, "a", null }, sep, false);
		assertRowValues(row, new String[] { "", "", "a", "" }, sep);
	}

	private void subtestMakeCSVRow(String[] input, String[] output, String sep) {
		String row = CSVUtil.makeCSVRow(input, sep, false);
		assertRowValues(row, output, sep);
	}

	private void subtestQuoteCSVValue(String[] inputs, String[] outputs, String sep) {
		assertEquals("Input and output test case have same size", inputs.length, outputs.length);
		for (int i = 0; i < inputs.length; i++) {
			String input = inputs[i];
			String output = outputs[i];
			String actualOutput = CSVUtil.quoteCSVValue(input, sep);
			assertEquals("Quoting of test value with index " + i + "\" is correct", output, actualOutput);
		}
	}

	protected void assertRowValues(String row, String[] correctValues, String sep) {
		assertNotNull("Returned row is not null", row);
		assertTrue("Row length is >= 2", row.length() >= 2); // because of CRLF chars
		assertEquals("Row has LF at its end", "\n", row.substring(row.length() - 1));
		assertFalse("Row has no CRLF at its end", "\r\n".equals(row.substring(row.length() - 2)));

		String[] rowValues = csvRowSplit(row, sep.charAt(0));
		for (int i = 0; i < correctValues.length; i++) {
			assertEquals("Quoted value from column " + i + " is correct", correctValues[i], rowValues[i]);
		}
	}

	protected String[] csvRowSplit(String row, char sep) {
		// remove trailing CRLF
		if (row.endsWith("\r\n")) {
			row = row.substring(0, row.length() - 2);
		} else if (row.endsWith("\n")) {
			row = row.substring(0, row.length() - 1);

		}

		// limit is necessary to prevent split from trimming the empty values at the end
		// of the row
		// FIXME: split() only works if no values contain the separator string
		// String[] rowValues = row.split(Pattern.quote(sep), correctValues.length);
		List<String> rowValues = new Vector<>();

		StringBuilder aValue = new StringBuilder();
		CharacterIterator i = new StringCharacterIterator(row);
		boolean insideQuote = false;
		for (char c = i.first(); c != CharacterIterator.DONE; c = i.next()) {
			if (c == sep && !insideQuote) {
				// value is finished
				rowValues.add(aValue.toString());
				// clear buffer
				aValue.delete(0, aValue.length());
			} else {
				aValue.append(c);
			}
			// if first or last quote met
			if (c == QUOTE) {
				insideQuote = !insideQuote;
			}
		}
		// add last value
		rowValues.add(aValue.toString());

		return rowValues.toArray(new String[0]);
	}

}
