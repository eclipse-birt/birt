/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.DrilledInfo;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.query.view.DrillOnDimensionHierarchy;

public class DrillFilterHelper {
	/**
	 * populate drilled aggregation for nested aggregation
	 *
	 * @param cubeQueryDefinition
	 * @param cubeAggr
	 * @param aggrDefns
	 * @return
	 */
	public static AggregationDefinition[] preparedDrillForNestedAggregation(ICubeQueryDefinition cubeQueryDefinition,
			CubeAggrDefn[] cubeAggr, AggregationDefinition[] aggrDefns) {
		IEdgeDefinition columnEdge = cubeQueryDefinition.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IEdgeDefinition rowEdge = cubeQueryDefinition.getEdge(ICubeQueryDefinition.ROW_EDGE);
		List<DrillOnDimensionHierarchy> columnDrill = CubeQueryDefinitionUtil.flatternDrillFilter(columnEdge);
		List<DrillOnDimensionHierarchy> rowDrill = CubeQueryDefinitionUtil.flatternDrillFilter(rowEdge);
		List<DrillOnDimensionHierarchy> combinedDrill = new ArrayList<>(rowDrill);
		combinedDrill.addAll(columnDrill);

		if (combinedDrill.isEmpty()) {
			return new AggregationDefinition[0];
		}

		List<DrilledInfo> aggregation = new ArrayList<>();
		for (int i = 0; i < aggrDefns.length; i++) {
			if (aggrDefns[i].getAggregationFunctions() == null) {
				continue;
			}
			DimLevel[] levels = (DimLevel[]) cubeAggr[i].getAggrLevelsInAggregationResult().toArray(new DimLevel[0]);
			if (levels == null) {
				continue;
			}

			List<List<DimLevel>> groupByDimension = new ArrayList<>();
			String dimensionName = null;
			List<DimLevel> list = null;
			for (int j = 0; j < levels.length - 1; j++) {
				if (dimensionName != null && dimensionName.equals(levels[j].getDimensionName())) {
					if (isDrilledLevel(levels[j], combinedDrill)) {
						list.add(levels[j]);
					}
				} else {
					list = new ArrayList<>();
					if (isDrilledLevel(levels[j], combinedDrill)) {
						list.add(levels[j]);
					}
					dimensionName = levels[j].getDimensionName();
					groupByDimension.add(list);
				}
			}
			if (groupByDimension.isEmpty()) {
				continue;
			}

			List<DimLevel[]> tagetLevels = new ArrayList<>();
			tagetLevels.add(levels);

			buildAggregationDimLevel(tagetLevels, groupByDimension, 0);

			for (int k = 1; k < tagetLevels.size(); k++) {
				boolean exist = false;
				for (int t = 0; t < aggregation.size(); t++) {
					if (aggregation.get(t).matchTargetlevels(tagetLevels.get(k))) {
						aggregation.get(t).addOriginalAggregation(aggrDefns[i]);
						exist = true;
						break;
					}
				}
				if (exist) {
					continue;
				}
				DrilledInfo aggr = null;
				if (isRunningAggregation(aggrDefns[i])) {
					DimLevel[] aggrLevels = aggrDefns[i].getLevels();
					List<DimLevel> onList = Arrays.asList(tagetLevels.get(k));
					List<DimLevel> targetList = null;
					if (aggrLevels != null) {
						targetList = new ArrayList<>();
						for (DimLevel dimLevel : aggrLevels) {
							if (onList.contains(dimLevel)) {
								targetList.add(dimLevel);
							}
						}
					}
					aggr = new DrilledInfo(targetList == null ? null : targetList.toArray(new DimLevel[0]),
							cubeQueryDefinition);
				} else {
					aggr = new DrilledInfo(tagetLevels.get(k), cubeQueryDefinition);
				}
				aggr.addOriginalAggregation(aggrDefns[i]);
				aggregation.add(aggr);
			}
		}

		AggregationDefinition[] a = new AggregationDefinition[aggregation.size()];
		for (int i = 0; i < aggregation.size(); i++) {
			a[i] = new AggregationDefinition(aggregation.get(i).getTargetLevels(), aggregation.get(i).getSortType(),
					aggregation.get(i).getAggregationFunctionDefinition());
			a[i].setDrilledInfo(aggregation.get(i));
		}
		return a;
	}

	/**
	 * check whether there is drill operation defined in cube query
	 *
	 * @param cubeQueryDefinition
	 * @return
	 */
	public static boolean containsDrillFilter(ICubeQueryDefinition cubeQueryDefinition) {
		IEdgeDefinition columnEdge = cubeQueryDefinition.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IEdgeDefinition rowEdge = cubeQueryDefinition.getEdge(ICubeQueryDefinition.ROW_EDGE);
		List<DrillOnDimensionHierarchy> columnDrill = CubeQueryDefinitionUtil.flatternDrillFilter(columnEdge);
		List<DrillOnDimensionHierarchy> rowDrill = CubeQueryDefinitionUtil.flatternDrillFilter(rowEdge);

		if (columnDrill.isEmpty() && rowDrill.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * populate drilled aggregation for normal aggregation
	 *
	 * @param cubeQueryDefinition
	 * @param aggrDefns
	 * @return
	 */
	public static AggregationDefinition[] preparedDrillAggregation(ICubeQueryDefinition cubeQueryDefinition,
			AggregationDefinition[] aggrDefns) {
		IEdgeDefinition columnEdge = cubeQueryDefinition.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IEdgeDefinition rowEdge = cubeQueryDefinition.getEdge(ICubeQueryDefinition.ROW_EDGE);
		List<DrillOnDimensionHierarchy> columnDrill = CubeQueryDefinitionUtil.flatternDrillFilter(columnEdge);
		List<DrillOnDimensionHierarchy> rowDrill = CubeQueryDefinitionUtil.flatternDrillFilter(rowEdge);
		List<DrillOnDimensionHierarchy> combinedDrill = new ArrayList<>(rowDrill);
		combinedDrill.addAll(columnDrill);

		if (combinedDrill.isEmpty()) {
			return new AggregationDefinition[0];
		}

		List<DrilledInfo> aggregation = new ArrayList<>();
		for (int i = 0; i < aggrDefns.length; i++) {
			if (aggrDefns[i].getAggregationFunctions() == null) {
				continue;
			}
			DimLevel[] levels = aggrDefns[i].getLevels();
			if (levels == null) {
				continue;
			}

			List<List<DimLevel>> groupByDimension = new ArrayList<>();
			String dimensionName = null;
			List<DimLevel> list = null;
			for (int j = 0; j < levels.length - 1; j++) {
				if (dimensionName != null && dimensionName.equals(levels[j].getDimensionName())) {
					if (isDrilledLevel(levels[j], combinedDrill)) {
						list.add(levels[j]);
					}
				} else {
					list = new ArrayList<>();
					if (isDrilledLevel(levels[j], combinedDrill)) {
						list.add(levels[j]);
					}
					dimensionName = levels[j].getDimensionName();
					groupByDimension.add(list);
				}
			}
			if (groupByDimension.isEmpty()) {
				continue;
			}

			List<DimLevel[]> tagetLevels = new ArrayList<>();
			tagetLevels.add(levels);

			buildAggregationDimLevel(tagetLevels, groupByDimension, 0);

			for (int k = 1; k < tagetLevels.size(); k++) {
				boolean exist = false;
				for (int t = 0; t < aggregation.size(); t++) {
					if (aggregation.get(t).matchTargetlevels(tagetLevels.get(k))) {
						aggregation.get(t).addOriginalAggregation(aggrDefns[i]);
						exist = true;
						break;
					}
				}
				if (exist) {
					continue;
				}
				DrilledInfo aggr = new DrilledInfo(tagetLevels.get(k), cubeQueryDefinition);
				aggr.addOriginalAggregation(aggrDefns[i]);
				aggregation.add(aggr);
			}
		}

		AggregationDefinition[] a = new AggregationDefinition[aggregation.size()];
		for (int i = 0; i < aggregation.size(); i++) {
			a[i] = new AggregationDefinition(aggregation.get(i).getTargetLevels(), aggregation.get(i).getSortType(),
					aggregation.get(i).getAggregationFunctionDefinition());
			a[i].setDrilledInfo(aggregation.get(i));
		}
		return a;
	}

	private static boolean isRunningAggregation(AggregationDefinition defn) {
		AggregationDefinition ad = defn;
		if (ad != null) {
			AggregationFunctionDefinition[] afds = ad.getAggregationFunctions();
			if (afds != null && afds.length == 1) {
				String functionName = afds[0].getFunctionName();
				IAggrFunction af = null;
				try {
					af = AggregationManager.getInstance().getAggregation(functionName);
				} catch (DataException e) {
				}
				return af != null && af.getType() == IAggrFunction.RUNNING_AGGR;
			}
		}
		return false;
	}

	private static void buildAggregationDimLevel(List<DimLevel[]> tagetLevels, List<List<DimLevel>> groupByDimension,
			int dimIndex) {
		List<DimLevel> l = (List<DimLevel>) groupByDimension.get(dimIndex);
		List<DimLevel[]> temp = new ArrayList<>();
		for (int t = 0; t < l.size(); t++) {
			DimLevel dimLevel = l.get(t);
			for (int i = 0; i < tagetLevels.size(); i++) {
				temp.add(getDrilledDimLevel(dimLevel, tagetLevels.get(i)));
			}
		}
		tagetLevels.addAll(temp);
		dimIndex++;
		if (dimIndex < groupByDimension.size()) {
			buildAggregationDimLevel(tagetLevels, groupByDimension, dimIndex);
		}
	}

	private static DimLevel[] getDrilledDimLevel(DimLevel dimLevel, DimLevel[] levels) {
		boolean find = false;
		List<DimLevel> d = new ArrayList<>();
		for (int i = 0; i < levels.length; i++) {
			if (!dimLevel.getDimensionName().equals(levels[i].getDimensionName())) {
				d.add(levels[i]);
			} else {
				if (dimLevel.equals(levels[i])) {
					find = true;
					d.add(levels[i]);
				}
				if (!find) {
					d.add(levels[i]);
				}
			}
		}
		DimLevel[] dim = new DimLevel[d.size()];
		for (int i = 0; i < dim.length; i++) {
			dim[i] = d.get(i);
		}
		return dim;
	}

	private static boolean isDrilledLevel(DimLevel levels, List<DrillOnDimensionHierarchy> combinedDrill) {
		for (int i = 0; i < combinedDrill.size(); i++) {
			DrillOnDimensionHierarchy dim = combinedDrill.get(i);
			List<IEdgeDrillFilter> filters = dim.getDrillFilterByLevel(levels);
			if (filters != null && !filters.isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
