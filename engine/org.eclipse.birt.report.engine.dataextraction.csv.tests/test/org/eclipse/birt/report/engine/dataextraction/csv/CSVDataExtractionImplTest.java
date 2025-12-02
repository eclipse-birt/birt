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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import jakarta.sql.rowset.serial.SerialBlob;
import jakarta.sql.rowset.serial.SerialException;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.dataextraction.CSVDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.csv.mock.MockExtractionResults;

import junit.framework.TestCase;

public class CSVDataExtractionImplTest extends TestCase {
	/**
	 * Test locale used in the test files.
	 */
	private static final String TEST_LOCALE_COUNTRY = "US"; //$NON-NLS-1$
	/**
	 * Test locale used in the test files.
	 */
	private static final String TEST_LOCALE_LANGUAGE = "en"; //$NON-NLS-1$
	/**
	 * Test time zone used in the test files.
	 */
	private static final String TEST_TIME_ZONE = "GMT+1"; //$NON-NLS-1$

	/**
	 * Folder containing the test*.csv files.
	 */
	public static final String ROOT_FOLDER = "test/" //$NON-NLS-1$
			+ CSVDataExtractionImplTest.class.getPackage().getName().replace('.', '/') + "/"; //$NON-NLS-1$

	/**
	 * Date format used for the date format test.
	 */
	private static final String TEST_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"; //$NON-NLS-1$
	private static final String TEST_DATE_FORMAT_WITH_TIMEZONE = "dd/MM/yyyy HH:mm:ss z"; //$NON-NLS-1$

	private static final String ENCODING_ISO = "ISO-8859-1"; //$NON-NLS-1$

	private static final String[] TEST_DATA_COLUMNS = { "stringColumn", //$NON-NLS-1$
			"integerColumn", //$NON-NLS-1$
			"dateColumn", //$NON-NLS-1$
			"decimalColumn" //$NON-NLS-1$
	};
	private static final String[] TEST_SELECT_COLUMNS = { "dateColumn", //$NON-NLS-1$
			"stringColumn", //$NON-NLS-1$
			"integerColumn" //$NON-NLS-1$
	};

	private static final String[] TEST_INVALID_COLUMNS = { "stringColumn", //$NON-NLS-1$
			"invalidColumn", //$NON-NLS-1$
			"integerColumn" //$NON-NLS-1$
	};

	private static final int[] TEST_DATA_TYPES = { DataType.STRING_TYPE, DataType.INTEGER_TYPE, DataType.DATE_TYPE,
			DataType.DECIMAL_TYPE };

	// is be initialized in setUp()
	private Object[][] TEST_DATA = null;

	private static final String[] TEST_DATA_COLUMNS_QUOTING = { "Column 1", //$NON-NLS-1$
			"Column 2" //$NON-NLS-1$
	};

	private static final int[] TEST_DATA_TYPES_QUOTING = { DataType.STRING_TYPE, DataType.STRING_TYPE, };

	private static final Object[][] TEST_DATA_QUOTING = {
			// newlines
			new Object[] { "A string\nwith\nnewlines", //$NON-NLS-1$
					"A second\r\nstring with\r\nnewlines" //$NON-NLS-1$
			}, new Object[] { " Space at the beginning", //$NON-NLS-1$
					"Space at the end " //$NON-NLS-1$
			}, new Object[] { "\tTab at the beginning", //$NON-NLS-1$
					"Tab at the end\t" //$NON-NLS-1$
			}, new Object[] { "\tTab at both sides\t", //$NON-NLS-1$
					"Tab\tin\tthe\tmiddle" //$NON-NLS-1$
			}, new Object[] { " Space at both sides ", //$NON-NLS-1$
					"String,containing,separator,char" //$NON-NLS-1$
			}, new Object[] { "String \"with\" double-quotes", //$NON-NLS-1$
					"String 'with' single quotes" //$NON-NLS-1$
			}, new Object[] { "String|with|pipes", //$NON-NLS-1$
					"String;with;semicolons" //$NON-NLS-1$
			} };

	private ByteArrayOutputStream out;
	private CSVDataExtractionOption option;
	private IExtractionResults results;
	private DateFormat inputDateFormat;

	@Override
	public void setUp() {
		Locale.setDefault(new Locale(TEST_LOCALE_LANGUAGE, TEST_LOCALE_COUNTRY));
		TimeZone.setDefault(TimeZone.getTimeZone(TEST_TIME_ZONE));
		inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		out = new ByteArrayOutputStream();
		option = createOptions();

		TEST_DATA = new Object[][] { new Object[] { "myString1", //$NON-NLS-1$
				Integer.valueOf(5), makeDate("2008-08-08 10:30:00"), //$NON-NLS-1$
				Double.valueOf(25.689) }, new Object[] { null, Integer.valueOf(-12), null, null },
				new Object[] { "my,str;in\tg|3", //$NON-NLS-1$
						null, makeDate("2007-01-01 15:30:00"), //$NON-NLS-1$
						Double.valueOf(-987.654321) },
				// case for testing encoding
				new Object[] { "\u00fc\u4f60\u00df\u00e9", //$NON-NLS-1$
						Integer.valueOf(0), null, Double.valueOf(0.0) } };

		results = new MockExtractionResults(TEST_DATA_COLUMNS, TEST_DATA_TYPES, TEST_DATA);
	}

	@Override
	public void tearDown() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = null;
		option = null;
	}

	public void testOutputDefaults() throws Exception {
		IDataExtractionOption option = new DataExtractionOption();
		option.setOutputFile("test.csv"); //$NON-NLS-1$
		option.setOutputFormat("csv"); //$NON-NLS-1$
		subtestRegular(option, "testDefaults.csv"); //$NON-NLS-1$
	}

	public void testOutputRegular() throws Exception {
		option.setLocale(Locale.FRANCE); // must be ignored because of locale neutral
		option.setLocaleNeutralFormat(true);
		subtestRegular(option, "testRegular.csv"); //$NON-NLS-1$
	}

	public void testOutputLocalized() throws Exception {
		option.setLocale(Locale.FRANCE);
		option.setLocaleNeutralFormat(false);
		subtestRegular(option, "testLocalized.csv"); //$NON-NLS-1$
	}

	public void testOutputEncoding() throws Exception {
		option.setLocale(Locale.ENGLISH);
		option.setLocaleNeutralFormat(true);
		option.setEncoding(ENCODING_ISO);
		CSVDataExtractionImpl extract = createExtraction(out, option);

		// replace test value with chars available in ISO encoding
		Object[][] data = { new Object[] { "\u00e9\u00fc", //$NON-NLS-1$
				Integer.valueOf(0), null, Double.valueOf(0.0) } };

		IExtractionResults results = new MockExtractionResults(TEST_DATA_COLUMNS, TEST_DATA_TYPES, data);
		extract.output(results);

		String testFile = ROOT_FOLDER + "testEncoding.csv"; //$NON-NLS-1$
		assertFileContent(testFile, out.toByteArray());
	}

	public void testOutputDateFormatWithLocaleNeutral() throws Exception {
		option.setLocale(Locale.FRENCH);
		option.setLocaleNeutralFormat(true);
		option.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); //$NON-NLS-1$ // must be ignored because of locale
																	// neutral
		option.setDateFormat(TEST_DATE_FORMAT); // must be ignored because of locale neutral
		subtestDateFormat("testLocaleNeutralDateFormat.csv"); //$NON-NLS-1$
	}

	public void testOutputDateFormatWithLocale() throws Exception {
		option.setLocale(Locale.FRENCH);
		option.setLocaleNeutralFormat(false);
		option.setDateFormat(TEST_DATE_FORMAT);

		// replace test value with chars available in ISO encoding

		subtestDateFormat("testDateFormat.csv"); //$NON-NLS-1$
	}

	public void testOutputDefaultDateFormatWithLocale() throws Exception {
		option.setLocale(Locale.FRENCH);
		option.setLocaleNeutralFormat(false);
		option.setDateFormat(null); // use default format
		subtestDateFormat("testDefaultDateFormat.csv"); //$NON-NLS-1$
	}

	public void testOutputWithTimeZone() throws Exception {
		option.setLocale(Locale.FRENCH);
		option.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); //$NON-NLS-1$
		option.setLocaleNeutralFormat(false);
		option.setDateFormat(null);

		// replace test value with chars available in ISO encoding

		subtestDateFormat("testTimeZone.csv"); //$NON-NLS-1$
	}

	public void testOutputDateFormatWithTimeZone() throws Exception {
		option.setLocale(Locale.FRENCH);
		option.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); //$NON-NLS-1$
		option.setLocaleNeutralFormat(false);
		option.setDateFormat(TEST_DATE_FORMAT_WITH_TIMEZONE);

		// replace test value with chars available in ISO encoding

		subtestDateFormat("testDateFormatWithTimeZone.csv"); //$NON-NLS-1$
	}

	/**
	 * @param fileName
	 * @throws BirtException
	 */
	private void subtestDateFormat(String fileName) throws BirtException {
		// replace test value with chars available in ISO encoding
		String[] dataColumns = { "Any Type Column using DateTime", //$NON-NLS-1$
				"Any Type Column using Date", //$NON-NLS-1$
				"Any Type Column using Time", //$NON-NLS-1$
				"DateTime Column", //$NON-NLS-1$
				"SQL Date Column", //$NON-NLS-1$
				"SQL Time Column" //$NON-NLS-1$
		};

		int[] dataTypes = { DataType.ANY_TYPE, DataType.ANY_TYPE, DataType.ANY_TYPE, DataType.DATE_TYPE,
				DataType.SQL_DATE_TYPE, DataType.SQL_TIME_TYPE };

		Object[][] data = { new Object[] { makeDate("2008-08-08 08:08:08"), //$NON-NLS-1$
				new java.sql.Date(makeDate("2008-08-08 08:08:08").getTime()), //$NON-NLS-1$
				new java.sql.Time(makeDate("2008-08-08 08:08:08").getTime()), //$NON-NLS-1$
				makeDate("2008-08-08 08:08:08"), //$NON-NLS-1$
				new java.sql.Date(makeDate("2008-08-08 08:08:08").getTime()), //$NON-NLS-1$
				new java.sql.Time(makeDate("2008-08-08 08:08:08").getTime()) //$NON-NLS-1$
				} };

		results = new MockExtractionResults(dataColumns, dataTypes, data);

		subtestRegular(option, fileName);
	}

	public void testOutputWithSelectedColumns() throws Exception {
		option.setSelectedColumns(TEST_SELECT_COLUMNS);
		subtestRegular(option, "testSelectColumns.csv"); //$NON-NLS-1$
	}

	/*
	 * Test will ignore the invalid column and output only valid ones without
	 * exception
	 */
	public void testOutputWithInvalidColumn() throws Exception {
		option.setSelectedColumns(TEST_INVALID_COLUMNS);
		subtestRegular(option, "testSelectInvalidColumn.csv"); //$NON-NLS-1$
	}

	public void testOutputQuoting() throws Exception {
		CSVDataExtractionImpl extract = createExtraction(out, option);
		IExtractionResults results = new MockExtractionResults(TEST_DATA_COLUMNS_QUOTING, TEST_DATA_TYPES_QUOTING,
				TEST_DATA_QUOTING);
		extract.output(results);

		String testFile = ROOT_FOLDER + "testQuoting.csv"; //$NON-NLS-1$
		assertFileContent(testFile, out.toByteArray());
	}

	public void testOutputDataTypes() throws Exception {
		option.setLocale(Locale.FRENCH);
		option.setLocaleNeutralFormat(true);
		subtestDataTypes("testDataTypes.csv"); //$NON-NLS-1$
	}

	public void testOutputDataTypesLocalized() throws Exception {
		option.setLocale(Locale.FRENCH);
		option.setLocaleNeutralFormat(false);
		subtestDataTypes("testDataTypesLocalized.csv"); //$NON-NLS-1$
	}

	/**
	 * @throws SerialException
	 * @throws SQLException
	 * @throws BirtException
	 */
	private void subtestDataTypes(String fileName) throws SerialException, SQLException, BirtException {
		boolean isLocaleNeutral = option.isLocaleNeutralFormat();
		final String[] TEST_DATA_TYPES_COLUMNS = { "Any", //$NON-NLS-1$
				"Boolean", //$NON-NLS-1$
				"Integer", //$NON-NLS-1$
				"Double", //$NON-NLS-1$
				"Decimal", //$NON-NLS-1$
				"String", //$NON-NLS-1$
				"Date", //$NON-NLS-1$
				"Blob", //$NON-NLS-1$
				"Binary", //$NON-NLS-1$
				"SQL Date", //$NON-NLS-1$
				"SQL Time" //$NON-NLS-1$
		};

		final int[] TEST_DATA_TYPES_TYPES = { DataType.ANY_TYPE, DataType.BOOLEAN_TYPE, DataType.INTEGER_TYPE,
				DataType.DOUBLE_TYPE, DataType.DECIMAL_TYPE, DataType.STRING_TYPE, DataType.DATE_TYPE,
				DataType.BLOB_TYPE, DataType.BINARY_TYPE, DataType.SQL_DATE_TYPE, DataType.SQL_TIME_TYPE };

		final Object[][] data = { new Object[] { null, null, null, null, null, null, null, null, null, null, null },
				new Object[] { Boolean.TRUE, // any type
						Boolean.TRUE, Integer.valueOf(-24), Double.valueOf(-123.456789),
						// need to use different values because localized BigDecimal produce huge values
						// (no scientific format)
						isLocaleNeutral ? new BigDecimal("123e456789") //$NON-NLS-1$
								: new BigDecimal("12345678901234567890123456789"), //$NON-NLS-1$
						"Simple String", //$NON-NLS-1$
						makeDate("2008-08-08 08:08:08"), //$NON-NLS-1$
						new SerialBlob(new byte[] { 1, 2, 3 }), // blob
						new byte[] { 4, 5, 6 }, // binary
						new java.sql.Date(makeDate("2008-08-08 08:08:08").getTime()), // sql date //$NON-NLS-1$
						new java.sql.Time(makeDate("2008-08-08 08:08:08").getTime())// sql time //$NON-NLS-1$
				}, new Object[] { "Any String", // any type //$NON-NLS-1$
						Boolean.FALSE, Integer.valueOf(28), Double.valueOf(123.456789),
						isLocaleNeutral ? new BigDecimal("123E-456789") //$NON-NLS-1$
								: new BigDecimal("123.45678901234567890123456789"), //$NON-NLS-1$
						null, null, null, null, null, null } };

		results = new MockExtractionResults(TEST_DATA_TYPES_COLUMNS, TEST_DATA_TYPES_TYPES, data);

		subtestRegular(option, fileName);
	}

	public void testOutputWithoutColumnType() throws Exception {
		option.setExportDataType(false);
		subtestRegular(option, "testNoColumnType.csv"); //$NON-NLS-1$
	}

	public void testOutputWithPipeSeparator() throws Exception {
		option.setLocaleNeutralFormat(true);
		option.setSeparator(ICSVDataExtractionOption.SEPARATOR_PIPE);
		subtestRegular(option, "testRegularPipe.csv"); //$NON-NLS-1$
	}

	public void testOutputWithSemicolonSeparator() throws Exception {
		option.setLocaleNeutralFormat(true);
		option.setSeparator(ICSVDataExtractionOption.SEPARATOR_SEMICOLON);
		subtestRegular(option, "testRegularSemicolon.csv"); //$NON-NLS-1$
	}

	public void testOutputWithTabSeparator() throws Exception {
		option.setLocaleNeutralFormat(true);
		option.setSeparator(ICSVDataExtractionOption.SEPARATOR_TAB);
		subtestRegular(option, "testRegularTab.csv"); //$NON-NLS-1$
	}

	/**
	 * @throws BirtException
	 */
	private void subtestRegular(IDataExtractionOption option, String testFile) throws BirtException {
		CSVDataExtractionImpl extract = createExtraction(out, option);
		extract.output(results);
		assertFileContent(ROOT_FOLDER + testFile, out.toByteArray());
	}

	public void testException() {
		// use a dummy extraction results class which throws exceptions
		IExtractionResults faultyResults = new IExtractionResults() {

			@Override
			public void close() {
			}

			@Override
			public IResultMetaData getResultMetaData() throws BirtException {
				throw new BirtException("DummyPluginId", "Test exception", null); //$NON-NLS-1$ //$NON-NLS-2$
			}

			@Override
			public IDataIterator nextResultIterator() throws BirtException {
				throw new BirtException("DummyPluginId", "Test exception", null); //$NON-NLS-1$ //$NON-NLS-2$
			}
		};

		CSVDataExtractionImpl extract = null;
		// test with null output stream
		try {
			extract = createExtraction(null, option);
			fail("Must throw BirtException if passed output stream is null"); //$NON-NLS-1$
		} catch (BirtException e) {
			assertFalse("Exception message is localized", e.getMessage().startsWith("exception.")); //$NON-NLS-1$//$NON-NLS-2$
		}

		try {
			extract = createExtraction(out, option);
		} catch (BirtException e) {
			fail("Exception occured while creating extraction instance: " + e.getMessage()); //$NON-NLS-1$
		}

		try {
			// test extraction with results throwing exception
			extract.output(faultyResults);
		} catch (BirtException e) {
			assertEquals("BirtException contains correct plugin id", CSVDataExtractionImpl.PLUGIN_ID, e.getPluginId()); //$NON-NLS-1$
			assertFalse("Exception message is localized", e.getMessage().startsWith("exception.")); //$NON-NLS-1$//$NON-NLS-2$

			Throwable cause = e.getCause();
			assertNotNull("Exception cause is not null", cause); //$NON-NLS-1$
			assertTrue("Exception cause is of type BirtException", cause instanceof BirtException); //$NON-NLS-1$
			BirtException birtException = (BirtException) cause;
			assertEquals("Cause BirtException contains correct error message", "Test exception", //$NON-NLS-1$ //$NON-NLS-2$
					birtException.getMessage());
			assertEquals("Cause BirtException contains correct plugin id", "DummyPluginId", //$NON-NLS-1$ //$NON-NLS-2$
					birtException.getPluginId());
		} catch (Exception e) {
			fail("output() method must throw BirtException"); //$NON-NLS-1$
		}
	}

	/**
	 * @param option
	 * @return
	 * @throws BirtException
	 */
	private CSVDataExtractionImpl createExtraction(OutputStream out, IDataExtractionOption option)
			throws BirtException {
		CSVDataExtractionImpl extract = new CSVDataExtractionImpl();

		// simulate engine's DataExtractionTaskV1.setupExtractOption() by copying the
		// values to a new object
		// of instance DataExtractionOption
		Map allOptions = new HashMap(option.getOptions());
		DataExtractionOption deOptions = new DataExtractionOption(allOptions);
		deOptions.setOutputStream(out);
		extract.initialize(null, deOptions);
		return extract;
	}

	/**
	 * @return
	 */
	private CSVDataExtractionOption createOptions() {
		CSVDataExtractionOption option = new CSVDataExtractionOption();
		option.setOutputFile("test.csv"); //$NON-NLS-1$
		option.setOutputFormat("csv"); //$NON-NLS-1$

		// option.setDateFormat( null );
		option.setExportDataType(true);
		option.setLocale(Locale.ENGLISH);
		option.setLocaleNeutralFormat(true);

		// option.setEncoding( ENCODING_UTF_8 );
		option.setSelectedColumns(null);
		option.setSeparator(ICSVDataExtractionOption.SEPARATOR_COMMA);
		return option;
	}

	/**
	 * Asserts that the given file contains the given content. The file will be
	 * compared row by row
	 *
	 * @param fileName  file to load
	 * @param byteArray content to compare
	 */
	protected void assertFileContent(String fileName, byte[] byteArray) {
		File file = new File(fileName);
		BufferedReader fileInput = null;
		BufferedReader resultInput = null;
		try {
			int rowIndex = 0;
			fileInput = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			resultInput = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(byteArray)));

			String fileContent = null;
			while ((fileContent = fileInput.readLine()) != null) {
				rowIndex++;
				String resultContent = resultInput.readLine();
				if (resultContent == null) {
					fail("Result content has less lines than test file"); //$NON-NLS-1$
				}
				assertEquals("Row " + rowIndex + " is identical", fileContent, resultContent); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (FileNotFoundException e) {
			fail("Test file \"" + file.getAbsolutePath() + "\" not found"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e) {
			fail("Error occured while reading test file \"" + file.getAbsolutePath() + "\"."); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					fail("Exception while closing test file"); //$NON-NLS-1$
				}
			}
			if (resultInput != null) {
				try {
					resultInput.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private java.util.Date makeDate(String string) {
		try {
			return inputDateFormat.parse(string);
		} catch (ParseException e) {
			fail("Parse exception occurred for date string \"" + string + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
	}

}
