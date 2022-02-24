/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api;

import java.util.List;

/**
 * an engine task that extracts data from a report. The task allows the return
 * of metadata and data from engine
 * 
 * User first creates the task from engine, then sets a report component ID, or
 * report component instance ID. If none is set, data extraction is assumed to
 * be based on all the data stored in the report. The user can call the
 * getMetaData method to retrieve metadata for each resultset. Based on the
 * metadata, he can select additional columns, add filter conditions, or specify
 * sorting conditions.
 */
public interface IDataExtractionTask extends IExtractionTask {
	// public static int SORT_DIRECTION_ASCENDING = 0;
	// public static int SORT_DIRECTION_DESCENDING = 1;

	/**
	 * sets the report item identifier that data extraction will happen on
	 * 
	 * @param cid report item identifier
	 */
	// public void setItemID( ComponentID cid );

	/**
	 * to set the indicator whether to enable the cube export functionality.
	 */
	public void setCubeExportEnabled(boolean isCubeExportEnabled);

	/**
	 * to indicate whether the cube export functionality should be enabled or not.
	 */
	public boolean isCubeExportEnabled();

	/**
	 * * @param iid
	 * 
	 * identifies a report item instance that data extraction will happen on
	 */
	public void setInstanceID(InstanceID iid);

	/**
	 * returns the metadata corresponding to the data stored in the report document,
	 * for the specific extraction level, i.e., report, daat set, report item, or
	 * report item instance levels. To get the metadata for the extracted data, use
	 * the getResultMetaData method from the IDataIterator interface.
	 * 
	 * @return a List of IResultMetaData. The list usually has one result set meta
	 *         data, but could have more if data extraction is based on the whole
	 *         report
	 * @deprecated
	 */
	public List getMetaData() throws EngineException;

	/**
	 * returns the metadata corresponding to the data stored in the report document,
	 * for the specific extraction level, i.e., report, daat set, report item, or
	 * report item instance levels. To get the metadata for the extracted data, use
	 * the getResultMetaData method from the IDataIterator interface.
	 * 
	 * @return a List of IResultSetItem.
	 */
	public List getResultSetList() throws EngineException;

	/**
	 * select the result set from which to export data.
	 * 
	 * @param resultSetName the result set name
	 */
	public void selectResultSet(String resultSetName);

	/**
	 * @param columnName name of the column to be included in the data set
	 */
	public void selectColumns(String[] columnNames);

	/**
	 * @param maxRows set the maximum rows that are returned from ResultSet
	 */
	public void setMaxRows(int maxRows);

	/**
	 * Sets start row of the result.
	 */
	public void setStartRow(int startRow);

	/**
	 * whether gets distinct values
	 */
	public void setDistinctValuesOnly(boolean distinct);

	/**
	 * Redeclare this method in order to return IExtractionResults.
	 */
	public IExtractionResults extract() throws EngineException;
}
