/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * Represents the handle of one column in the result set. The result set column
 * defines the data in which column is in the result set.
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a result set column has an optional name.</dd>
 *
 * <dt><strong>Position </strong></dt>
 * <dd>a result set column has an optional position for it.</dd>
 *
 * <dt><strong>Data Type </strong></dt>
 * <dd>a result set column has a choice data type: any, integer, string, data
 * time, decimal, float, structure or table.</dd>
 * </dl>
 *
 */

public class ResultSetColumnHandle extends StructureHandle {

	/**
	 * Constructs the handle of result set column.
	 *
	 * @param valueHandle the value handle for result set column list of one
	 *                    property
	 * @param index       the position of this result set column in the list
	 */

	public ResultSetColumnHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	public ResultSetColumnHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
	}

	/**
	 * Returns the column name.
	 *
	 * @return the column name
	 */

	public String getColumnName() {
		return getStringProperty(ResultSetColumn.NAME_MEMBER);
	}

	/**
	 * Sets the column name.
	 *
	 * @param columnName the column name to set
	 * @throws SemanticException value required exception
	 */

	public void setColumnName(String columnName) throws SemanticException {
		setProperty(ResultSetColumn.NAME_MEMBER, columnName);
	}

	/**
	 * Returns the data type of this column. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 *
	 * @return the data type of this column.
	 */

	public String getDataType() {
		return getStringProperty(ResultSetColumn.DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the data type of this column. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 *
	 * @param dataType the data type to set
	 * @throws SemanticException if the dataType is not in the choice list.
	 */

	public void setDataType(String dataType) throws SemanticException {
		setProperty(ResultSetColumn.DATA_TYPE_MEMBER, dataType);
	}

	/**
	 * Returns the position that this column is in the result set.
	 *
	 * @return the position that this column is in the result set.
	 */

	public Integer getPosition() {
		return (Integer) getProperty(ResultSetColumn.POSITION_MEMBER);
	}

	/**
	 * Sets the position that this column is in the result set.
	 *
	 * @param position the position to set
	 */

	public void setPosition(Integer position) {
		setPropertySilently(ResultSetColumn.POSITION_MEMBER, position);
	}

	/**
	 * Returns the native data type.
	 *
	 * @return the result set column native data type.
	 */

	public Integer getNativeDataType() {
		return (Integer) getProperty(ResultSetColumn.NATIVE_DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the result set column native data type.
	 *
	 * @param dataType the native data type to set.
	 */

	public void setNativeDataType(Integer dataType) {
		setPropertySilently(ResultSetColumn.NATIVE_DATA_TYPE_MEMBER, dataType);
	}
}
