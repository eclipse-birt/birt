/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *******************************************************************************/


package org.eclipse.birt.report.data.oda.excel.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.birt.report.data.oda.excel.impl.util.ExcelFileSource;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * Flat file data provider's implementation of the ODA IResultSet interface.
 */

public class ResultSet implements IResultSet {

	public static final int DEFAULT_MAX_ROWS = 1000;
	private static final int CURSOR_INITIAL_VALUE = -1;
	private String[][] sourceData = null;
	private ResultSetMetaData resultSetMetaData = null;
	private int maxRows = 0;
	private int cursor = CURSOR_INITIAL_VALUE;
	private ExcelFileSource excelFileSource;
	// Boolean which marks whether it is successful of last call to getXXX();
	private boolean wasNull = false;
	// a counter that counts the total number of rows read from the excel file
	private int fetchAccumulator = 0;

	private boolean overFlow = false;

	private static ULocale JRE_DEFAULT_LOCALE = ULocale.getDefault();

	private DateFormat dateFormat;
	/** The Excel epoch in milliseconds */
	final static private long EXCEL_EPOCH_MILLIS = -2209161600000L;
	/** The number of milliseconds in a day */
	final static private BigDecimal MILLIS_IN_DAY = new BigDecimal(24 * 60 * 60 * 1000);
	/**
	 * Constructor
	 *
	 * @param excelSource
	 *            flat file data source reader
	 * @param rsmd
	 */
	ResultSet(ExcelFileSource excelSource, ResultSetMetaData rsmd) {
		assert excelSource != null;
		this.excelFileSource = excelSource;
		this.resultSetMetaData = rsmd;
		this.maxRows = this.excelFileSource.getMaxRowsToRead(this.maxRows);
		this.dateFormat = new SimpleDateFormat(
				this.excelFileSource.getDateFormatString());
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException {
		return this.resultSetMetaData;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#close()
	 */
	public void close() throws OdaException {
		this.cursor = 0;
		this.sourceData = null;
		this.resultSetMetaData = null;
		this.excelFileSource.closeFileSource();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws OdaException {
		this.maxRows = max;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#next()
	 */
	public boolean next() throws OdaException {
		if (overFlow) {
			return false;
		}
		// first time to call next
		if (cursor == CURSOR_INITIAL_VALUE) {
			sourceData = this.excelFileSource.getSourceData();
		}

		if ((this.maxRows <= 0 ? false : fetchAccumulator >= this.maxRows)) {
			this.excelFileSource.closeFileSource();
			cursor = CURSOR_INITIAL_VALUE;
			overFlow = true;
			return false;
		}

		if (cursor == this.sourceData.length - 1) {
			sourceData = this.excelFileSource.getSourceData();
			cursor = CURSOR_INITIAL_VALUE;

			if (sourceData.length == 0) {
				this.excelFileSource.closeFileSource();
				overFlow = true;
				return false;
			}
		}

		fetchAccumulator++;
		cursor++;

		return true;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getRow()
	 */
	public int getRow() throws OdaException {
		validateCursorState();
		return this.fetchAccumulator;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(int)
	 */
	public String getString(int index) throws OdaException {
		validateCursorState();
		String result = sourceData[cursor][index - 1];
		this.wasNull = result == null ? true : false;
		return result;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getString(java.lang
	 * .String)
	 */
	public String getString(String columnName) throws OdaException {
		validateCursorState();
		int columnIndex = findColumn(columnName);
		return getString(columnIndex);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(int)
	 */
	public int getInt(int index) throws OdaException {
		return stringToInt(getString(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getInt(java.lang.String
	 * )
	 */
	public int getInt(String columnName) throws OdaException {
		return stringToInt(getString(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(int)
	 */
	public double getDouble(int index) throws OdaException {
		return stringToDouble(getString(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(java.lang
	 * .String)
	 */
	public double getDouble(String columnName) throws OdaException {
		return stringToDouble(getString(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int index) throws OdaException {
		return stringToBigDecimal(getString(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(java.
	 * lang.String)
	 */
	public BigDecimal getBigDecimal(String columnName) throws OdaException {
		return stringToBigDecimal(getString(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(int)
	 */
	public Date getDate(int index) throws OdaException {
		return stringToDate(getString(index));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getDate(java.lang.String
	 * )
	 */
	public Date getDate(String columnName) throws OdaException {
		return stringToDate(getString(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(int)
	 */
	public Time getTime(int index) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getTime(java.lang.String
	 * )
	 */
	public Time getTime(String columnName) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int index) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(java.lang
	 * .String)
	 */
	public Timestamp getTimestamp(String columnName) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(int)
	 */
	public IBlob getBlob(int index) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(java.lang.String
	 * )
	 */
	public IBlob getBlob(String columnName) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(int)
	 */
	public IClob getClob(int index) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getClob(java.lang.String
	 * )
	 */
	public IClob getClob(String columnName) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int index) throws OdaException {
		return stringToBoolean(getString(index)).booleanValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang
	 * .String)
	 */
	public boolean getBoolean(String columnName) throws OdaException {
		return stringToBoolean(getString(columnName)).booleanValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getObject(int)
	 */
	public Object getObject(int index) throws OdaException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#getObject(java.lang
	 * .String)
	 */
	public Object getObject(String columnName) throws OdaException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#wasNull()
	 */
	public boolean wasNull() throws OdaException {
		return this.wasNull;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IResultSet#findColumn(java.lang
	 * .String)
	 */
	public int findColumn(String columnName) throws OdaException {
		return resultSetMetaData.findColumn(columnName);
	}

	/**
	 * Validate whether the cursor has been initialized and at a valid row.
	 *
	 * @throws OdaException
	 *             if the cursor is not initialized
	 */
	private void validateCursorState() throws OdaException {
		if (this.cursor < 0)
			throw new OdaException(
					Messages.getString("resultSet_CURSOR_HAS_NOT_BEEN_INITIALIZED")); //$NON-NLS-1$
	}

	/**
	 * Transform a String value to an int value
	 *
	 * @param stringValue
	 *            String value
	 * @return Corresponding int value
	 */
	private int stringToInt(String stringValue) {
		if (stringValue != null) {
			try {
				return new Integer(stringValue).intValue();
			} catch (NumberFormatException e) {
				try {
					Number number = NumberFormat
					.getInstance(JRE_DEFAULT_LOCALE).parse(stringValue);
					if (number != null) {
						return number.intValue();
					}
				} catch (ParseException e1) {
				}
			}
		}
		this.wasNull = true;
		return 0;
	}

	/**
	 * Transform a String value to a double value
	 *
	 * @param stringValue
	 *            String value
	 * @return Corresponding double value
	 */
	private double stringToDouble(String stringValue) {
		if (stringValue != null) {
			try {
				return new Double(stringValue).doubleValue();
			} catch (NumberFormatException e) {
				try {
					Number number = NumberFormat
					.getInstance(JRE_DEFAULT_LOCALE).parse(stringValue);
					if (number != null) {
						return number.doubleValue();
					}
				} catch (ParseException e1) {
				}
			}
		}
		this.wasNull = true;
		return 0;
	}

	/**
	 * Transform a String value to a big decimal value
	 *
	 * @param stringValue
	 *            String value
	 * @return Corresponding BigDecimal value
	 */
	private BigDecimal stringToBigDecimal(String stringValue) {
		if (stringValue != null) {
			try {
				return new BigDecimal(stringValue);
			} catch (NumberFormatException e) {
				try {
					Number number = NumberFormat
					.getInstance(JRE_DEFAULT_LOCALE).parse(stringValue);
					if (number != null) {
						return new BigDecimal(number.toString());
					}
				} catch (ParseException e1) {
				}
			}
		}
		this.wasNull = true;
		return null;
	}

	/**
	 * Transform a String value to a date value
	 *
	 * @param stringValue
	 *            String value
	 * @return Corresponding date value
	 * @throws OdaException
	 */

	private Date stringToDate(String stringValue) throws OdaException {
		if (stringValue != null && stringValue.length() > 0) {
			try {
				java.util.Date date = dateFormat.parse(stringValue);
				return new Date(date.getTime());
			} catch (ParseException e) {
				try{
					return new Date (excelDateToDate(Double.parseDouble(stringValue)).getTime());


				} catch( Exception ex){

					throw new OdaException(Messages.getFormattedString(
							"invalid_date_value", new String[] { stringValue }));
				}
			}
		}

		this.wasNull = true;
		return null;
	}

	/**
	 * Transform a string to boolean value
	 *
	 * @param stringValue
	 * @return
	 */
	private Boolean stringToBoolean(String stringValue) {
		if (stringValue != null) {
			if (stringValue.equalsIgnoreCase("true")) //$NON-NLS-1$
				return Boolean.TRUE;
			else if (stringValue.equalsIgnoreCase("false")) //$NON-NLS-1$
				return Boolean.FALSE;
			else {
				try {
					if (Integer.parseInt((String) stringValue) == 0)
						return Boolean.FALSE;
					else
						return Boolean.TRUE;
				} catch (NumberFormatException e) {
					try {
						Number number = NumberFormat.getInstance(
								JRE_DEFAULT_LOCALE).parse(stringValue);
						if (number != null) {
							return number.intValue() == 0 ? Boolean.FALSE
									: Boolean.TRUE;
						}
					} catch (ParseException e1) {
					}
				}
			}
		}
		return Boolean.FALSE;
	}


	/**
	 * Creates a Java Date object from an Excel numeric date value
	 * @param number the Excel date to convert
	 * @return the Date object corresponding to the specified numeric Excel date
	 */
	public static java.util.Date excelDateToDate(double number)
	{
		Calendar calendar = excelDateToCalendar(number, null);
		return calendar.getTime();
	}
	/**
	 * Creates an Java Calendar object from an Excel numeric date value
	 * @param number the Excel date to convert
	 * @param tz the time-zone to use, or null for the default time-zone
	 * @return the Calendar object corresponding to the specified numeric date
	 */
	private static Calendar excelDateToCalendar(double number, TimeZone tz)
	{
		if (tz == null)
		{
			tz = TimeZone.getDefault();
		}
		Calendar calendar = Calendar.getInstance(tz);
		long millis = excelDateToMilliseconds(number);
		millis -= calendar.getTimeZone().getOffset(millis);
		calendar.setTimeInMillis(millis);
		//Excel considers 1900 to be a leap year.  Must correct for that.
		final long dec311899 = -2209143600000L; //December 31, 1899
		final long mar011900 = -2203873200000L; //March 1, 1900
		if (millis >= dec311899 && millis < mar011900)
		{
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return calendar;
	}
	/**
	 * @param number the Excel date value to convert
	 * @return the number of milliseconds since the 1970 (Java) epoch
	 */
	private static long excelDateToMilliseconds(double number)
	{
		long millis = (long)(number * MILLIS_IN_DAY.doubleValue());
		millis += EXCEL_EPOCH_MILLIS;
		return millis;
	}




}