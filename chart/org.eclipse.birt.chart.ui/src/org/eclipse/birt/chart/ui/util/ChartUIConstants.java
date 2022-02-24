/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

/**
 * Constants used in UI
 */

public interface ChartUIConstants {
	public static final String IMAGE_TASK_TYPE = "icons/obj16/selecttype.gif";//$NON-NLS-1$
	public static final String IMAGE_TASK_DATA = "icons/obj16/selectdata.gif";//$NON-NLS-1$
	public static final String IMAGE_TASK_FORMAT = "icons/obj16/selectformat.gif";//$NON-NLS-1$

	public static final String IMAGE_RA_BOTTOMLEFT = "icons/obj16/ra_bottomleft.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_BOTTOMRIGHT = "icons/obj16/ra_bottomright.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_LEFTUP = "icons/obj16/ra_leftup.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_LEFTDOWN = "icons/obj16/ra_leftright.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_RIGHTUP = "icons/obj16/ra_rightup.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_RIGHTDOWN = "icons/obj16/ra_rightdown.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_TOPLEFT = "icons/obj16/ra_topleft.gif"; //$NON-NLS-1$
	public static final String IMAGE_RA_TOPRIGHT = "icons/obj16/ra_topright.gif"; //$NON-NLS-1$

	public static final String IMAGE_DELETE = "icons/obj16/delete_edit.gif"; //$NON-NLS-1$
	public static final String IMAGE_SIGMA = "icons/obj16/sigma.gif"; //$NON-NLS-1$

	// Icons in Outline view
	public static final String IMAGE_OUTLINE = "icons/obj16/outline.gif"; //$NON-NLS-1$
	public static final String IMAGE_OUTLINE_LIB = "icons/obj16/outline_lib.gif"; //$NON-NLS-1$
	public static final String IMAGE_OUTLINE_ERROR = "icons/obj16/outline_error.gif"; //$NON-NLS-1$

	// Constants for position scope
	public static final int ALLOW_ABOVE_POSITION = 1;
	public static final int ALLOW_BELOW_POSITION = 2;
	public static final int ALLOW_LEFT_POSITION = 4;
	public static final int ALLOW_RIGHT_POSITION = 8;
	public static final int ALLOW_IN_POSITION = 16;
	public static final int ALLOW_OUT_POSITION = 32;
	public static final int ALLOW_VERTICAL_POSITION = ALLOW_ABOVE_POSITION | ALLOW_BELOW_POSITION;
	public static final int ALLOW_HORIZONTAL_POSITION = ALLOW_LEFT_POSITION | ALLOW_RIGHT_POSITION;
	public static final int ALLOW_INOUT_POSITION = ALLOW_IN_POSITION | ALLOW_OUT_POSITION;
	public static final int ALLOW_ALL_POSITION = ALLOW_VERTICAL_POSITION | ALLOW_HORIZONTAL_POSITION
			| ALLOW_INOUT_POSITION;

	public static final String NON_STACKED_TYPE = "non-stacked"; //$NON-NLS-1$
	public static final String STACKED_TYPE = "stacked"; //$NON-NLS-1$

	// Constants of query type in chart
	public static final String QUERY_CATEGORY = "category"; //$NON-NLS-1$
	public static final String QUERY_VALUE = "value"; //$NON-NLS-1$
	public static final String QUERY_OPTIONAL = "optional"; //$NON-NLS-1$

	// Constants for subtask ID.
	public static final String SUBTASK_SERIES = "Series"; //$NON-NLS-1$
	public static final String SUBTASK_SERIES_Y = "Series.Y Series"; //$NON-NLS-1$
	public static final String SUBTASK_SERIES_CATEGORY = "Series.Category Series"; //$NON-NLS-1$
	public static final String SUBTASK_SERIES_VALUE = "Series.Value Series"; //$NON-NLS-1$
	public static final String SUBTASK_SERIES_NEEDLE = "Series.Value Series.Needle"; //$NON-NLS-1$
	public static final String SUBTASK_CHART_AREA = "Chart"; //$NON-NLS-1$
	public static final String SUBTASK_AXIS = "Chart.Axis"; //$NON-NLS-1$
	public static final String SUBTASK_AXIS_X = "Chart.Axis.X Axis"; //$NON-NLS-1$
	public static final String SUBTASK_AXIS_Y = "Chart.Axis.Y Axis"; //$NON-NLS-1$
	public static final String SUBTASK_AXIS_Z = "Chart.Axis.Z Axis"; //$NON-NLS-1$
	public static final String SUBTASK_TITLE = "Chart.Title"; //$NON-NLS-1$
	public static final String SUBTASK_PLOT = "Chart.Plot"; //$NON-NLS-1$
	public static final String SUBTASK_LEGEND = "Chart.Legend"; //$NON-NLS-1$

	// Constants for toggle button ID. These IDs should be used by prefixing
	// subtask's node path
	public static final String BUTTON_OUTLINE = ".Outline"; //$NON-NLS-1$
	public static final String BUTTON_GERNERAL = ".General"; //$NON-NLS-1$
	public static final String BUTTON_CUSTOM = ".Custom"; //$NON-NLS-1$
	public static final String BUTTON_TEXT = ".Text"; //$NON-NLS-1$
	public static final String BUTTON_LAYOUT = ".Layout"; //$NON-NLS-1$
	public static final String BUTTON_INTERACTIVITY = ".Interactivity"; //$NON-NLS-1$
	public static final String BUTTON_AREA_FORMAT = ".Area"; //$NON-NLS-1$
	public static final String BUTTON_TITLE = ".Title"; //$NON-NLS-1$
	public static final String BUTTON_ENTRIES = ".Entries"; //$NON-NLS-1$
	public static final String BUTTON_PALETTE = ".Palette"; //$NON-NLS-1$
	public static final String BUTTON_LABEL = ".Label"; //$NON-NLS-1$
	public static final String BUTTON_CURVE = ".Curve"; //$NON-NLS-1$
	public static final String BUTTON_DIAL_LABELS = ".DialLables"; //$NON-NLS-1$
	public static final String BUTTON_NEEDLES = ".Needles"; //$NON-NLS-1$
	public static final String BUTTON_REGIONS = ".Regions"; //$NON-NLS-1$
	public static final String BUTTON_TICKS = ".Ticks"; //$NON-NLS-1$
	public static final String BUTTON_SCALE = ".Scale"; //$NON-NLS-1$
	public static final String BUTTON_GRIDLINES = ".Gridlines"; //$NON-NLS-1$
	public static final String BUTTON_MARKERS = ".Markers"; //$NON-NLS-1$
	public static final String BUTTON_POSITIVE_MARKERS = ".Positive"; //$NON-NLS-1$
	public static final String BUTTON_NEGATIVE_MARKERS = ".Negative"; //$NON-NLS-1$
	public static final String BUTTON_DECORATION = ".Decoration"; //$NON-NLS-1$
	public static final String BUTTON_VISIBILITY = ".Visibility"; //$NON-NLS-1$

	// Constants for Chart types
	public static final String TYPE_BAR = "Bar Chart"; //$NON-NLS-1$
	public static final String TYPE_PYRAMID = "Pyramid Chart"; //$NON-NLS-1$
	public static final String TYPE_TUBE = "Tube Chart"; //$NON-NLS-1$
	public static final String TYPE_CONE = "Cone Chart"; //$NON-NLS-1$
	public static final String TYPE_LINE = "Line Chart"; //$NON-NLS-1$
	public static final String TYPE_AREA = "Area Chart"; //$NON-NLS-1$
	public static final String TYPE_SCATTER = "Scatter Chart"; //$NON-NLS-1$
	public static final String TYPE_STOCK = "Stock Chart"; //$NON-NLS-1$
	public static final String TYPE_BUBBLE = "Bubble Chart"; //$NON-NLS-1$
	public static final String TYPE_DIFFERENCE = "Difference Chart"; //$NON-NLS-1$
	public static final String TYPE_GANTT = "Gantt Chart"; //$NON-NLS-1$
	public static final String TYPE_PIE = "Pie Chart"; //$NON-NLS-1$
	public static final String TYPE_METER = "Meter Chart"; //$NON-NLS-1$

	public static final String COPY_SERIES_DEFINITION = "CopySeriesDefinition"; //$NON-NLS-1$

	public static final String UPDATE_CUBE_BINDINGS = "UpdateCubeBindings"; //$NON-NLS-1$

	public static final String UPDATE_MODEL = "UpdateModel"; //$NON-NLS-1$
}
