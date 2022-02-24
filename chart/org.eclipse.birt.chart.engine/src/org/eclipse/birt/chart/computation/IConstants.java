/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.engine.i18n.Messages;

/**
 * Defines all constants here.
 */
public interface IConstants {

	double LOG_10 = Math.log(10);

	int LINE_EXPAND_SIZE = 2;

	int LINE_EXPAND_DOUBLE_SIZE = 4;

	// The old size was 5, but was used without dpi consideration (so as in 72 dpi)
	// When the dpi issue was fixed, charts in 96dpi had bigger ticks than before,
	// So the size has been adjusted to a 72/96 ratio.
	double TICK_SIZE = 3.75;

	int TICK_SIDE1 = 1;

	int TICK_SIDE2 = 2;

	int TICK_NONE = 0;

	int TICK_LEFT = TICK_SIDE1;

	int TICK_RIGHT = TICK_SIDE2;

	int TICK_ABOVE = TICK_SIDE1;

	int TICK_BELOW = TICK_SIDE2;

	int TICK_ACROSS = TICK_SIDE1 | TICK_SIDE2;

	int NUMERICAL = 1;

	int LINEAR = 2;

	int LOGARITHMIC = 4;

	int DATE_TIME = 8;

	int TEXT = 16;

	int BOOLEAN = 17;

	int PERCENT = 32;

	int OTHER = 64;

	int ARRAY = 128;

	int AXIS = 1;

	int LABELS = 2;

	int BASE_AXIS = 5;

	int ORTHOGONAL_AXIS = 9;

	int ANCILLARY_AXIS = 17;

	int HORIZONTAL = 0;

	int VERTICAL = 1;

	int AUTO = 0;

	int FORWARD = 1;

	int BACKWARD = -1;

	int TWO_D = 0;

	int TWO_5_D = 1;

	int THREE_D = 2;

	int LEFT = 1;

	int RIGHT = 2;

	int CENTER = 3;

	int TOP = 4;

	int BOTTOM = 8;

	int ABOVE = TOP;

	int BELOW = BOTTOM;

	int OUTSIDE = 16;

	int INSIDE = 32;

	/**
	 * Used to calculate base position via AND operation.
	 */
	int POSITION_MASK = 63;// 2<<5-1

	/**
	 * Used to adjust the position by one half of width or height.
	 */
	int POSITION_MOVE_LEFT = 2 << 5;

	int POSITION_MOVE_RIGHT = 2 << 6;

	int POSITION_MOVE_ABOVE = 2 << 7;

	int POSITION_MOVE_BELOW = 2 << 8;

	int MAX = 1;

	int MIN = 2;

	int VALUE = 3;

	int AVERAGE = 4;

	int UNDEFINED = 0;

	int MAJOR = 0;

	int MINOR = 1;

	int BASE = 1;

	int ORTHOGONAL = 2;

	int ANCILLARY_BASE = 3;

	int USER_INTERFACE = 1;

	int DESIGN_TIME = 2;

	int RUN_TIME = 3;

	int COLLECTION = 1;

	int PRIMITIVE_ARRAY = 2;

	int NON_PRIMITIVE_ARRAY = 3;

	int BIG_NUMBER_PRIMITIVE_ARRAY = 4;

	int NUMBER_PRIMITIVE_ARRAY = 5;

	String EMPTY_STRING = ""; //$NON-NLS-1$

	String ONE_SPACE = " "; //$NON-NLS-1$

	String UNDEFINED_STRING = EMPTY_STRING;

	String NULL_STRING = Messages.getString("constant.null.string"); //$NON-NLS-1$

	int SOME_NULL = -2;

	int LESS = -1;

	int EQUAL = 0;

	int MORE = 1;

	int LEGEND_ENTRY = 1;

	int LEGEND_SEPERATOR = 2;

	int LEGEND_GROUP_NAME = 4;

	int LEGEND_MINSLICE_ENTRY = 5;
}
