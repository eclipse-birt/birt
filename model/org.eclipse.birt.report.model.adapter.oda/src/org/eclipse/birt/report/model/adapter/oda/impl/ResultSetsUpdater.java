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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.impl.ResultSetsAdapter.ResultSetColumnInfo;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.emf.common.util.EList;

/**
 * Updates all the design parameters to oda data set parameter handle and linked
 * scalar parameter handle.
 */
class ResultSetsUpdater {

	/**
	 * The data set handle defined result set columns that want to be updated.
	 */

	private List<OdaResultSetColumnHandle> toUpdateColumns = null;

	/**
	 * The data set handle originally defined result set columna.
	 */

	private List<OdaResultSetColumnHandle> setDefinedColumns = null;

	private String dataSourceId = null;
	private String dataSetId = null;

	private OdaDataSetHandle setHandle = null;

	private final ResultSetCriteriaAdapter filterAdapter;

	/**
	 * @param setDesign
	 * @param cachedParameters
	 * @param setHandle
	 */

	ResultSetsUpdater(DataSetDesign setDesign, OdaDataSetHandle setHandle, List<OdaResultSetColumn> updateParams) {

		this.setHandle = setHandle;
		filterAdapter = new ResultSetCriteriaAdapter(setHandle, setDesign);

		Iterator<OdaResultSetColumnHandle> tmpParams = setHandle.resultSetIterator();
		setDefinedColumns = new ArrayList<>();
		while (tmpParams.hasNext()) {
			setDefinedColumns.add(tmpParams.next());
		}

		dataSourceId = setDesign.getOdaExtensionDataSourceId();
		dataSetId = setDesign.getOdaExtensionDataSetId();

		this.toUpdateColumns = buildUpdateParams(updateParams);

	}

	private List<OdaResultSetColumnHandle> buildUpdateParams(List<OdaResultSetColumn> updateParams) {
		if (updateParams == null) {
			return Collections.emptyList();
		}

		List<OdaResultSetColumnHandle> retList = new ArrayList<>();
		for (int i = 0; i < updateParams.size(); i++) {
			OdaResultSetColumn param = updateParams.get(i);
			for (int j = 0; j < setDefinedColumns.size(); j++) {
				OdaResultSetColumnHandle paramHandle = setDefinedColumns.get(j);
				if (paramHandle.getStructure() == param && !retList.contains(paramHandle)) {
					retList.add(paramHandle);
				}

			}
		}
		return retList;
	}

	/**
	 *
	 */

	public void processResultSets(ResultSets resultSets) throws SemanticException {
		if (resultSets == null) {
			return;
		}

		EList<ResultSetDefinition> tmpResultSets = resultSets.getResultSetDefinitions();
		if (tmpResultSets.isEmpty()) {
			return;
		}

		ResultSetDefinition resultSetDefinition = tmpResultSets.get(0);
		assert resultSetDefinition != null;

		ResultSetColumns setColumns = resultSetDefinition.getResultSetColumns();
		if (setColumns == null) {
			return;
		}
		EList<ColumnDefinition> odaSetColumns = setColumns.getResultColumnDefinitions();
		if (odaSetColumns.isEmpty()) {
			return;
		}

		OdaResultSetColumnHandle foundColumn = null;
		OdaResultSetColumnHandle oldSetColumn = null;
		OdaResultSetColumn newColumn = null;
		List<ResultSetColumnInfo> infoList = new ArrayList<>();
		List<OdaResultSetColumn> newColumns = new ArrayList<>();
		List<ColumnHint> newHints = new ArrayList<>();

		for (int i = 0; i < odaSetColumns.size(); i++) {
			ColumnDefinition columnDefn = odaSetColumns.get(i);

			DataElementAttributes dataAttrs = columnDefn.getAttributes();

			// if data attributes is empty, just create a new result set and
			// update it from the scratch
			if (dataAttrs == null) {
				newColumn = StructureFactory.createOdaResultSetColumn();
			} else {
				String nativeName = dataAttrs.getName();
				Integer position = dataAttrs.getPosition();
				Integer nativeDataType = dataAttrs.getNativeDataTypeCode();

				foundColumn = ResultSetsAdapter.findOdaResultSetColumn(toUpdateColumns.iterator(), nativeName, position,
						nativeDataType);

				// if foundParam == null, could be two cases: 1. no need to
				// update; 2. this is a new result set column
				if (foundColumn == null) {
					oldSetColumn = ResultSetsAdapter.findOdaResultSetColumn(setDefinedColumns.iterator(), nativeName,
							position, nativeDataType);
				}

				if (foundColumn == null) {
					// this is a new result set column, need to update from the
					// scratch
					if (oldSetColumn == null) {
						newColumn = StructureFactory.createOdaResultSetColumn();
					} else {
						// just copy it since no need to update this one
						newColumn = (OdaResultSetColumn) oldSetColumn.getStructure().copy();
						newColumns.add(newColumn);
						infoList.add(new ResultSetColumnInfo(newColumn, null));
						continue;
					}
				} else {
					newColumn = (OdaResultSetColumn) foundColumn.getStructure().copy();
				}
			}

			newColumns.add(newColumn);

			ResultSetColumnUpdater oneUpdater = new ResultSetColumnUpdater(newColumn, columnDefn, setHandle,
					dataSourceId, dataSetId);
			ColumnHint hint = oneUpdater.process();
			if (hint != null) {
				newHints.add(hint);
			}

			infoList.add(new ResultSetColumnInfo(newColumn, hint));
		}

		// create a unique name for all oda result set column
		ResultSetsAdapter.createUniqueResultSetColumnNames(infoList);

		// now clear all the old columns and then set it to newColumns
		PropertyHandle propHandle = setHandle.getPropertyHandle(OdaDataSetHandle.RESULT_SET_PROP);
		propHandle.setValue(new ArrayList());

		if (!newColumns.isEmpty()) {
			for (int i = 0; i < newColumns.size(); i++) {
				propHandle.addItem(newColumns.get(i));
			}
		}

		// collect all column hints for computed column
		List<ColumnHint> computedColumnHints = collectHintsForComputedColumn();

		// new clear all the old hints and then add the hints for result set
		// column first
		propHandle = setHandle.getPropertyHandle(OdaDataSetHandle.COLUMN_HINTS_PROP);
		propHandle.setValue(new ArrayList());
		if (!newHints.isEmpty()) {
			for (int i = 0; i < newHints.size(); i++) {
				ColumnHint hint = (ColumnHint) newHints.get(i);
				ColumnHintHandle oldHint = AdapterUtil.findColumnHint(
						(String) hint.getProperty(null, ColumnHint.COLUMN_NAME_MEMBER),
						setHandle.columnHintsIterator());

				if (oldHint == null) {
					propHandle.addItem(newHints.get(i));
				} else {
					oldHint.setDisplayName((String) hint.getProperty(null, ColumnHint.DISPLAY_NAME_MEMBER));
					oldHint.setHelpText((String) hint.getProperty(null, ColumnHint.HELP_TEXT_MEMBER));
					oldHint.setFormat((String) hint.getProperty(null, ColumnHint.FORMAT_MEMBER));
				}
			}
		}

		// second, add column hints for the computed column
		for (int i = 0; i < computedColumnHints.size(); i++) {
			propHandle.addItem((ColumnHint) computedColumnHints.get(i));
		}

		// add filter condition for the result set
		filterAdapter.updateROMSortAndFilter();
	}

	private List<ColumnHint> collectHintsForComputedColumn() {
		Iterator columns = setHandle.computedColumnsIterator();
		List<ColumnHint> hints = new ArrayList<>();
		while (columns.hasNext()) {
			ComputedColumnHandle tmpColumn = (ComputedColumnHandle) columns.next();
			String columnName = tmpColumn.getName();
			ColumnHintHandle hintHandle = AdapterUtil.findColumnHint(columnName, setHandle.columnHintsIterator());
			if (hintHandle == null) {
				continue;
			}
			hints.add((ColumnHint) hintHandle.getStructure().copy());

		}

		return hints;
	}

}
