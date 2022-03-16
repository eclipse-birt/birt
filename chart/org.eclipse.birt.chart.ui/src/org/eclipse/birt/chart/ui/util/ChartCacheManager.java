/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;

/**
 * Cache manager for chart series, sub-type and etc.
 */

public final class ChartCacheManager {

	private static Map<String, ChartCacheManager> instances = new HashMap<>(3);

	private static String currentInstanceId = null;

	private static final String DEFAULT_INSTANCE = "default"; //$NON-NLS-1$

	private List<Map<String, Series>> cacheSeries = new ArrayList<>(3);

	private Map<String, Object> cacheCharts = new HashMap<>();

	/**
	 * The map stores Series label position, key is Chart stacked type, value is
	 * reated label position object whose class type is <code>Position</code>.
	 */
	private Map<String, Position> cacheLabelPosition = new HashMap<>();

	private static final String PREFIX_SUBTYPE = "s_"; //$NON-NLS-1$

	private static final String PREFIX_ORIENTATION = "o_"; //$NON-NLS-1$

	private static final String PREFIX_CATEGORY = "c_"; //$NON-NLS-1$

	private static final String PREFIX_SERIESTYPE = "t_"; //$NON-NLS-1$

	private static final String PREFIX_DIMENSION = "d_"; //$NON-NLS-1$

	private ChartCacheManager() {

	}

	/**
	 * Returns an instance. Must invoke <code>switchInstance(String)</code> at first
	 * to ensure returned instance is what you want, or return a default instance.
	 *
	 * @return instance
	 */
	public static ChartCacheManager getInstance() {
		if (currentInstanceId == null) {
			switchInstance(DEFAULT_INSTANCE);
		}
		return instances.get(currentInstanceId);
	}

	/**
	 * Returns a specified instance.
	 *
	 * @param instanceId Instance id.
	 * @return instance
	 */
	public static ChartCacheManager getInstance(String instanceId) {
		switchInstance(instanceId);
		return getInstance();
	}

	/**
	 * Switched the instance by passing id. If id is new, create an instance.
	 *
	 * @param instanceId Instance id.
	 */
	public static void switchInstance(String instanceId) {
		assert instanceId != null;
		currentInstanceId = instanceId;
		if (!instances.containsKey(instanceId)) {
			instances.put(instanceId, new ChartCacheManager());
		}
	}

	/**
	 * Returns a cached series.
	 *
	 * @param seriesClass Class name of series
	 * @param seriesIndex The series index in the all series definitions
	 * @return a cloned series instances of specified type. Returns null if not
	 *         found
	 */
	public Series findSeries(String seriesClass, int seriesIndex) {
		assert seriesIndex >= 0;
		while (cacheSeries.size() <= seriesIndex) {
			cacheSeries.add(new HashMap<String, Series>());
		}
		Map<String, Series> map = cacheSeries.get(seriesIndex);
		if (!map.containsKey(seriesClass)) {
			return null;
		}
		return map.get(seriesClass).copyInstance();
	}

	/**
	 * Caches a list of series. Series instance will be cloned before being stored.
	 * If the series instance is existent, replace it with the latest.
	 *
	 * @param seriesDefinitions A list of series definitions. Series types can be
	 *                          different from each other.
	 */
	public void cacheSeries(List<SeriesDefinition> seriesDefinitions) {
		for (int i = 0; i < seriesDefinitions.size(); i++) {
			Series series = seriesDefinitions.get(i).getDesignTimeSeries();
			if (cacheSeries.size() <= i) {
				cacheSeries.add(new HashMap<String, Series>());
			}
			// Clone the series instance and save it
			cacheSeries.get(i).put(series.getClass().getName(), series.copyInstance());
		}

		// Remove redundant series instances.
		removeSeries(seriesDefinitions.size());
	}

	/**
	 * Caches a series. Series instance will be cloned before being stored. If the
	 * series instance is existent, replace it with the latest.
	 *
	 * @param seriesIndex The series index in the all series definitions
	 * @param series      Series instance
	 */
	public void cacheSeries(int seriesIndex, Series series) {
		assert seriesIndex >= 0;
		while (cacheSeries.size() <= seriesIndex) {
			cacheSeries.add(new HashMap<String, Series>());
		}
		cacheSeries.get(seriesIndex).put(series.getClass().getName(), series.copyInstance());
	}

	/**
	 * Removes redundant series instances.
	 *
	 * @param seriesSize The series number of current chart model
	 */
	public void removeSeries(int seriesSize) {
		while (cacheSeries.size() > seriesSize) {
			cacheSeries.remove(seriesSize);
		}
	}

	/**
	 * Clears current instance and related resources.
	 *
	 */
	public void dispose() {
		cacheSeries.clear();
		cacheCharts.clear();
		cacheLabelPosition.clear();
		instances.remove(currentInstanceId);
		currentInstanceId = null;
	}

	/**
	 * Caches the latest selection of sub-type
	 *
	 * @param chartType Chart type
	 * @param subtype   Chart sub-type
	 */
	public void cacheSubtype(String chartType, String subtype) {
		cacheCharts.put(PREFIX_SUBTYPE + chartType, subtype);
	}

	/**
	 * Cache the latest selection of series-type
	 *
	 * @param seriesType Chart second axis series-type
	 */
	public void cacheSeriesType(String seriesType) {
		cacheCharts.put(PREFIX_SERIESTYPE, seriesType);
	}

	/**
	 * Caches the label position of specified Chart stacked type
	 *
	 * @param stackedType   Chart stacked type.
	 * @param labelPosition Series label position.
	 */
	public void cacheLabelPositionWithStackedCase(String stackedType, Position labelPosition) {
		cacheLabelPosition.put(stackedType, labelPosition);
	}

	/**
	 * Returns label position of specified Chart stacked type.
	 *
	 * @param stackedType Chart stacked type.
	 * @return value of Series label position.
	 */
	public Position findLabelPositionWithStackedCase(String stackedType) {
		return cacheLabelPosition.get(stackedType);
	}

	/**
	 * Returns the latest selection of sub-type
	 *
	 * @param chartType Chart type
	 * @return the latest selection of sub-type. Returns null if not found
	 */
	public String findSubtype(String chartType) {
		return (String) cacheCharts.get(PREFIX_SUBTYPE + chartType);
	}

	/**
	 * Returns the latest selection of series-type
	 *
	 * @return the latest selection of series-type. Returns null if not found
	 */
	public String findSeriesType() {
		return (String) cacheCharts.get(PREFIX_SERIESTYPE);
	}

	/**
	 * Caches the latest selection of orientation.
	 *
	 * @param chartType   Chart type
	 * @param orientation Chart orientation
	 */
	public void cacheOrientation(String chartType, Orientation orientation) {
		cacheCharts.put(PREFIX_ORIENTATION + chartType, orientation);
	}

	/**
	 * Returns the latest selection of orientation.
	 *
	 * @param chartType Chart type
	 * @return the latest selection of orientation. Returns null if not found
	 */
	public Orientation findOrientation(String chartType) {
		return (Orientation) cacheCharts.get(PREFIX_ORIENTATION + chartType);
	}

	/**
	 * Caches the latest selection of axis category.
	 *
	 * @param chartType Chart type
	 * @param bCategory
	 */
	public void cacheCategory(String chartType, boolean bCategory) {
		cacheCharts.put(PREFIX_CATEGORY + chartType, Boolean.valueOf(bCategory));
	}

	/**
	 * Returns the latest selection of axis category.
	 *
	 * @param chartType Chart type
	 * @return the latest selection of axis category. Returns null if not found
	 */
	public Boolean findCategory(String chartType) {
		return (Boolean) cacheCharts.get(PREFIX_CATEGORY + chartType);
	}

	/**
	 * Cache the dimension selection matching to the chart type.
	 *
	 * @param chartType Chart type
	 * @param dimension dimension
	 */
	public void cacheDimension(String chartType, String dimension) {
		cacheCharts.put(PREFIX_DIMENSION + chartType, dimension);
	}

	/**
	 * Return the latest selection according to the chart type.
	 *
	 * @param chartType chart type
	 * @return the latest selection according to the chart type.
	 */
	public String getDimension(String chartType) {
		return (String) cacheCharts.get(PREFIX_DIMENSION + chartType);
	}
}
