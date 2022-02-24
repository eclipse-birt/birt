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

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.layout.Legend;

/**
 * This class defines a wrapped structure source object which could have a
 * parent source for all chart events.
 */
public class WrappedStructureSource extends StructureSource {

	private StructureSource parent = null;

	/**
	 * The constructor.
	 * 
	 * @param source
	 */
	public WrappedStructureSource(Object source) {
		super(source);
	}

	/**
	 * The constructor.
	 * 
	 * @param source
	 */
	public WrappedStructureSource(Object source, StructureType type) {
		super(source, type);
	}

	/**
	 * The constructor.
	 * 
	 * @param parent
	 * @param source
	 */
	public WrappedStructureSource(StructureSource parent, Object source, StructureType type) {
		super(source, type);

		this.parent = parent;
	}

	/**
	 * The constructor.
	 * 
	 * @param parentSource
	 * @param parentType
	 * @param source
	 * @param type
	 */
	public WrappedStructureSource(Object parentSource, StructureType parentType, Object source, StructureType type) {
		super(source, type);

		this.parent = new StructureSource(parentSource, parentType);
	}

	/**
	 * Returns the parent object.
	 * 
	 * @return
	 */
	public StructureSource getParent() {
		return parent;
	}

	/**
	 * Creates a WrappedStructure Source for type Legend Title with a Legend parent.
	 * 
	 * @param lg
	 * @param title
	 * @return
	 */
	public static StructureSource createLegendTitle(Legend lg, Label title) {
		return new WrappedStructureSource(lg, StructureType.LEGEND, title, StructureType.LEGEND_TITLE);
	}

	/**
	 * Creates a WrappedStructure Source for type Legend Entry with a Legend parent.
	 * 
	 * @param lg
	 * @param title
	 * @return
	 */
	public static StructureSource createLegendEntry(Legend lg, LegendItemHints entry) {
		return new WrappedStructureSource(lg, StructureType.LEGEND, entry, StructureType.LEGEND_ENTRY);
	}

	/**
	 * Creates a WrappedStructure Source for type Axis Title with an Axis parent.
	 * 
	 * @param ax
	 * @param title
	 * @return
	 */
	public static StructureSource createAxisTitle(Axis ax, Label title) {
		return new WrappedStructureSource(ax, StructureType.AXIS, title, StructureType.AXIS_TITLE);
	}

	/**
	 * Creates a WrappedStructure Source for type Axis Label with an Axis parent.
	 * 
	 * @param ax
	 * @param title
	 * @return
	 */
	public static StructureSource createAxisLabel(Axis ax, Label lb) {
		return new WrappedStructureSource(ax, StructureType.AXIS, lb.getCaption().getValue(), StructureType.AXIS_LABEL);
	}

	/**
	 * Creates a WrappedStructure Source for type Series DataPoint with a Series
	 * parent.
	 * 
	 * @param se
	 * @param dph
	 * @return
	 */
	public static StructureSource createSeriesDataPoint(Series se, DataPointHints dph) {
		return new WrappedStructureSource(se, StructureType.SERIES, dph, StructureType.SERIES_DATA_POINT);
	}

	/**
	 * Creates a WrappedStructure Source for type Series Element with a Series
	 * parent.
	 * 
	 * @param se
	 * @param dph
	 * @return
	 */
	public static StructureSource createSeriesDataElement(Series se, DataPointHints dph) {
		return new WrappedStructureSource(se, StructureType.SERIES, dph, StructureType.SERIES_ELEMENT);
	}

	/**
	 * Creates a WrappedStructure Source for type Series Title with a Series parent.
	 * 
	 * @param se
	 * @param title
	 * @return
	 */
	public static StructureSource createSeriesTitle(Series se, Label title) {
		return new WrappedStructureSource(se, StructureType.SERIES, title, StructureType.SERIES_TITLE);
	}

	/**
	 * Creates a WrappedStructure Source for type Series Marker with a Series
	 * parent.
	 * 
	 * @param se
	 * @param dph
	 * @return
	 */
	public static StructureSource createSeriesMarker(Series se, Marker mk) {
		return new WrappedStructureSource(se, StructureType.SERIES, mk, StructureType.SERIES_MARKER);
	}

	/**
	 * Creates a WrappedStructure Source for type Series FittingCurve with a Series
	 * parent.
	 * 
	 * @param se
	 * @param dph
	 * @return
	 */
	public static StructureSource createSeriesFittingCurve(Series se, CurveFitting cf) {
		return new WrappedStructureSource(se, StructureType.SERIES, cf, StructureType.SERIES_FITTING_CURVE);
	}

}
