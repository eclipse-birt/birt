/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v2.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-2.0.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - more efficient xlsx processing;
  *         support of timestamp, datetime, time, and date data types
  *    Actuate Corporation - added support of relative file path
  *    Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.ResultSetMetaDataHelper;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

public class ExcelFileSource {
	private String fileExtension;
	private boolean isReaderInitialised;
	private IResultSetMetaData rsmd;
	private ResultSetMetaDataHelper rsmdHelper;
	private int statementMaxRows;
	private String currentTableName;
	private String uriPath;
	private URI uri;
	private boolean hasColumnNames;
	private boolean hasTypeLine;
	private List<String> sheetNameList = new ArrayList<>();
	private ExcelFileReader excelFileReader;
	private int resultSetMaxRows = 0;
	private int maxRowsToRead = 0;
	private int fetchCounter = 0;
	// Max number of rows fetched each time from data source
	public static final int MAX_ROWS_PER_FETCH = 65535;
	private int[] selectColumIndexes;
	private String[] originalColumnNames;
	private boolean isFirstTimeToReadSourceData = true;
	private List<String> nextDataLine;

	// use Object type in case ResourceIdentifiers instance was loaded by a
	// different classloader
	private Object resourceIdentifiers;

	/**
	 * Constructor
	 *
	 * @param connProperties   Connection properties
	 * @param currentTableName The current table name of this connection
	 * @param statementMaxRows The max number of rows specified in the query
	 * @param rsmd             ResultSet meta-data
	 * @param rsmdHelper       ResultSet meta-data helper
	 * @throws OdaException
	 * @throws IOException
	 */
	public ExcelFileSource(Properties connProperties, String currentTableName, String workSheetNames,
			int statementMaxRows, IResultSetMetaData rsmd, ResultSetMetaDataHelper rsmdHelper, Map appContext)
			throws OdaException {
		this.rsmd = rsmd;
		this.rsmdHelper = rsmdHelper;
		this.statementMaxRows = statementMaxRows;
		this.currentTableName = currentTableName;
		this.resourceIdentifiers = appContext != null
				? appContext.get(ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS)
				: null;
		this.fileExtension = ExcelFileReader.getExtensionName(resourceIdentifiers,
				connProperties.getProperty(ExcelODAConstants.CONN_FILE_URI_PROP));
		Properties properties = getCopyOfConnectionProperties(connProperties);
		populateHomeDir(properties);
		populateHasColumnNames(properties);
		populateHasTypeLine(properties);
		populateWorksheetNames(workSheetNames);
	}

	public IResultSetMetaData getRsmd() {
		return rsmd;
	}

	public void setRsmd(IResultSetMetaData rsmd) {
		this.rsmd = rsmd;
	}

	public ResultSetMetaDataHelper getRsmdHelper() {
		return rsmdHelper;
	}

	public void setRsmdHelper(ResultSetMetaDataHelper rsmdHelper) {
		this.rsmdHelper = rsmdHelper;
	}

	public int getStatementMaxRows() {
		return statementMaxRows;
	}

	public void setStatementMaxRows(int statementMaxRows) {
		this.statementMaxRows = statementMaxRows;
	}

	public String getCurrentTableName() {
		return currentTableName;
	}

	public void setCurrentTableName(String currentTableName) {
		this.currentTableName = currentTableName;
	}

	/**
	 *
	 * @param connProperties
	 * @return
	 */
	private Properties getCopyOfConnectionProperties(Properties connProperties) {
		Properties copyConnProperites = new Properties();

		if (connProperties.getProperty(ExcelODAConstants.CONN_FILE_URI_PROP) != null) {
			copyConnProperites.setProperty(ExcelODAConstants.CONN_FILE_URI_PROP,
					connProperties.getProperty(ExcelODAConstants.CONN_FILE_URI_PROP));
		}

		copyConnProperites.setProperty(ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP,
				connProperties.getProperty(ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP));
		copyConnProperites.setProperty(ExcelODAConstants.CONN_INCLTYPELINE_PROP,
				connProperties.getProperty(ExcelODAConstants.CONN_INCLTYPELINE_PROP));

		return copyConnProperites;
	}

	/**
	 *
	 * @param connProperties
	 * @throws OdaException
	 */
	private void populateHomeDir(Properties connProperties) throws OdaException {
		this.uriPath = connProperties.getProperty(ExcelODAConstants.CONN_FILE_URI_PROP);
		uri = ResourceLocatorUtil.resolvePath(resourceIdentifiers, uriPath);

		if (uri == null) {
			throw new OdaException(Messages.getFormattedString("fileSource_excelFileNotFound", //$NON-NLS-1$
					new Object[] { uriPath }));
		}

		try {
			ResourceLocatorUtil.validateFileURI(uri);
		} catch (Exception e) {
			throw new OdaException(Messages.getFormattedString("fileSource_excelFileNotFound", //$NON-NLS-1$
					new Object[] { uriPath }));
		}
	}

	/**
	 *
	 * @param connProperties
	 */
	private void populateHasColumnNames(Properties connProperties) {
		this.hasColumnNames = connProperties.getProperty(ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP)
				.equalsIgnoreCase(ExcelODAConstants.INC_COLUMN_NAME_NO) ? false : true;
	}

	/**
	 *
	 * @param connProperties
	 */
	private void populateHasTypeLine(Properties connProperties) {
		this.hasTypeLine = connProperties.getProperty(ExcelODAConstants.CONN_INCLTYPELINE_PROP)
				.equalsIgnoreCase(ExcelODAConstants.INC_TYPE_LINE_NO) ? false : true;
	}

	/**
	 *
	 * @param connProperties
	 * @throws OdaException
	 */
	private void populateWorksheetNames(String workSheetNames) throws OdaException {
		if (workSheetNames == null) {
			throw new OdaException(Messages.getString("query_WORKSHEET_CANNOT_BE_NULL") //$NON-NLS-1$
			);
		}
		String[] workSheetNamesArray = workSheetNames.split(ExcelODAConstants.DELIMITER_SEMICOLON_VALUE);
		for (int index = 0; index < workSheetNamesArray.length; index++) {
			sheetNameList.add(workSheetNamesArray[index]);
		}
	}

	/**
	 *
	 * @param connProperties
	 * @param tableName
	 * @return
	 * @throws OdaException
	 */
	public int getColumnCount() throws OdaException {
		// this.currentTableName = tableName;

		int count;
		try {
			initialiseReader();
			List<String> columnLine;
			while (isEmptyRow(columnLine = this.excelFileReader.readLine())) {
				continue;
			}
			count = columnLine.size();
		} catch (IOException e) {
			throw new OdaException(Messages.getString("query_IO_EXCEPTION") //$NON-NLS-1$
					+ findDataFileAbsolutePath());
		}

		return count;
	}

	/**
	 * Find the absolute path of the file in which a specific table resides.
	 *
	 * @param tableName the name of table
	 * @return the String which contains the absolute path of that table
	 * @throws OdaException if the table name cannot be found
	 */
	public String findDataFileAbsolutePath() throws OdaException {
		if (uri == null) {
			throw new OdaException(Messages.getFormattedString("fileSource_excelFileNotFound", //$NON-NLS-1$
					new Object[] { uriPath }));
		}
		try {
			ResourceLocatorUtil.validateFileURI(uri);
		} catch (Exception e) {
			throw new OdaException(Messages.getString("query_invalidTableName") //$NON-NLS-1$
					+ this.uriPath);
		}

		return uri.toString();

	}

	/**
	 *
	 * @return
	 * @throws OdaException
	 */
	public String[][] getSourceData() throws OdaException {
		try {
			initialiseReader();
			List<String[]> v = fetchQueriedDataFromFileToList();
			return copyDataFromListToTwoDimensionArray(v);
		} catch (IOException e) {
			throw new OdaException(e);
		} finally {
			try {
				excelFileReader.close();
			} catch (IOException e) {
				//
			}
		}
	}

	/**
	 *
	 * @return
	 * @throws OdaException
	 */
	private List<String[]> fetchQueriedDataFromFileToList() throws OdaException {
		List<String[]> result = new ArrayList<>();
		try {
			if (isFirstTimeToReadSourceData) {
				excelFileReader.setCurrentRowIndex(0);
				// make a copy of column names if there are
				if (this.hasColumnNames) {
					List<String> columeNameLine;
					while (isEmptyRow(columeNameLine = excelFileReader.readLine())) {
						continue;
					}
					this.originalColumnNames = getColumnNameArray(columeNameLine);
					initNameIndexMap();
				}

				// skip Type information. The type information is in the second
				// line
				// of file
				if (this.hasTypeLine) {
					while (isEmptyRow(excelFileReader.readLine())) {
						continue;
					}
				}

				if (!this.hasColumnNames) {
					while (isEmptyRow(nextDataLine = excelFileReader.readLine())) {
						continue;
					}
					this.originalColumnNames = createTempColumnNames(nextDataLine);
					initNameIndexMap();
				} else {
					nextDataLine = excelFileReader.readLine();
				}
				excelFileReader.setMaxColumnIndex(originalColumnNames.length);
				isFirstTimeToReadSourceData = false;
			}

			// temporary variable which is used to store the data of a row
			// fetched from a the file

			int counterLimitPerFetch = fetchCounter + MAX_ROWS_PER_FETCH;

			while ((this.maxRowsToRead <= 0 ? true : this.fetchCounter < this.maxRowsToRead)
					&& this.fetchCounter < counterLimitPerFetch && nextDataLine != null) {
				if (!isEmptyRow(nextDataLine)) {
					fetchCounter++;
					result.add(fetchQueriedDataFromRow(nextDataLine));
				}
				nextDataLine = excelFileReader.readLine();
			}

			return result;
		} catch (IOException e) {
			throw new OdaException(e.getMessage());
		}
	}

	/**
	 * Feed the row data from a List to a two-dimension array. The string value is
	 * trimmed before being copied into array.
	 *
	 * @param v
	 * @return a String two dimension array with each horizontal array contains a
	 *         row
	 * @throws OdaException
	 */
	private String[][] copyDataFromListToTwoDimensionArray(List<String[]> v) throws OdaException {
		String[][] rowSet = new String[v.size()][this.rsmd.getColumnCount()];
		for (int i = 0; i < v.size(); i++) {
			String[] temp = (String[]) v.get(i);
			for (int j = 0; j < temp.length; j++) {
				if (temp[j] != null) {
					rowSet[i][j] = temp[j].trim();
				} else {
					throw new OdaException(Messages.getString("data_read_error")); //$NON-NLS-1$
				}
			}
		}
		return rowSet;
	}

	private void initNameIndexMap() throws OdaException {
		assert originalColumnNames != null;
		HashMap<String, Integer> originalColumnNameIndexMap = new HashMap<>();
		for (int i = 0; i < originalColumnNames.length; i++) {
			originalColumnNameIndexMap.put(originalColumnNames[i].trim().toUpperCase(), Integer.valueOf(i));
		}
		selectColumIndexes = new int[rsmd.getColumnCount()];

		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			selectColumIndexes[i] = findIndex(rsmdHelper.getOriginalColumnName(rsmd.getColumnName(i + 1)),
					originalColumnNameIndexMap);
		}
	}

	/**
	 *
	 * @param columnCount
	 * @return
	 * @throws OdaException
	 */
	private String[] createTempColumnNames(List<String> aRow) throws OdaException {
		String[] tempColumnNames = new String[aRow.size()];

		for (int i = 0; i < aRow.size(); i++) {
			tempColumnNames[i] = formatTempColumnName(i + 1);
		}

		return tempColumnNames;
	}

	private static String formatTempColumnName(int columnPosition) {
		return "COLUMN_" + columnPosition; //$NON-NLS-1$
	}

	/**
	 * Fetch data from a row.
	 *
	 * @param aRow a row read from table
	 * @return an array of data values for each specified column names from a row.
	 *         The "specified column names" are obtained from meta data
	 * @throws OdaException
	 */
	private String[] fetchQueriedDataFromRow(List<String> aRow) throws OdaException {
		String[] sArray = new String[rsmd.getColumnCount()];
		for (int i = 0; i < sArray.length; i++) {
			int location = selectColumIndexes[i];
			if (location != -1) {
				if (location >= aRow.size()) {
					throw new OdaException(Messages.getString("query_INVALID_EXCEL_FILE")); //$NON-NLS-1$
				} else {
					sArray[i] = aRow.get(location).toString();
				}
			}
		}
		return sArray;
	}

	/**
	 * Return the 0-based position of a value in the given array
	 *
	 * @param value
	 * @param array
	 * @return
	 */
	private int findIndex(String value, HashMap<String, Integer> originalColumnNameIndexMap) {
		Integer index = originalColumnNameIndexMap.get(value.trim().toUpperCase());
		if (index == null) {
			return -1;
		} else {
			return index.intValue();
		}
	}

	public List<String> readLine() throws OdaException, IOException {
		if (!isReaderInitialised) {
			initialiseReader();
			isReaderInitialised = true;
		}

		return excelFileReader.readLine();
	}

	public void resetRowCounter() {

		if (this.excelFileReader != null) {
			this.excelFileReader.setCurrentRowIndex(0);
		}
	}

	/**
	 *
	 * @throws OdaException
	 * @throws IOException
	 */
	private void initialiseReader() throws OdaException, IOException {

		if (isReaderInitialised) {
			return;
		}
		this.fileExtension = ExcelFileReader.getExtensionName(uri);
		this.excelFileReader = new ExcelFileReader(ResourceLocatorUtil.getURIStream(uri), this.fileExtension,
				this.sheetNameList, this.statementMaxRows);
		isReaderInitialised = true;

	}

	/**
	 * Extract the column name from the line into the format of string array
	 *
	 * @param line
	 * @param isFirstLine
	 * @return
	 * @throws OdaException
	 */
	public String[] getColumnNameArray(List<?> line) throws OdaException {
		if (line == null) {
			throw new OdaException(Messages.getString("common_CANNOT_FIND_COLUMN")); //$NON-NLS-1$
		}
		return getStringArrayFromList(line);
	}

	/**
	 * Put the contants of the list into a string array
	 *
	 * @param list
	 * @return
	 */
	public static String[] getStringArrayFromList(List<?> list) {
		String[] array = null;
		if (list != null) {
			array = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				String columnName = (String) list.get(i);
				if (columnName == null || columnName.isEmpty()) {
					columnName = formatTempColumnName(i + 1);
				}
				array[i] = columnName;
			}
		}
		return array;
	}

	/**
	 * See if this row is empty or not
	 *
	 * @param row
	 * @return
	 * @throws OdaException
	 */
	public boolean isEmptyRow(List<String> line) throws OdaException {
		if (line == null) {
			throw new OdaException(Messages.getString("query_INVALID_EXCEL_FILE")); //$NON-NLS-1$
		}

		return line.isEmpty() || (line.size() == 1 && line.get(0).equals("")); //$NON-NLS-1$
	}

	public void close() throws OdaException {
		try {
			if (isReaderInitialised) {
				excelFileReader.close();
			}
		} catch (IOException e) {
		}
	}

	public int getMaxRows() throws OdaException {
		try {
			if (!isReaderInitialised) {
				initialiseReader();
			}
			return excelFileReader.getMaxRows();
		} catch (IOException e) {
			close();
			throw new OdaException(e);
		}
	}

	/**
	 *
	 * @param resultSetMaxRows
	 * @return
	 */
	public int getMaxRowsToRead(int resultSetMaxRows) {
		this.resultSetMaxRows = resultSetMaxRows;
		return this.maxRowsToRead = ((this.statementMaxRows != 0 && this.statementMaxRows < this.resultSetMaxRows)
				|| this.resultSetMaxRows == 0) ? this.statementMaxRows : this.resultSetMaxRows;
	}

	/**
	 *
	 *
	 */
	public void closeFileSource() {
		try {
			if (this.excelFileReader != null) {
				this.excelFileReader.close();
			}
		} catch (IOException e) {
		}
		this.excelFileReader = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#finalize()
	 */
	@Override
	public void finalize() {
		this.closeFileSource();
	}
}
