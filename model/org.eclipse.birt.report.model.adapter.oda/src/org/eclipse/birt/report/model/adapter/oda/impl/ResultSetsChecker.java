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

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousResultSetNode;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSets;
import org.eclipse.emf.common.util.EList;

/**
 * Class to check all the result set column and result set column hint defined
 * by data set design.
 */
class ResultSetsChecker {

	/**
	 *
	 */
	private final DataSetDesign setDesign;

	private final Iterator setDefinedResultsIter;

	private final Iterator columnHintsIter;

	/**
	 * @param setDesign
	 * @param cachedParameters
	 * @param setHandle
	 */

	ResultSetsChecker(DataSetDesign setDesign, OdaDataSetHandle setHandle) {
		this.setDesign = setDesign;
		this.setDefinedResultsIter = setHandle.resultSetIterator();
		this.columnHintsIter = setHandle.columnHintsIterator();
	}

	/**
	 *
	 */

	List<IAmbiguousResultSetNode> process() {
		ResultSetDefinition resultDefn = setDesign.getPrimaryResultSet();
		if (resultDefn == null) {
			ResultSets resultSets = setDesign.getResultSets();

			if (resultSets == null) {
				return Collections.emptyList();
			}

			EList<ResultSetDefinition> definitions = resultSets.getResultSetDefinitions();
			if (definitions.isEmpty()) {
				return Collections.emptyList();
			}

			resultDefn = definitions.get(0);
		}
		assert resultDefn != null;

		ResultSetColumns setColumns = resultDefn.getResultSetColumns();
		if (setColumns == null) {
			return Collections.emptyList();
		}

		EList<ColumnDefinition> odaSetColumns = setColumns.getResultColumnDefinitions();
		if (odaSetColumns.isEmpty()) {
			return Collections.emptyList();
		}

		List<IAmbiguousResultSetNode> ambiguousResultSets = new ArrayList<>(4);

		for (int i = 0; i < odaSetColumns.size(); i++) {
			ColumnDefinition columnDefn = odaSetColumns.get(i);
			OdaResultSetColumnHandle existingColumnHandle = null;

			DataElementAttributes dataAttrs = columnDefn.getAttributes();
			if (dataAttrs != null) {
				String nativeName = dataAttrs.getName();
				Integer position = dataAttrs.getPosition();
				Integer nativeDataType = dataAttrs.getNativeDataTypeCode();

				existingColumnHandle = ResultSetsAdapter.findOdaResultSetColumn(setDefinedResultsIter, nativeName,
						position, nativeDataType);
			}

			// if not found the matched column handle, do nothing
			if (existingColumnHandle == null) {
				continue;
			}

			ColumnHintHandle existingColumnHintHandle = AdapterUtil.findColumnHint(existingColumnHandle.getColumnName(),
					columnHintsIter);

			ResultSetColumnChecker oneChecker = new ResultSetColumnChecker(columnDefn, existingColumnHandle,
					existingColumnHintHandle);

			List<IAmbiguousAttribute> attrs = oneChecker.process();
			if (attrs == null || attrs.isEmpty()) {
				continue;
			}

			IAmbiguousResultSetNode node = new AmbiguousResultSetNode(existingColumnHandle, attrs);
			ambiguousResultSets.add(node);
		}

		return ambiguousResultSets;
	}
}
