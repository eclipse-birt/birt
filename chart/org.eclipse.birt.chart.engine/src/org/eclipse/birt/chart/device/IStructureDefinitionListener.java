/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public interface IStructureDefinitionListener
{

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_BLOCK = ScriptHandler.BEFORE_DRAW_BLOCK;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_BLOCK = ScriptHandler.AFTER_DRAW_BLOCK;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_LEGEND_ENTRY = ScriptHandler.BEFORE_DRAW_LEGEND_ENTRY;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_LEGEND_ENTRY = ScriptHandler.AFTER_DRAW_LEGEND_ENTRY;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_SERIES = ScriptHandler.BEFORE_DRAW_SERIES;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_SERIES = ScriptHandler.AFTER_DRAW_SERIES;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_SERIES_TITLE = ScriptHandler.BEFORE_DRAW_SERIES_TITLE;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_SERIES_TITLE = ScriptHandler.AFTER_DRAW_SERIES_TITLE;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_MARKER_LINE = ScriptHandler.BEFORE_DRAW_MARKER_LINE;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_MARKER_LINE = ScriptHandler.AFTER_DRAW_MARKER_LINE;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_MARKER_RANGE = ScriptHandler.BEFORE_DRAW_MARKER_RANGE;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_MARKER_RANGE = ScriptHandler.AFTER_DRAW_MARKER_RANGE;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_ELEMENT = ScriptHandler.BEFORE_DRAW_ELEMENT;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_ELEMENT = ScriptHandler.AFTER_DRAW_ELEMENT;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_FITTING_CURVE = ScriptHandler.BEFORE_DRAW_FITTING_CURVE;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_FITTING_CURVE = ScriptHandler.AFTER_DRAW_FITTING_CURVE;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_DATA_POINT = ScriptHandler.BEFORE_DRAW_DATA_POINT;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_DATA_POINT = ScriptHandler.AFTER_DRAW_DATA_POINT;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_DATA_POINT_LABEL = ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_DATA_POINT_LABEL = ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_AXIS_LABEL = ScriptHandler.BEFORE_DRAW_AXIS_LABEL;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_AXIS_LABEL = ScriptHandler.AFTER_DRAW_AXIS_LABEL;

	/**
	 * 
	 */
	public static final String BEFORE_DRAW_AXIS_TITLE = ScriptHandler.BEFORE_DRAW_AXIS_TITLE;

	/**
	 * 
	 */
	public static final String AFTER_DRAW_AXIS_TITLE = ScriptHandler.AFTER_DRAW_AXIS_TITLE;

	/**
	 * Sends out a notification to a listener indicating that a structure group
	 * has changed (either via a start or end) notification.
	 * 
	 * @param scev
	 *            Encapsulated information associated with the structure change
	 *            notification that identifies the source object being changed.
	 */
	public void changeStructure( StructureChangeEvent scev );
}
