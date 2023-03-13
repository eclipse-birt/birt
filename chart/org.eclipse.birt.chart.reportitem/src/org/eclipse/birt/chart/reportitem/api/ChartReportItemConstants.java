/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.api;

/**
 * Constants used in Chart report item.
 */

public interface ChartReportItemConstants {

	String ID = "org.eclipse.birt.chart.reportitem"; //$NON-NLS-1$

	String EXPRESSION_SPLITTOR = "/"; //$NON-NLS-1$

	/**
	 * Specified the query expression of min aggregation binding
	 */
	String NAME_QUERY_MIN = "chart__min"; //$NON-NLS-1$

	/**
	 * Specified the query expression of max aggregation binding
	 */
	String NAME_QUERY_MAX = "chart__max"; //$NON-NLS-1$

	String NAME_SUBQUERY = "chart_subquery"; //$NON-NLS-1$

	String CHART_EXTENSION_NAME = "Chart";//$NON-NLS-1$

	/**
	 * Specified property names defined in ExtendedItemHandle or IReportItem
	 */
	String PROPERTY_XMLPRESENTATION = "xmlRepresentation"; //$NON-NLS-1$
	String PROPERTY_CHART = "chart.instance"; //$NON-NLS-1$
	String PROPERTY_SCALE = "chart.scale"; //$NON-NLS-1$
	String PROPERTY_SCRIPT = "script"; //$NON-NLS-1$
	String PROPERTY_ONRENDER = "onRender"; //$NON-NLS-1$
	String PROPERTY_OUTPUT = "outputFormat"; //$NON-NLS-1$
	String PROPERTY_CUBE_FILTER = "cubeFilter";//$NON-NLS-1$
	String PROPERTY_HOST_CHART = "hostChart";//$NON-NLS-1$
	String PROPERTY_INHERIT_COLUMNS = "inheritColumns";//$NON-NLS-1$
	String EXTENDED_PROPERTY_HIERARCHY_CATEGORY = "keep.hierarchy.category"; //$NON-NLS-1$
	String EXTENDED_PROPERTY_HIERARCHY_SERIES = "keep.hierarchy.series";//$NON-NLS-1$

	String PROPERTY_OUTPUT_FORMAT_PNG = "PNG"; //$NON-NLS-1$
	String PROPERTY_OUTPUT_FORMAT_JPG = "JPG";//$NON-NLS-1$
	String PROPERTY_OUTPUT_FORMAT_BMP = "BMP";//$NON-NLS-1$
	String PROPERTY_OUTPUT_FORMAT_SVG = "SVG";//$NON-NLS-1$

	/**
	 * Specifies the chart type in xtab
	 *
	 * @see #TYPE_PLOT_CHART
	 * @see #TYPE_AXIS_CHART
	 */
	String PROPERTY_CHART_TYPE = "chartType";//$NON-NLS-1$

	/**
	 * Specifies all chart types which could be in <code>PROPERTY_CHART_TYPE</code>
	 * property of handle
	 *
	 * @see #PROPERTY_CHART_TYPE
	 */
	String TYPE_PLOT_CHART = "plotChart";//$NON-NLS-1$
	String TYPE_AXIS_CHART = "axisChart";//$NON-NLS-1$

	double DEFAULT_CHART_BLOCK_HEIGHT = 130;
	double DEFAULT_CHART_BLOCK_WIDTH = 212;
	double DEFAULT_AXIS_CHART_BLOCK_SIZE = 22;
}
