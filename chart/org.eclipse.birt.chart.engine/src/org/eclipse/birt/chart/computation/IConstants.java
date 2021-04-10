/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.engine.i18n.Messages;

/**
 * Defines all constants here.
 */
public interface IConstants {

	public final double LOG_10 = Math.log(10);

	public final int LINE_EXPAND_SIZE = 2;

	public final int LINE_EXPAND_DOUBLE_SIZE = 4;

	// The old size was 5, but was used without dpi consideration (so as in 72 dpi)
	// When the dpi issue was fixed, charts in 96dpi had bigger ticks than before,
	// So the size has been adjusted to a 72/96 ratio.
	public final double TICK_SIZE = 3.75;

	public final int TICK_SIDE1 = 1;

	public final int TICK_SIDE2 = 2;

	public final int TICK_NONE = 0;

	public final int TICK_LEFT = TICK_SIDE1;

	public final int TICK_RIGHT = TICK_SIDE2;

	public final int TICK_ABOVE = TICK_SIDE1;

	public final int TICK_BELOW = TICK_SIDE2;

	public final int TICK_ACROSS = TICK_SIDE1 | TICK_SIDE2;

	public final int NUMERICAL = 1;

	public final int LINEAR = 2;

	public final int LOGARITHMIC = 4;

	public final int DATE_TIME = 8;

	public final int TEXT = 16;

	public final int BOOLEAN = 17;

	public final int PERCENT = 32;

	public final int OTHER = 64;

	public final int ARRAY = 128;

	public final int AXIS = 1;

	public final int LABELS = 2;

	public final int BASE_AXIS = 5;

	public final int ORTHOGONAL_AXIS = 9;

	public final int ANCILLARY_AXIS = 17;

	public final int HORIZONTAL = 0;

	public final int VERTICAL = 1;

	public final int AUTO = 0;

	public final int FORWARD = 1;

	public final int BACKWARD = -1;

	public final int TWO_D = 0;

	public final int TWO_5_D = 1;

	public final int THREE_D = 2;

	public final int LEFT = 1;

	public final int RIGHT = 2;

	public final int CENTER = 3;

	public final int TOP = 4;

	public final int BOTTOM = 8;

	public final int ABOVE = TOP;

	public final int BELOW = BOTTOM;

	public final int OUTSIDE = 16;

	public final int INSIDE = 32;

	/**
	 * Used to calculate base position via AND operation.
	 */
	public final int POSITION_MASK = 63;// 2<<5-1

	/**
	 * Used to adjust the position by one half of width or height.
	 */
	public final int POSITION_MOVE_LEFT = 2 << 5;

	public final int POSITION_MOVE_RIGHT = 2 << 6;

	public final int POSITION_MOVE_ABOVE = 2 << 7;

	public final int POSITION_MOVE_BELOW = 2 << 8;

	public final int MAX = 1;

	public final int MIN = 2;

	public final int VALUE = 3;

	public final int AVERAGE = 4;

	public final int UNDEFINED = 0;

	public final int MAJOR = 0;

	public final int MINOR = 1;

	public final int BASE = 1;

	public final int ORTHOGONAL = 2;

	public final int ANCILLARY_BASE = 3;

	public final int USER_INTERFACE = 1;

	public final int DESIGN_TIME = 2;

	public final int RUN_TIME = 3;

	public final int COLLECTION = 1;

	public final int PRIMITIVE_ARRAY = 2;

	public final int NON_PRIMITIVE_ARRAY = 3;

	public final int BIG_NUMBER_PRIMITIVE_ARRAY = 4;

	public final int NUMBER_PRIMITIVE_ARRAY = 5;

	public static final String EMPTY_STRING = ""; //$NON-NLS-1$

	public static final String ONE_SPACE = " "; //$NON-NLS-1$

	public static final String UNDEFINED_STRING = EMPTY_STRING;

	public static final String NULL_STRING = Messages.getString("constant.null.string"); //$NON-NLS-1$

	public final int SOME_NULL = -2;

	public final int LESS = -1;

	public final int EQUAL = 0;

	public final int MORE = 1;

	public final int LEGEND_ENTRY = 1;

	public final int LEGEND_SEPERATOR = 2;

	public static final int LEGEND_GROUP_NAME = 4;

	public final int LEGEND_MINSLICE_ENTRY = 5;
}
