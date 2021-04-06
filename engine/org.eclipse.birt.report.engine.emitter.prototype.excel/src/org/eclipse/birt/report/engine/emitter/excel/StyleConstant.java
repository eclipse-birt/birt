/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.awt.Color;

public interface StyleConstant {

	public static final int COUNT = 36;

	public static final int FONT_FAMILY_PROP = 0;
	public static final int FONT_SIZE_PROP = 1;
	public static final int FONT_STYLE_PROP = 2;
	public static final int FONT_WEIGHT_PROP = 3;

	public static final int TEXT_LINE_THROUGH_PROP = 4;
	public static final int TEXT_UNDERLINE_PROP = 5;

	public static final int BACKGROUND_COLOR_PROP = 6;
	public static final int COLOR_PROP = 7;

	public static final int H_ALIGN_PROP = 8;
	public static final int V_ALIGN_PROP = 9;

	public static final int BORDER_BOTTOM_COLOR_PROP = 10;
	public static final int BORDER_BOTTOM_STYLE_PROP = 11;
	public static final int BORDER_BOTTOM_WIDTH_PROP = 12;

	public static final int BORDER_TOP_COLOR_PROP = 13;
	public static final int BORDER_TOP_STYLE_PROP = 14;
	public static final int BORDER_TOP_WIDTH_PROP = 15;

	public static final int BORDER_LEFT_COLOR_PROP = 16;
	public static final int BORDER_LEFT_STYLE_PROP = 17;
	public static final int BORDER_LEFT_WIDTH_PROP = 18;

	public static final int BORDER_RIGHT_COLOR_PROP = 19;
	public static final int BORDER_RIGHT_STYLE_PROP = 20;
	public static final int BORDER_RIGHT_WIDTH_PROP = 21;

	public static final int DATE_FORMAT_PROP = 22;
	public static final int NUMBER_FORMAT_PROP = 23;
	public static final int STRING_FORMAT_PROP = 24;
	public static final int DATA_TYPE_PROP = 25;
	public static final int TEXT_TRANSFORM = 26;
	public static final int TEXT_INDENT = 27;

	public static final int DIRECTION_PROP = 28;

	public static final int WHITE_SPACE = 29;

	public static final int BORDER_DIAGONAL_COLOR_PROP = 30;
	public static final int BORDER_DIAGONAL_STYLE_PROP = 31;
	public static final int BORDER_DIAGONAL_WIDTH_PROP = 32;

	public static final int BORDER_ANTIDIAGONAL_COLOR_PROP = 33;
	public static final int BORDER_ANTIDIAGONAL_STYLE_PROP = 34;
	public static final int BORDER_ANTIDIAGONAL_WIDTH_PROP = 35;

	public static final String NULL = "NULL";

	public static final Color HYPERLINK_COLOR = Color.blue;
}
