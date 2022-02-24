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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;

/**
 * Update the oda result column with the information in the column definition.
 */

class ResultSetColumnUpdater {

	ColumnDefinition columnDefn = null;

	OdaResultSetColumn newColumn;

	String dataSourceId = null;

	String dataSetId = null;

	OdaDataSetHandle setHandle;

	/**
	 * @param columnDefn
	 * @param columnHandle
	 */

	ResultSetColumnUpdater(OdaResultSetColumn newColumn, ColumnDefinition columnDefn, OdaDataSetHandle setHandle,
			String dataSourceId, String dataSetId) {
		if (newColumn == null || columnDefn == null || setHandle == null)
			throw new IllegalArgumentException("The column definition and oda result set column can not be null!"); //$NON-NLS-1$
		this.columnDefn = columnDefn;
		this.newColumn = newColumn;
		this.dataSourceId = dataSourceId;
		this.dataSetId = dataSetId;
		this.setHandle = setHandle;
	}

	/**
	 * 
	 */

	ColumnHint process() {
		DataElementAttributes dataAttrs = columnDefn.getAttributes();
		processDataElementAttributes(dataAttrs);

		// update column hint
		ColumnHint oldHint = null;
		ColumnHintHandle oldHintHandle = AdapterUtil.findColumnHint(newColumn, setHandle.columnHintsIterator());
		if (oldHintHandle != null)
			oldHint = (ColumnHint) oldHintHandle.getStructure();

		ColumnHint newHint = ResultSetsAdapter.newROMColumnHintFromColumnDefinition(columnDefn, null, oldHint,
				newColumn);

		return newHint;
	}

	/**
	 * 
	 */
	private void processDataElementAttributes(DataElementAttributes dataAttrs) {
		// check the native name

		if (dataAttrs == null)
			return;

		// update the name with the native name in oda result set column
		String newValue = dataAttrs.getName();
		newColumn.setNativeName(newValue);

		// update position
		int position = dataAttrs.getPosition();
		newColumn.setPosition(Integer.valueOf(position));

		// update native data type
		int newNativeDataType = dataAttrs.getNativeDataTypeCode();
		newColumn.setNativeDataType(newNativeDataType);

		// update data type
		newColumn.setDataType(
				AdapterUtil.convertNativeTypeToROMDataType(dataSourceId, dataSetId, newNativeDataType, null));
	}

}
