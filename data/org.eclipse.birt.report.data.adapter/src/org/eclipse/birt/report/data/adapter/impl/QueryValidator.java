/**
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.ITimePeriod;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;

public class QueryValidator {

	/**
	 * 
	 * @param query
	 * @param cubeHandle
	 * @throws DataException
	 * @throws AdapterException
	 */
	public static void validateTimeFunctionInCubeQuery(ICubeQueryDefinition query, CubeHandle cubeHandle)
			throws DataException, AdapterException {
		if (cubeHandle == null)
			return;

		Map<String, IDimensionDefinition> dimensionMap = new HashMap<String, IDimensionDefinition>();
		IEdgeDefinition columnEdge = query.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		if (columnEdge != null) {
			List<IDimensionDefinition> dimensions = columnEdge.getDimensions();
			for (int i = 0; i < dimensions.size(); i++) {
				dimensionMap.put(dimensions.get(i).getName(), dimensions.get(i));
			}
		}

		IEdgeDefinition rowEdge = query.getEdge(ICubeQueryDefinition.ROW_EDGE);
		if (rowEdge != null) {
			List<IDimensionDefinition> dimensions = rowEdge.getDimensions();
			for (int i = 0; i < dimensions.size(); i++) {
				dimensionMap.put(dimensions.get(i).getName(), dimensions.get(i));
			}
		}

		IEdgeDefinition pageEdge = query.getEdge(ICubeQueryDefinition.PAGE_EDGE);
		if (pageEdge != null) {
			List<IDimensionDefinition> dimensions = pageEdge.getDimensions();
			for (int i = 0; i < dimensions.size(); i++) {
				dimensionMap.put(dimensions.get(i).getName(), dimensions.get(i));
			}
		}

		List bindingList = query.getBindings();
		for (int i = 0; i < bindingList.size(); i++) {
			IBinding binding = (IBinding) bindingList.get(i);
			if (binding.getTimeFunction() != null) {
				String dimensionName = binding.getTimeFunction().getTimeDimension();
				QueryValidator.validateTimeFunction(cubeHandle, binding.getTimeFunction());
				if (dimensionMap.containsKey(dimensionName) && binding.getTimeFunction().getReferenceDate() == null) {
					QueryValidator.validateTimeFunction(dimensionMap.get(dimensionName), cubeHandle,
							binding.getTimeFunction());
				}
			}
		}
	}

	/**
	 * time dimension used in time function is not in xTab
	 * 
	 * @param cube
	 * @param function
	 * @return
	 * @throws DataException
	 * @throws AdapterException
	 */
	private static void validateTimeFunction(CubeHandle cube, ITimeFunction function)
			throws DataException, AdapterException {
		if (cube != null) {
			String dimensionName = function.getTimeDimension();
			ITimePeriod basePeriod = function.getBaseTimePeriod();
			ITimePeriod relativePeriod = function.getRelativeTimePeriod();

			DimensionHandle handle = cube.getDimension(dimensionName);

			if (handle == null) {
				throw new AdapterException(ResourceConstants.CUBE_QUERY_MISS_DIMENSION, new Object[] { dimensionName });
			}
			TabularHierarchyHandle hierhandle = (TabularHierarchyHandle) handle.getDefaultHierarchy();
			List levels = hierhandle.getContents(TabularHierarchyHandle.LEVELS_PROP);
			List<String> levelTypes = new ArrayList<String>();

			for (int i = 0; i < levels.size(); i++) {
				TabularLevelHandle level = (TabularLevelHandle) levels.get(i);
				levelTypes.add(level.getDateTimeLevelType());
			}

			if (basePeriod != null) {
				String timeType = DataAdapterUtil.toModelTimeType(basePeriod.getType());
				if (!levelTypes.contains(timeType)) {
					throw new AdapterException(ResourceConstants.CUBE_QUERY_MISS_LEVEL,
							new Object[] { timeType, dimensionName });
				}
			}
			if (relativePeriod != null) {
				String timeType = DataAdapterUtil.toModelTimeType(relativePeriod.getType());
				if (!levelTypes.contains(timeType)) {
					throw new AdapterException(ResourceConstants.CUBE_QUERY_MISS_LEVEL,
							new Object[] { timeType, dimensionName });
				}
			}
		}
	}

	/**
	 * time dimension used in time function is in xTab
	 * 
	 * @param cube
	 * @param function
	 * @return
	 * @throws AdapterException
	 * @throws DataException
	 */
	private static void validateTimeFunction(IDimensionDefinition timeDimension, CubeHandle cubeHandle,
			ITimeFunction function) throws AdapterException, DataException {
		List<String> levelTypes = new ArrayList<String>();

		if (cubeHandle != null) {
			String dimensionName = function.getTimeDimension();
			ITimePeriod basePeriod = function.getBaseTimePeriod();
			ITimePeriod relativePeriod = function.getRelativeTimePeriod();

			DimensionHandle handle = cubeHandle.getDimension(dimensionName);

			if (handle == null) {
				throw new AdapterException(ResourceConstants.CUBE_QUERY_MISS_DIMENSION, new Object[] { dimensionName });
			}
			TabularHierarchyHandle hierhandle = (TabularHierarchyHandle) handle.getDefaultHierarchy();
			List levels = hierhandle.getContents(TabularHierarchyHandle.LEVELS_PROP);

			List<ILevelDefinition> queryLevels = timeDimension.getHierarchy().get(0).getLevels();
			String mostDetailedLevel = queryLevels.get(queryLevels.size() - 1).getName();

			for (int i = 0; i < levels.size(); i++) {
				TabularLevelHandle level = (TabularLevelHandle) levels.get(i);
				if (mostDetailedLevel != null && level.getName().equals(mostDetailedLevel)) {
					levelTypes.add(level.getDateTimeLevelType());
					break;
				} else {
					levelTypes.add(level.getDateTimeLevelType());
				}
			}

			if (basePeriod != null) {
				String timeType = DataAdapterUtil.toModelTimeType(basePeriod.getType());
				if (!levelTypes.contains(timeType)) {
					throw new AdapterException(ResourceConstants.MISS_TIME_TYPE_LEVEL, new Object[] { timeType });
				}
			}
			if (relativePeriod != null) {
				String timeType = DataAdapterUtil.toModelTimeType(relativePeriod.getType());
				if (!levelTypes.contains(timeType)) {
					throw new AdapterException(ResourceConstants.MISS_TIME_TYPE_LEVEL, new Object[] { timeType });
				}
			}
		}
	}
}
