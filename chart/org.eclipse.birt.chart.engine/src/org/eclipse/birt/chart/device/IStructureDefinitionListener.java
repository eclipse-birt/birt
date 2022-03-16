/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.event.StructureChangeEvent;
import org.eclipse.birt.chart.script.ScriptHandler;

/**
 * Notifies a listener of group change events associated with a structure being
 * rendered.
 */
public interface IStructureDefinitionListener {
	/**
	 * Comment for <code>BEFORE_DRAW_BLOCK</code>
	 */
	String BEFORE_DRAW_BLOCK = ScriptHandler.BEFORE_DRAW_BLOCK;

	/**
	 * Comment for <code>AFTER_DRAW_BLOCK</code>
	 */
	String AFTER_DRAW_BLOCK = ScriptHandler.AFTER_DRAW_BLOCK;

	/**
	 * Comment for <code>BEFORE_DRAW_LEGEND_ENTRY</code>
	 */
	String BEFORE_DRAW_LEGEND_ENTRY = ScriptHandler.BEFORE_DRAW_LEGEND_ENTRY;

	/**
	 * Comment for <code>AFTER_DRAW_LEGEND_ENTRY</code>
	 */
	String AFTER_DRAW_LEGEND_ENTRY = ScriptHandler.AFTER_DRAW_LEGEND_ENTRY;

	/**
	 * Comment for <code>BEFORE_DRAW_LEGEND_ITEM</code>
	 */
	String BEFORE_DRAW_LEGEND_ITEM = ScriptHandler.BEFORE_DRAW_LEGEND_ITEM;

	/**
	 * Comment for <code>AFTER_DRAW_LEGEND_ITEM</code>
	 */
	String AFTER_DRAW_LEGEND_ITEM = ScriptHandler.AFTER_DRAW_LEGEND_ITEM;

	/**
	 * Comment for <code>BEFORE_DRAW_SERIES</code>
	 */
	String BEFORE_DRAW_SERIES = ScriptHandler.BEFORE_DRAW_SERIES;

	/**
	 * Comment for <code>AFTER_DRAW_SERIES</code>
	 */
	String AFTER_DRAW_SERIES = ScriptHandler.AFTER_DRAW_SERIES;

	/**
	 * Comment for <code>BEFORE_DRAW_SERIES_TITLE</code>
	 */
	String BEFORE_DRAW_SERIES_TITLE = ScriptHandler.BEFORE_DRAW_SERIES_TITLE;

	/**
	 * Comment for <code>AFTER_DRAW_SERIES_TITLE</code>
	 */
	String AFTER_DRAW_SERIES_TITLE = ScriptHandler.AFTER_DRAW_SERIES_TITLE;

	/**
	 * Comment for <code>BEFORE_DRAW_MARKER</code>
	 */
	String BEFORE_DRAW_MARKER = ScriptHandler.BEFORE_DRAW_MARKER;

	/**
	 * Comment for <code>AFTER_DRAW_MARKER</code>
	 */
	String AFTER_DRAW_MARKER = ScriptHandler.AFTER_DRAW_MARKER;

	/**
	 * Comment for <code>BEFORE_DRAW_MARKER_LINE</code>
	 */
	String BEFORE_DRAW_MARKER_LINE = ScriptHandler.BEFORE_DRAW_MARKER_LINE;

	/**
	 * Comment for <code>AFTER_DRAW_MARKER_LINE</code>
	 */
	String AFTER_DRAW_MARKER_LINE = ScriptHandler.AFTER_DRAW_MARKER_LINE;

	/**
	 * Comment for <code>BEFORE_DRAW_MARKER_RANGE</code>
	 */
	String BEFORE_DRAW_MARKER_RANGE = ScriptHandler.BEFORE_DRAW_MARKER_RANGE;

	/**
	 * Comment for <code>AFTER_DRAW_MARKER_RANGE</code>
	 */
	String AFTER_DRAW_MARKER_RANGE = ScriptHandler.AFTER_DRAW_MARKER_RANGE;

	/**
	 * Comment for <code>BEFORE_DRAW_ELEMENT</code>
	 *
	 * @deprecated Not used anymore. This is kept just for backward compatibility.
	 */
	@Deprecated
	String BEFORE_DRAW_ELEMENT = ScriptHandler.BEFORE_DRAW_ELEMENT;

	/**
	 * Comment for <code>AFTER_DRAW_ELEMENT</code>
	 *
	 * @deprecated Not used anymore. This is kept just for backward compatibility.
	 */
	@Deprecated
	String AFTER_DRAW_ELEMENT = ScriptHandler.AFTER_DRAW_ELEMENT;

	/**
	 * Comment for <code>BEFORE_DRAW_FITTING_CURVE</code>
	 */
	String BEFORE_DRAW_FITTING_CURVE = ScriptHandler.BEFORE_DRAW_FITTING_CURVE;

	/**
	 * Comment for <code>AFTER_DRAW_FITTING_CURVE</code>
	 */
	String AFTER_DRAW_FITTING_CURVE = ScriptHandler.AFTER_DRAW_FITTING_CURVE;

	/**
	 * Comment for <code>BEFORE_DRAW_DATA_POINT</code>
	 */
	String BEFORE_DRAW_DATA_POINT = ScriptHandler.BEFORE_DRAW_DATA_POINT;

	/**
	 * Comment for <code>AFTER_DRAW_DATA_POINT</code>
	 */
	String AFTER_DRAW_DATA_POINT = ScriptHandler.AFTER_DRAW_DATA_POINT;

	/**
	 * Comment for <code>BEFORE_DRAW_DATA_POINT_LABEL</code>
	 */
	String BEFORE_DRAW_DATA_POINT_LABEL = ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL;

	/**
	 * Comment for <code>AFTER_DRAW_DATA_POINT_LABEL</code>
	 */
	String AFTER_DRAW_DATA_POINT_LABEL = ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL;

	/**
	 * Comment for <code>BEFORE_DRAW_AXIS_LABEL</code>
	 */
	String BEFORE_DRAW_AXIS_LABEL = ScriptHandler.BEFORE_DRAW_AXIS_LABEL;

	/**
	 * Comment for <code>AFTER_DRAW_AXIS_LABEL</code>
	 */
	String AFTER_DRAW_AXIS_LABEL = ScriptHandler.AFTER_DRAW_AXIS_LABEL;

	/**
	 * Comment for <code>BEFORE_DRAW_AXIS_TITLE</code>
	 */
	String BEFORE_DRAW_AXIS_TITLE = ScriptHandler.BEFORE_DRAW_AXIS_TITLE;

	/**
	 * Comment for <code>AFTER_DRAW_AXIS_TITLE</code>
	 */
	String AFTER_DRAW_AXIS_TITLE = ScriptHandler.AFTER_DRAW_AXIS_TITLE;

	/**
	 * Sends out a notification to a listener indicating that a structure group has
	 * changed (either via a start or end) notification.
	 *
	 * @param scev Encapsulated information associated with the structure change
	 *             notification that identifies the source object being changed.
	 */
	void changeStructure(StructureChangeEvent scev);
}
