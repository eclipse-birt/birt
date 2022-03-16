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

package org.eclipse.birt.chart.event;

/**
 * This class defines the type for a StructureSource object and provides
 * predefined constants.
 */
public class StructureType {

	/**
	 * Indicates an Unknown structure type. The relevant source is an unknown
	 * <code>Object</code> type.
	 */
	public static final StructureType UNKNOWN = new StructureType("unknown"); //$NON-NLS-1$

	/**
	 * Indicates a Chart Block structure type. The relevant source is a
	 * <code>Block</code> type.
	 */
	public static final StructureType CHART_BLOCK = new StructureType("chartBlock"); //$NON-NLS-1$

	/**
	 * Indicates a Plot structure type. The relevant source is a <code>Plot</code>
	 * type.
	 */
	public static final StructureType PLOT = new StructureType("plot"); //$NON-NLS-1$

	/**
	 * Indicates a Title structure type. The relevant source is a
	 * <code>TitleBlock</code> type.
	 */
	public static final StructureType TITLE = new StructureType("title"); //$NON-NLS-1$

	/**
	 * Indicates a Legend structure type. The relevant source is a
	 * <code>Legend</code> type.
	 */
	public static final StructureType LEGEND = new StructureType("legend"); //$NON-NLS-1$

	/**
	 * Indicates a Legend Title structure type. The relevant source is a
	 * <code>Label</code> type.
	 */
	public static final StructureType LEGEND_TITLE = new StructureType("legendTitle"); //$NON-NLS-1$

	/**
	 * Indicates a Legend Entry structure type. The relevant source is a
	 * <code>LegendEntryRenderingHints</code> type.
	 */
	public static final StructureType LEGEND_ENTRY = new StructureType("legendEntry"); //$NON-NLS-1$

	/**
	 * Indicates an Axis structure type. The relevant source is a <code>Axis</code>
	 * type.
	 */
	public static final StructureType AXIS = new StructureType("axis"); //$NON-NLS-1$

	/**
	 * Indicates an Axis Title structure type. The relevant source is a
	 * <code>Label</code> type.
	 */
	public static final StructureType AXIS_TITLE = new StructureType("axisTitle"); //$NON-NLS-1$

	/**
	 * Indicates an Axis Label structure type. The relevant source is a
	 * <code>Label</code> type.
	 */
	public static final StructureType AXIS_LABEL = new StructureType("axisLabel"); //$NON-NLS-1$

	/**
	 * Indicates a Series structure type. The relevant source is a
	 * <code>Series</code> type.
	 */
	public static final StructureType SERIES = new StructureType("series"); //$NON-NLS-1$

	/**
	 * Indicates a Series Title structure type. The relevant source is a
	 * <code>Label</code> type.
	 */
	public static final StructureType SERIES_TITLE = new StructureType("seriesTitle"); //$NON-NLS-1$

	/**
	 * Indicates a Series Element structure type. The relevant source is a
	 * <code>DataPointHints</code> type.
	 */
	public static final StructureType SERIES_ELEMENT = new StructureType("seriesElement"); //$NON-NLS-1$

	/**
	 * Indicates a Series Marker structure type. The relevant source is a
	 * <code>Marker</code> type.
	 */
	public static final StructureType SERIES_MARKER = new StructureType("seriesMarker"); //$NON-NLS-1$

	/**
	 * Indicates a Series DataPoint structure type. The relevant source is a
	 * <code>DataPointHints</code> type.
	 */
	public static final StructureType SERIES_DATA_POINT = new StructureType("seriesDataPoint"); //$NON-NLS-1$

	/**
	 * Indicates a Series Fitting Curve structure type. The relevant source is a
	 * <code>CurveFitting</code> type.
	 */
	public static final StructureType SERIES_FITTING_CURVE = new StructureType("seriesFittingCurve"); //$NON-NLS-1$

	/**
	 * Indicates a Marker Line structure type. The relevant source is a
	 * <code>MarkerLine</code> type.
	 */
	public static final StructureType MARKER_LINE = new StructureType("markerLine"); //$NON-NLS-1$

	/**
	 * Indicates a Marker Range structure type. The relevant source is a
	 * <code>MarkerRange</code> type.
	 */
	public static final StructureType MARKER_RANGE = new StructureType("markerRange"); //$NON-NLS-1$

	private String type;

	/**
	 * Prevent from instantiating.
	 *
	 * @param type
	 */
	private StructureType(String type) {
		this.type = type;
	}

	/**
	 * Returns the type string.
	 *
	 * @return
	 */
	public final String getTypeString() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StructureType[" + type + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
