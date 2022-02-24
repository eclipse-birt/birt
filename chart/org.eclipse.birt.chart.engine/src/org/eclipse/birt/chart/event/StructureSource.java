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

import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.ecore.EObject;

/**
 * This class defines a structure source object for all chart events.
 */
public class StructureSource {

	private Object source;

	private StructureType type;

	/**
	 * The constructor.
	 * 
	 * @param source
	 */
	public StructureSource(Object source) {
		super();

		this.source = source;
		this.type = StructureType.UNKNOWN;
	}

	/**
	 * The constructor.
	 * 
	 * @param source
	 */
	public StructureSource(Object source, StructureType type) {
		super();

		this.source = source;
		this.type = type;
	}

	/**
	 * Returns the source object.
	 * 
	 * @return
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Returns the source type.
	 * 
	 * @return
	 */
	public StructureType getType() {
		return type;
	}

	/**
	 * Creates a Structure Source for type Series.
	 * 
	 * @param se
	 * @return
	 */
	public static StructureSource createSeries(Series se) {
		return new StructureSource(se, StructureType.SERIES);
	}

	/**
	 * Creates a Structure Source for type Plot.
	 * 
	 * @param pt
	 * @return
	 */
	public static StructureSource createPlot(Plot pt) {
		return new StructureSource(pt, StructureType.PLOT);
	}

	/**
	 * Creates a Structure Source for type Title.
	 * 
	 * @param pt
	 * @return
	 */
	public static StructureSource createTitle(TitleBlock tb) {
		return new StructureSource(tb, StructureType.TITLE);
	}

	/**
	 * Creates a Structure Source for type Chart Block.
	 * 
	 * @param pt
	 * @return
	 */
	public static StructureSource createChartBlock(Block block) {
		return new StructureSource(block, StructureType.CHART_BLOCK);
	}

	/**
	 * Creates a Structure Source for type Unknown.
	 * 
	 * @param o
	 * @return
	 */
	public static StructureSource createUnknown(EObject o) {
		return new StructureSource(o, StructureType.UNKNOWN);
	}

	/**
	 * Creates a Structure Source for type Axis.
	 * 
	 * @param ax
	 * @return
	 */
	public static StructureSource createAxis(Axis ax) {
		return new StructureSource(ax, StructureType.AXIS);
	}

	/**
	 * Creates a Structure Source for type Legend.
	 * 
	 * @param lg
	 * @return
	 */
	public static StructureSource createLegend(Legend lg) {
		return new StructureSource(lg, StructureType.LEGEND);
	}

	/**
	 * Creates a Structure Source for type MarkerLine.
	 * 
	 * @param ml
	 * @return
	 */
	public static StructureSource createMarkerLine(MarkerLine ml) {
		return new StructureSource(ml, StructureType.MARKER_LINE);
	}

	/**
	 * Creates a Structure Source for type MarkerRange.
	 * 
	 * @param ml
	 * @return
	 */
	public static StructureSource createMarkerRange(MarkerRange mr) {
		return new StructureSource(mr, StructureType.MARKER_RANGE);
	}

}
