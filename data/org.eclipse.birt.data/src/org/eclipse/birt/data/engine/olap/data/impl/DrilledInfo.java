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
package org.eclipse.birt.data.engine.olap.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;

/**
 * This class is to save the drilled aggregation info, such as target levels,
 * related normal aggregation etc.
 * 
 */
public class DrilledInfo {

	private int[] sortType;
	private List<AggregationDefinition> originalAggregationList;
	private DimLevel[] targetLevels;
	private ICubeQueryDefinition cubeQueryDefinition;

	public DrilledInfo(DimLevel[] targetLevels, ICubeQueryDefinition cubeQueryDefinition) {
		this.targetLevels = targetLevels;
		this.cubeQueryDefinition = cubeQueryDefinition;
		originalAggregationList = new ArrayList<AggregationDefinition>();

		if (targetLevels == null) {
			sortType = new int[0];
			return;
		}
		sortType = new int[targetLevels.length];
		for (int index = 0; index < targetLevels.length; index++) {
			try {
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection(targetLevels[index], cubeQueryDefinition);
			} catch (DataException e) {
				sortType[index] = IDimensionSortDefn.SORT_UNDEFINED;
			}
		}
	}

	public void addOriginalAggregation(AggregationDefinition aggregation) {
		originalAggregationList.add(aggregation);
	}

	public List<AggregationDefinition> getOriginalAggregation() {
		return this.originalAggregationList;
	}

	public boolean usedByAggregation(AggregationDefinition aggregation) {
		Set aggregtionFunctionNames = new HashSet();
		AggregationFunctionDefinition[] functions = aggregation.getAggregationFunctions();
		for (AggregationFunctionDefinition defn : functions) {
			aggregtionFunctionNames.add(defn.getName());
		}

		for (int i = 0; i < this.originalAggregationList.size(); i++) {
			AggregationFunctionDefinition[] functions1 = this.originalAggregationList.get(i).getAggregationFunctions();

			for (AggregationFunctionDefinition defn : functions1) {
				if (aggregtionFunctionNames.contains(defn.getName())) {
					return true;
				}
			}
		}
		return this.originalAggregationList.contains(aggregation);
	}

	public boolean matchTargetlevels(DimLevel[] levels) {
		if (levels == targetLevels)
			return true;
		if (targetLevels == null)
			return false;
		if (levels == null)
			return false;
		if (targetLevels.length != levels.length)
			return false;
		for (int i = 0; i < targetLevels.length; i++) {
			if (!targetLevels[i].equals(levels[i]))
				return false;
		}
		return true;
	}

	public DimLevel[] getTargetLevels() {
		return this.targetLevels;
	}

	public int[] getSortType() {
		return this.sortType;
	}

	/**
	 * Get the drilled AggregationFunctionDefinition on this target dimlevel
	 * 
	 * @return
	 */
	public AggregationFunctionDefinition[] getAggregationFunctionDefinition() {
		Map<String, AggregationFunctionDefinition> functionMap = new HashMap<String, AggregationFunctionDefinition>();
		for (int i = 0; i < this.originalAggregationList.size(); i++) {
			AggregationDefinition aggr = originalAggregationList.get(i);
			for (int j = 0; j < aggr.getAggregationFunctions().length; j++) {
				if (!functionMap.containsKey(aggr.getAggregationFunctions()[j].getName()))
					functionMap.put(aggr.getAggregationFunctions()[j].getName(), aggr.getAggregationFunctions()[j]);
			}
		}
		Iterator<Entry<String, AggregationFunctionDefinition>> iter = functionMap.entrySet().iterator();
		AggregationFunctionDefinition[] aggr = new AggregationFunctionDefinition[functionMap.size()];
		int index = 0;
		while (iter.hasNext()) {
			aggr[index] = iter.next().getValue();
			index++;
		}
		return aggr;
	}

	/**
	 * 
	 * @return
	 */
	public DrilledInfo copy() {
		DrilledInfo drilledInfo = new DrilledInfo(this.targetLevels, this.cubeQueryDefinition);
		for (int i = 0; i < this.originalAggregationList.size(); i++) {
			drilledInfo.addOriginalAggregation(this.originalAggregationList.get(i));
		}
		return drilledInfo;
	}

}
