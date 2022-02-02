/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.query.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.cursor.DrilledAggregateResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

public class DrillOperationExecutor {

	public IAggregationResultSet[] execute(IAggregationResultSet[] aggregationRsFromCube,
			IAggregationResultSet[] aggregationRsForDrill, ICubeQueryDefinition iCubeQueryDefinition)
			throws IOException, DataException {
		IEdgeDefinition columnEdge = iCubeQueryDefinition.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IEdgeDefinition rowEdge = iCubeQueryDefinition.getEdge(ICubeQueryDefinition.ROW_EDGE);
		List<DrillOnDimensionHierarchy> columnDrill = CubeQueryDefinitionUtil.flatternDrillFilter(columnEdge);
		List<DrillOnDimensionHierarchy> rowDrill = CubeQueryDefinitionUtil.flatternDrillFilter(rowEdge);
		List<DrillOnDimensionHierarchy> combinedDrill = new ArrayList<DrillOnDimensionHierarchy>();
		combinedDrill.addAll(rowDrill);
		combinedDrill.addAll(columnDrill);

		int index = 0;
		if (columnEdge != null) {
			if (!columnEdge.getDrillFilter().isEmpty()) {
				IAggregationResultSet rs = populateResultSet(aggregationRsFromCube[index], columnDrill);
				aggregationRsFromCube[index] = rs;
			}
			index++;
		}
		if (rowEdge != null) {
			if (!rowEdge.getDrillFilter().isEmpty()) {
				IAggregationResultSet rs = populateResultSet(aggregationRsFromCube[index], rowDrill);
				aggregationRsFromCube[index] = rs;
			}
			index++;
		}

		if (!combinedDrill.isEmpty()) {
			for (int i = index; i < aggregationRsFromCube.length; i++) {
				List<IAggregationResultSet> drillRs = new ArrayList<IAggregationResultSet>();
				for (int j = 0; j < aggregationRsForDrill.length; j++) {
					if (aggregationRsForDrill[j].getAggregationDefinition().getDrilledInfo() != null
							&& aggregationRsForDrill[j].getAggregationDefinition().getDrilledInfo()
									.usedByAggregation(aggregationRsFromCube[i].getAggregationDefinition())) {
						drillRs.add(aggregationRsForDrill[j]);
					}
				}
				IAggregationResultSet[] drilledAggregationResult = new IAggregationResultSet[drillRs.size()];
				for (int k = 0; k < drillRs.size(); k++) {
					drilledAggregationResult[k] = (IAggregationResultSet) drillRs.get(k);
				}
				IAggregationResultSet rs = populateResultSet(aggregationRsFromCube[i], drilledAggregationResult,
						combinedDrill);
				aggregationRsFromCube[i] = rs;
			}
		}
		return aggregationRsFromCube;
	}

	private IAggregationResultSet populateResultSet(IAggregationResultSet aggregationRsFromCube,
			IAggregationResultSet[] aggregationRsFromDrill, List<DrillOnDimensionHierarchy> drillFilters)
			throws IOException, DataException {
		if (aggregationRsFromCube.getAllLevels() == null || aggregationRsFromCube.getAllLevels().length == 0
				|| aggregationRsFromCube.length() == 0)
			return aggregationRsFromCube;
		DrilledAggregateResultSet rs = new DrilledAggregateResultSet(aggregationRsFromCube, aggregationRsFromDrill,
				drillFilters);
		return rs;
	}

	private IAggregationResultSet populateResultSet(IAggregationResultSet aggregationRsFromCube,
			List<DrillOnDimensionHierarchy> drillFilters) throws IOException, DataException {
		return populateResultSet(aggregationRsFromCube, null, drillFilters);
	}
}
