/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - more efficient xlsx processing;
  *         support of timestamp, datetime, time, and date data types
  *    Actuate Corporation - added support of relative file path
  *    Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.ResultSetMetaDataHelper;
import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.birt.report.data.oda.excel.impl.util.DataTypes;
import org.eclipse.birt.report.data.oda.excel.impl.util.ExcelFileSource;
import org.eclipse.birt.report.data.oda.excel.impl.util.querytextutil.QueryTextUtil;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * Implementation class of IQuery for the Excel ODA runtime driver.
 */
public class ExcelFileQuery implements IQuery {
	public static final int DEFAULT_MAX_ROWS_TO_READ = 0;

	private int maxRows;
	private int maxRowsToRead = DEFAULT_MAX_ROWS_TO_READ;
	private ExcelFileSource masterExcelFileSource = null;

	private static final String NAME_LITERAL = "NAME"; //$NON-NLS-1$
	private static final String TYPE_LITERAL = "TYPE"; //$NON-NLS-1$

	// whether the Excel file has the column names
	private boolean hasColumnNames;

	// Whether to use 2nd line as Type line
	private boolean hasTypeLine;

	// The table that the query operates on
	private String currentTableName;

	// the properties of the connection
	private Properties connProperties;

	// The meta data of the query's result set.
	// It is available only after a query is prepared.
	private ResultSetMetaData resultSetMetaData;

	private ResultSetMetaDataHelper resultSetMetaDataHelper;

	private String worksheetNames;

	private String preparedColumnNames;

	private String[] columnLabels;

	private String colInfo;

	private boolean isInvalidQuery;

	private Map appContext = null;

	public ExcelFileQuery(Properties connProperties) throws OdaException {
		if (connProperties == null || connProperties.getProperty(ExcelODAConstants.CONN_FILE_URI_PROP) == null)
			throw new OdaException(Messages.getString("common_ARGUMENT_CANNOT_BE_NULL")); //$NON-NLS-1$

		this.connProperties = connProperties;

		extractsHasColumnNamesInfo();
		extractsHasColumnTypeLineInfo();
	}

	/**
	 *
	 */
	private void extractsHasColumnNamesInfo() {
		this.hasColumnNames = connProperties.getProperty(ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP)
				.equalsIgnoreCase(ExcelODAConstants.INC_COLUMN_NAME_NO) ? false : true;
	}

	/**
	 *
	 */
	private void extractsHasColumnTypeLineInfo() {
		this.hasTypeLine = connProperties.getProperty(ExcelODAConstants.CONN_INCLTYPELINE_PROP)
				.equalsIgnoreCase(ExcelODAConstants.INC_TYPE_LINE_NO) ? false : true;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	/**
	 * Prepare the meta data which will be used in execution of a query text. It
	 * sets the value of two member variables: resultSetMetaData and
	 * absoluteFileName
	 */
	public void prepare(String queryText) throws OdaException {
		if (queryText == null) {
			throw new OdaException(Messages.getString("common_NULL_QUERY_TEXT")); //$NON-NLS-1$
		}

		QueryTextUtil qtu = new QueryTextUtil(queryText);
		String query = formatQueryText(qtu.getQuery());
		validateNonEmptyQueryText(query);
		String[] queryFragments = parsePreparedQueryText(query);
		validateSingleTableQuery(queryFragments);

		// the name of table against which the query will be executed
		this.currentTableName = getPreparedTableNames(queryFragments);
		this.preparedColumnNames = getPreparedColumnNames(queryFragments);
		this.columnLabels = getColumnLabels(queryFragments);

		colInfo = qtu.getColumnsInfo();
	}

	/**
	 * Format the given query text. Eliminates redundant spaces and convert all
	 * keywords to uppercase.
	 *
	 * @param queryText
	 * @return
	 */
	private String formatQueryText(String queryText) {
		StringBuffer result = new StringBuffer();
		String[] temp = queryText.trim().split(ExcelODAConstants.DELIMITER_SPACE);
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equalsIgnoreCase(ExcelODAConstants.KEYWORD_AS))
				temp[i] = temp[i].toUpperCase();
			if (temp[i].equalsIgnoreCase(ExcelODAConstants.KEYWORD_FROM))
				temp[i] = temp[i].toUpperCase();
			if (temp[i].equalsIgnoreCase(ExcelODAConstants.KEYWORD_SELECT))
				temp[i] = temp[i].toUpperCase();
			result.append(temp[i]).append(ExcelODAConstants.DELIMITER_SPACE);
		}
		return result.toString().trim();
	}

	/**
	 * Prepare the meta data which will be used in execution of a query text. It
	 * sets the value of two member variables: resultSetMetaData and
	 * currentTableName
	 *
	 * @param queryText
	 * @throws OdaException
	 */
	private void prepareMetaData(String savedSelectedColInfo) throws OdaException {
		// limit the number of Rows to read to optimize getting metadata
		// from a xlsx file
		masterExcelFileSource = new ExcelFileSource(connProperties, currentTableName, worksheetNames, maxRowsToRead,
				null, null, appContext);

		String[] allColumnNames;
		String[] allColumnTypes;

		int columnCount = masterExcelFileSource.getColumnCount();
		allColumnNames = this.hasColumnNames ? discoverActualColumnMetaData(NAME_LITERAL, currentTableName)
				: createTempColumnNames(columnCount);
		allColumnTypes = this.hasTypeLine ? discoverActualColumnMetaData(TYPE_LITERAL, currentTableName)
				: createTempColumnTypes(columnCount);
		resultSetMetaData = new ResultSetMetaData(allColumnNames, allColumnTypes);

		if (allColumnNames.length != allColumnTypes.length)
			throw new OdaException(Messages.getString("invalid_excelfile_format")); //$NON-NLS-1$

		// the array that contains the column names read from command
		String[] queryColumnNames = null;
		String[] queryColumnTypes = null;
		String[] queryColumnLables = null;
		// dealing with "*"
		if (isWildCard(preparedColumnNames)) {
			queryColumnNames = allColumnNames;
			queryColumnTypes = allColumnTypes;
			queryColumnLables = allColumnNames;
			this.resultSetMetaDataHelper = new ResultSetMetaDataHelper(queryColumnNames, queryColumnTypes,
					queryColumnLables);
			this.resultSetMetaData = new ResultSetMetaData(this.resultSetMetaDataHelper);
		} else {
			queryColumnNames = ExcelFileSource.getStringArrayFromList(
					stripFormatInfoFromQueryColumnNames(getQueryColumnNamesVector((preparedColumnNames))));
			validateColumnName(queryColumnNames, allColumnNames);
			if (savedSelectedColInfo == null || savedSelectedColInfo.length() == 0 || hasTypeLine) {
				queryColumnTypes = this.hasTypeLine
						? getQueryColumnTypes(allColumnNames, allColumnTypes, queryColumnNames)
						: createTempColumnTypes(queryColumnNames.length);
				queryColumnLables = this.hasColumnNames ? columnLabels : queryColumnNames;
				if (queryColumnLables == null)
					queryColumnLables = queryColumnNames;
				this.resultSetMetaDataHelper = new ResultSetMetaDataHelper(queryColumnNames, queryColumnTypes,
						queryColumnLables);
				this.resultSetMetaData = new ResultSetMetaData(this.resultSetMetaDataHelper);

			} else {
				this.resultSetMetaDataHelper = new ResultSetMetaDataHelper(savedSelectedColInfo);
				this.resultSetMetaData = new ResultSetMetaData(this.resultSetMetaDataHelper);
			}
		}
	}

	/**
	 * Validate whether the given query text is empty.
	 *
	 * @param formattedQuery the trimed query text
	 * @throws OdaException if the given text is empty
	 */
	private void validateNonEmptyQueryText(String formattedQuery) throws OdaException {
		if (formattedQuery == null || formattedQuery.length() == 0)
			throw new OdaException(Messages.getString("query_COMMAND_IS_EMPTY")); //$NON-NLS-1$
	}

	/**
	 * Parse the command by separating keywords and other parts of a SQL SELECT
	 * query text.
	 *
	 * @param formattedQuery SQL SELECT query: SELECT COLUMNNAME (AS
	 *                       ALIAS)[,COLUMNNAME2 (AS ALIAS)] FROM TABLENAME
	 * @return a String array with first element that holds all the comma-separated
	 *         column names, the second element that holds all the comma-separated
	 *         column display labels, and the third element for table name(s) in
	 *         FROM clause
	 * @throws OdaException if the given query is not valid.
	 */
	private String[] parsePreparedQueryText(String formattedQuery) throws OdaException {

		return QueryTextUtil.getQueryMetaData(formattedQuery);
	}

	/**
	 *
	 * @param queryColumnNames
	 * @return
	 */
	private Vector<String> getQueryColumnNamesVector(String queryColumnNames) {
		Vector<String> result = new Vector<String>();
		char[] chars = queryColumnNames.toCharArray();
		List<Integer> indiceList = new ArrayList<Integer>();
		boolean inQuote = false;
		boolean isEscaped = false;
		int beginIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '"') {
				if (!isEscaped)
					inQuote = !inQuote;
				else
					isEscaped = !isEscaped;
			} else if (chars[i] == '\\') {
				isEscaped = !isEscaped;
			} else if (chars[i] == ',') {
				if (inQuote)
					continue;
				else
					indiceList.add(new Integer(i));
			}
		}

		if (indiceList.size() > 0) {
			for (int j = 0; j < indiceList.size(); j++) {

				endIndex = ((Integer) indiceList.get(j)).intValue();

				result.add(queryColumnNames.substring(beginIndex, endIndex).trim());
				beginIndex = endIndex + 1;

				if (j == indiceList.size() - 1) {
					result.add(queryColumnNames.substring(beginIndex, queryColumnNames.length()).trim());
				}
			}
		} else
			result.add(queryColumnNames);

		return result;
	}

	/**
	 *
	 * @param queryColumnNames
	 * @return
	 */
	private Vector<String> stripFormatInfoFromQueryColumnNames(Vector<String> queryColumnNames) {
		Vector<String> columnNames = new Vector<String>();

		boolean isEscaped = false;

		for (int i = 0; i < queryColumnNames.size(); i++) {
			StringBuffer sb = new StringBuffer();
			char[] chars = queryColumnNames.get(i).toCharArray();

			if (chars[0] != ExcelODAConstants.DELIMITER_DOUBLEQUOTE) {
				columnNames.add(queryColumnNames.get(i));
				continue;
			}

			for (int j = 0; j < chars.length; j++) {
				if (chars[j] == ExcelODAConstants.DELIMITER_DOUBLEQUOTE) {
					if (isEscaped) {
						sb.append(chars[j]);
						isEscaped = !isEscaped;
					}
				} else if (chars[j] == '\\') {
					if (isEscaped)
						sb.append(chars[j]);

					isEscaped = !isEscaped;
				} else
					sb.append(chars[j]);
			}

			columnNames.add(sb.toString());
		}

		return columnNames;
	}

	/**
	 * Returns the array that contains the types of column names read from a query
	 * text.
	 *
	 * @param allColumnNames   The array contains all column names read from file
	 * @param allColumnTypes   The array contains all column types read from file
	 * @param queryColumnNames The array contains those column names specified in a
	 *                         query
	 * @return
	 */
	private String[] getQueryColumnTypes(String[] allColumnNames, String[] allColumnTypes, String[] queryColumnNames) {
		if (!this.hasTypeLine)
			return null;

		// the array that contains the types of column names read from a query
		String[] queryColumnTypes = new String[queryColumnNames.length];

		for (int i = 0; i < queryColumnNames.length; i++) {
			for (int j = 0; j < allColumnNames.length; j++) {
				if (queryColumnNames[i].trim().equalsIgnoreCase(allColumnNames[j])) {
					queryColumnTypes[i] = allColumnTypes[j];
					break;
				}
			}

		}
		return queryColumnTypes;
	}

	/**
	 * Returns an array that contains column labels.
	 *
	 * @param queryFragments
	 * @return a String array that contains column labels
	 */
	private String[] getColumnLabels(String[] queryFragments) {
		String queryColumnLabels = getPreparedColumnLabels(queryFragments);
		return queryColumnLabels != null ? queryColumnLabels.split(ExcelODAConstants.DELIMITER_COMMA_VALUE) : null;
	}

	/**
	 * Return the String that contains column label(s) selected in a query. Multiple
	 * column labels, if any, are separated by comma.
	 *
	 * @param parsedQueryFragments the string array which is generated by the
	 *                             parsePreparedQueryText method
	 * @return the comma-separated column labels selected in a query
	 */
	private String getPreparedColumnLabels(String[] parsedQueryFragments) {
		return parsedQueryFragments[1];
	}

	/**
	 * Validate whether the given query segments contains a single table name.
	 *
	 * @param parsedQuerySegments
	 * @throws OdaException if the given query contains multiple table names
	 */
	private void validateSingleTableQuery(String[] parsedQuerySegments) throws OdaException {
		if (getPreparedTableNames(parsedQuerySegments).split(ExcelODAConstants.DELIMITER_COMMA_VALUE).length != 1)
			throw new OdaException(Messages.getString("query_DO_NOT_SUPPORT_CROSS_TABLE_QUERY")); //$NON-NLS-1$
	}

	/**
	 * Return the String which contains the identifiers in the FROM clause.
	 *
	 * @param parsedQueryFragments the string array which is generated by the
	 *                             parsePreparedQueryText method
	 * @return a String that contains table name(s) after the FROM keyword
	 */
	private String getPreparedTableNames(String[] parsedQueryFragments) {
		return parsedQueryFragments[2];
	}

	/**
	 * Return the String that contains column name(s) selected in a query. Multiple
	 * column names, if any, are separated by comma.
	 *
	 * @param parsedQueryFragments the string array which is generated by the
	 *                             parsePreparedQueryText method
	 * @return the comma-separated column names selected in a query
	 */
	private String getPreparedColumnNames(String[] parsedQueryFragments) {
		return parsedQueryFragments[0];
	}

	/**
	 * Returns a specified array of metadata info
	 *
	 * @param metaDataType : currently has two values: "NAME" and "TYPE"
	 * @param tableName
	 * @return String[] an array that holds the specified metadata
	 * @throws OdaException
	 */
	private String[] discoverActualColumnMetaData(String metaDataType, String tableName) throws OdaException {
		// use cached copy to reduce number of times the xlsx file is read

		// ExcelFileSource excelFileSource = new ExcelFileSource(
		// this.connProperties, tableName, worksheetNames,
		// 10, null, null);
		try {
			masterExcelFileSource.resetRowCounter();
			if (!(metaDataType.trim().equalsIgnoreCase(NAME_LITERAL)
					|| metaDataType.trim().equalsIgnoreCase(TYPE_LITERAL)))
				throw new OdaException(Messages.getString("query_ARGUMENT_ERROR")); //$NON-NLS-1$

			// if want to discover type information then just skip all the empty
			// lines and the first line
			if (metaDataType.trim().equalsIgnoreCase(TYPE_LITERAL)) {
				while (masterExcelFileSource.isEmptyRow(masterExcelFileSource.readLine()))
					continue;
			}
			// Skip all the empty lines until reach the first line
			List<String> columnNameLine;
			while (masterExcelFileSource.isEmptyRow(columnNameLine = masterExcelFileSource.readLine()))
				continue;

			String[] result = masterExcelFileSource.getColumnNameArray(columnNameLine);

			if (metaDataType.trim().equalsIgnoreCase(NAME_LITERAL))
				this.validateUniqueName(result);
			if (metaDataType.trim().equalsIgnoreCase(TYPE_LITERAL))
				validateColumnTypeConsistency(result);

			return trimStringArray(result);

		} catch (IOException e) {
			throw new OdaException(e);
		} finally {
			if (masterExcelFileSource != null)
				masterExcelFileSource.close();
		}
	}

	private String[] createTempColumnNames(int columnCount) {
		String[] tempColumnNames = new String[columnCount];

		for (int i = 0; i < columnCount; i++) {
			tempColumnNames[i] = "COLUMN_" + (i + 1); //$NON-NLS-1$
		}

		return tempColumnNames;
	}

	private String[] createTempColumnTypes(int columnCount) {
		String[] tempColumnTypes = new String[columnCount];

		for (int i = 0; i < columnCount; i++) {
			tempColumnTypes[i] = "STRING"; //$NON-NLS-1$
		}

		return tempColumnTypes;
	}

	/**
	 * @param cCN
	 * @return
	 */
	private boolean isWildCard(String cCN) {
		if (cCN.equalsIgnoreCase(ExcelODAConstants.KEYWORD_ASTERISK))
			return true;
		return false;
	}

	private void validateUniqueName(String[] aCN) throws OdaException {
		for (int i = 0; i < aCN.length; i++) {
			if (this.findOccuranceOfValueInStringArray(aCN[i], aCN) > 1) {
				throw new OdaException(Messages.getString("query_SOURCE_DATA_ERROR")); //$NON-NLS-1$
			}
		}
	}

	private void validateColumnTypeConsistency(String[] aCT) throws OdaException {
		if (!this.hasTypeLine)
			return;
		for (int i = 0; i < aCT.length; i++) {
			if (!DataTypes.isValidType(aCT[i])) {
				throw new OdaException(Messages.getString("dataTypes_TYPE_NAME_INVALID") + aCT[i]); //$NON-NLS-1$
			}
		}
	}

	private int findOccuranceOfValueInStringArray(String value, String[] array) {
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if (value.trim().equalsIgnoreCase(array[i].trim()))
				count++;
		}
		return count;
	}

	/**
	 * @param result
	 * @return
	 */
	private String[] trimStringArray(String[] array) {
		String[] result = new String[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i].trim();
		return result;
	}

	/**
	 * Check whether a column name given in query has exactly one occurance in
	 * actual table
	 *
	 * @param cCN the array of column names in query
	 * @param aCN the array of column names in actual table
	 * @throws OdaException
	 */
	private void validateColumnName(String[] cCN, String[] aCN) throws OdaException {
		for (int i = 0; i < cCN.length; i++) {
			if (this.findOccuranceOfValueInStringArray(cCN[i], aCN) != 1) {
				this.isInvalidQuery = true;
				throw new OdaException(Messages.getString("query_COMMAND_NOT_VALID")); //$NON-NLS-1$
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang
	 * .Object)
	 */
	public void setAppContext(Object context) throws OdaException {
		this.appContext = (Map) context;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	public void close() throws OdaException {
		maxRows = 0;
		resultSetMetaData = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException {
		if (resultSetMetaData == null)
			prepareMetaData(colInfo);
		return resultSetMetaData;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws OdaException {
		if (this.isInvalidQuery)
			throw new OdaException(Messages.getString("query_COMMAND_NOT_VALID")); //$NON-NLS-1$

		// Should only happen while designing the dataset
		if (masterExcelFileSource == null) {
			return new ResultSet(
					new ExcelFileSource(this.connProperties, this.currentTableName, worksheetNames, this.maxRows,
							this.getMetaData(), this.resultSetMetaDataHelper, appContext),
					(ResultSetMetaData) this.getMetaData());

		}
		if (this.resultSetMetaData != null)
			masterExcelFileSource.setRsmd(this.resultSetMetaData);

		if (this.resultSetMetaDataHelper != null)
			masterExcelFileSource.setRsmdHelper(this.resultSetMetaDataHelper);

		masterExcelFileSource.setStatementMaxRows(this.maxRows);

		return new ResultSet(masterExcelFileSource, this.resultSetMetaData);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String ,
	 * java.lang.String)
	 */
	public void setProperty(String name, String value) throws OdaException {
		if (name.equals(ExcelODAConstants.CONN_WORKSHEETS_PROP))
			this.worksheetNames = value;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws OdaException {
		maxRows = max;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	public int getMaxRows() throws OdaException {
		return maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters() throws OdaException {
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String,
	 * int)
	 */
	public void setInt(String parameterName, int value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt(int parameterId, int value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String,
	 * double)
	 */
	public void setDouble(String parameterName, double value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble(int parameterId, double value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang
	 * .String, java.math.BigDecimal)
	 */
	public void setBigDecimal(String parameterName, BigDecimal value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int,
	 * java.math.BigDecimal)
	 */
	public void setBigDecimal(int parameterId, BigDecimal value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String,
	 * java.lang.String)
	 */
	public void setString(String parameterName, String value) throws OdaException {
		// only applies to named input parameter
		System.out.println(parameterName);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int,
	 * java.lang.String)
	 */
	public void setString(int parameterId, String value) throws OdaException {
		// only applies to input parameter
		System.out.println(value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String,
	 * java.sql.Date)
	 */
	public void setDate(String parameterName, Date value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int,
	 * java.sql.Date)
	 */
	public void setDate(int parameterId, Date value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String,
	 * java.sql.Time)
	 */
	public void setTime(String parameterName, Time value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int,
	 * java.sql.Time)
	 */
	public void setTime(int parameterId, Time value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.
	 * String, java.sql.Timestamp)
	 */
	public void setTimestamp(String parameterName, Timestamp value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int,
	 * java.sql.Timestamp)
	 */
	public void setTimestamp(int parameterId, Timestamp value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String ,
	 * boolean)
	 */
	public void setBoolean(String parameterName, boolean value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
	 */
	public void setBoolean(int parameterId, boolean value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String,
	 * java.lang.Object)
	 */
	public void setObject(String parameterName, Object value) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int,
	 * java.lang.Object)
	 */
	public void setObject(int parameterId, Object value) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
	 */
	public void setNull(String parameterName) throws OdaException {
		// only applies to named input parameter
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
	 */
	public void setNull(int parameterId) throws OdaException {
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang
	 * .String)
	 */
	public int findInParameter(String parameterName) throws OdaException {
		// only applies to named input parameter
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException {
		return new ParameterMetaData();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse
	 * .datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec(SortSpec sortBy) throws OdaException {
		// only applies to sorting, assumes not supported
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec() throws OdaException {
		// only applies to sorting
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse
	 * .datatools.connectivity.oda.spec.QuerySpecification)
	 */
	public void setSpecification(QuerySpecification querySpec) throws OdaException, UnsupportedOperationException {
		// assumes no support
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
	 */
	public QuerySpecification getSpecification() {
		// assumes no support
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
	 */
	public String getEffectiveQueryText() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
	 */
	public void cancel() throws OdaException, UnsupportedOperationException {
		// assumes unable to cancel while executing a query
		throw new UnsupportedOperationException();
	}

	public Properties getConnectionProperties() {
		return this.connProperties;
	}
}
