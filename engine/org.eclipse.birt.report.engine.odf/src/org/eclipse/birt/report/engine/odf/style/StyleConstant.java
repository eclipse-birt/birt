/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf.style;

import java.awt.Color;

public interface StyleConstant {

	public static final int COUNT = 66;

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

	public static final int DIRECTION_PROP = 27;

	public static final int WHITE_SPACE = 28;

	public static final int BORDER_DIAGONAL_COLOR_PROP = 29;
	public static final int BORDER_DIAGONAL_STYLE_PROP = 30;
	public static final int BORDER_DIAGONAL_WIDTH_PROP = 31;

	public static final int BORDER_ANTIDIAGONAL_COLOR_PROP = 32;
	public static final int BORDER_ANTIDIAGONAL_STYLE_PROP = 33;
	public static final int BORDER_ANTIDIAGONAL_WIDTH_PROP = 34;

	public static final int WIDTH = 35;
	public static final int HEIGHT = 36;

	public static final int HIDDEN = 37;

	public static final int PADDING_TOP = 38;
	public static final int PADDING_BOTTOM = 39;
	public static final int PADDING_LEFT = 40;
	public static final int PADDING_RIGHT = 41;

	public static final int MARGIN_TOP = 42;
	public static final int MARGIN_BOTTOM = 43;
	public static final int MARGIN_LEFT = 44;
	public static final int MARGIN_RIGHT = 45;

	public static final int LINE_HEIGHT = 46;
	public static final int LETTER_SPACING = 47;

	public static final int HEADER_HEIGHT = 48;
	public static final int FOOTER_HEIGHT = 49;
	public static final int PAGE_ORIENTATION = 50;

	public static final int BACKGROUND_IMAGE_URL = 51;
	public static final int BACKGROUND_IMAGE_WIDTH = 52;
	public static final int BACKGROUND_IMAGE_HEIGHT = 53;
	public static final int BACKGROUND_IMAGE_LEFT = 54;
	public static final int BACKGROUND_IMAGE_TOP = 55;
	public static final int BACKGROUND_IMAGE_REPEAT = 56;

	public static final int PAGE_BREAK_BEFORE = 57;
	public static final int MASTER_PAGE = 58;

	public static final int TEXT_OVERLINE_PROP = 59;

	// graphic properties
	public static final int GRAPHIC_STROKE = 60;
	public static final int GRAPHIC_FILL = 61;
	public static final int GRAPHIC_FILL_COLOR = 62;
	public static final int GRAPHIC_STROKE_WIDTH = 63;

	public static final int MIN_HEIGHT = 64;
	public static final int TEXT_INDENT = 65;

	public static final String NULL = "NULL"; //$NON-NLS-1$

	public static final Color HYPERLINK_COLOR = Color.blue;

	public static final int TYPE_COUNT = 8;

	public static final int TYPE_PARAGRAPH = 0;
	public static final int TYPE_TEXT = 1;
	public static final int TYPE_TABLE = 2;
	public static final int TYPE_TABLE_COLUMN = 3;
	public static final int TYPE_TABLE_ROW = 4;
	public static final int TYPE_TABLE_CELL = 5;
	public static final int TYPE_PAGE_LAYOUT = 6;
	public static final int TYPE_DRAW = 7;

	public static final String HIDDEN_STYLE_NAME = "Hidden"; //$NON-NLS-1$

}
