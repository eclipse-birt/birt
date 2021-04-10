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

package org.eclipse.birt.report.data.oda.excel;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.birt.report.data.oda.excel.impl.util.querytextutil.ColumnsInfoUtil;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A helper class that helps ResultSetMetaData to parse column information and
 * provide a public access to ResultSet's original column names.
 */

public class ResultSetMetaDataHelper {
	String savedSelectedColumnsInfoString;
	String[] columnNames;
	String[] columnTypes;
	String[] originalColumnNames;
	String[] columnLabels;

	private Map<String, Integer> columnNameIndexMap = new HashMap<String, Integer>();

	/**
	 * Constructor
	 *
	 * @param colNames
	 * @param colTypes
	 * @param colLabels
	 * @throws OdaException
	 */
	public ResultSetMetaDataHelper(String[] colNames, String[] colTypes, String[] colLabels) throws OdaException {
		if (colNames == null)
			throw new OdaException(Messages.getString("common_ARGUMENT_CANNOT_BE_NULL")); //$NON-NLS-1$

		this.columnNames = colNames;
		this.columnTypes = colTypes;
		this.columnLabels = colLabels;
		this.originalColumnNames = colNames;
		trimMetaDataStrings();
		initMap();
	}

	/**
	 *
	 * @param savedSelectedColumnsInfoString
	 */
	public ResultSetMetaDataHelper(String savedSelectedColumnsInfoString) {
		this.savedSelectedColumnsInfoString = savedSelectedColumnsInfoString;
		ColumnsInfoUtil ciu = new ColumnsInfoUtil(savedSelectedColumnsInfoString);
		this.columnNames = ciu.getColumnNames();
		this.columnTypes = ciu.getColumnTypeNames();
		this.originalColumnNames = ciu.getOriginalColumnNames();
		this.columnLabels = this.columnNames;
		trimMetaDataStrings();
		initMap();
	}

	private void initMap() {
		for (int i = 0; i < columnNames.length; i++) {
			columnNameIndexMap.put(columnNames[i], Integer.valueOf(i));
		}
	}

	/**
	 *
	 *
	 */
	private void trimMetaDataStrings() {
		assert columnNames.length == columnTypes.length && columnTypes.length == originalColumnNames.length
				&& originalColumnNames.length == columnLabels.length;

		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = columnNames[i].trim();
			columnTypes[i] = columnTypes[i].trim();
			columnLabels[i] = columnLabels[i].trim();
			originalColumnNames[i] = originalColumnNames[i].trim();
		}
	}

	/**
	 *
	 * @return
	 */
	public String[] getColumnNames() {

		return this.columnNames;
	}

	/**
	 *
	 * @return
	 */
	public String[] getColumnTypes() {
		return this.columnTypes;
	}

	/**
	 *
	 * @return
	 */
	public String[] getColumnLabels() {
		return this.columnLabels;
	}

	/**
	 * Get the orignal column names
	 *
	 * @return
	 */
	public String[] getOriginalColumnNames() {
		return this.originalColumnNames;
	}

	/**
	 * Get the original column name of the specified column name
	 *
	 * @return
	 */
	public String getOriginalColumnName(String columnName) {
		String originName = null;

		Integer index = (Integer) columnNameIndexMap.get(columnName);

		if (index != null) {
			originName = originalColumnNames[index.intValue()];
		}
		return originName;
	}

}
