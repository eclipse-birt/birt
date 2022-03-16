/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;

/**
 * resultset column from resultset property
 *
 */
class ResultSetColumnDefinition extends ColumnDefinition {
	String name, nativeName;
	int position = -1;
	int dataType = DataType.UNKNOWN_TYPE;
	int nativeDataType = 0; // unknown
	String alias;
	String displayName;
	boolean computedCol = false;
	String dataTypeName;

	/**
	 * Construct a Column definition for a named column
	 */
	ResultSetColumnDefinition(String name) {
		super(name);
	}

	/**
	 * Gets the data type of the column.
	 *
	 * @return Data type as an integer.
	 */
	String getDataTypeName() {
		return dataTypeName;
	}

	/**
	 * @param dataType The dataType to set.
	 */
	void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}

	/**
	 *
	 * @param displayName
	 */
	void setLableName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 *
	 * @return
	 */
	String getLableName() {
		return this.displayName;
	}

	/**
	 *
	 * @param computedCol
	 */
	void setComputedColumn(boolean computedCol) {
		this.computedCol = computedCol;
	}

	/**
	 *
	 * @return
	 */
	boolean isComputedColumn() {
		return this.computedCol;
	}
}
