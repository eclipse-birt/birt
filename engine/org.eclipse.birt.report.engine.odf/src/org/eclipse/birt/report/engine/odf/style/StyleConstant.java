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

	int COUNT = 66;

	int FONT_FAMILY_PROP = 0;
	int FONT_SIZE_PROP = 1;
	int FONT_STYLE_PROP = 2;
	int FONT_WEIGHT_PROP = 3;

	int TEXT_LINE_THROUGH_PROP = 4;
	int TEXT_UNDERLINE_PROP = 5;

	int BACKGROUND_COLOR_PROP = 6;
	int COLOR_PROP = 7;

	int H_ALIGN_PROP = 8;
	int V_ALIGN_PROP = 9;

	int BORDER_BOTTOM_COLOR_PROP = 10;
	int BORDER_BOTTOM_STYLE_PROP = 11;
	int BORDER_BOTTOM_WIDTH_PROP = 12;

	int BORDER_TOP_COLOR_PROP = 13;
	int BORDER_TOP_STYLE_PROP = 14;
	int BORDER_TOP_WIDTH_PROP = 15;

	int BORDER_LEFT_COLOR_PROP = 16;
	int BORDER_LEFT_STYLE_PROP = 17;
	int BORDER_LEFT_WIDTH_PROP = 18;

	int BORDER_RIGHT_COLOR_PROP = 19;
	int BORDER_RIGHT_STYLE_PROP = 20;
	int BORDER_RIGHT_WIDTH_PROP = 21;

	int DATE_FORMAT_PROP = 22;
	int NUMBER_FORMAT_PROP = 23;
	int STRING_FORMAT_PROP = 24;
	int DATA_TYPE_PROP = 25;
	int TEXT_TRANSFORM = 26;

	int DIRECTION_PROP = 27;

	int WHITE_SPACE = 28;

	int BORDER_DIAGONAL_COLOR_PROP = 29;
	int BORDER_DIAGONAL_STYLE_PROP = 30;
	int BORDER_DIAGONAL_WIDTH_PROP = 31;

	int BORDER_ANTIDIAGONAL_COLOR_PROP = 32;
	int BORDER_ANTIDIAGONAL_STYLE_PROP = 33;
	int BORDER_ANTIDIAGONAL_WIDTH_PROP = 34;

	int WIDTH = 35;
	int HEIGHT = 36;

	int HIDDEN = 37;

	int PADDING_TOP = 38;
	int PADDING_BOTTOM = 39;
	int PADDING_LEFT = 40;
	int PADDING_RIGHT = 41;

	int MARGIN_TOP = 42;
	int MARGIN_BOTTOM = 43;
	int MARGIN_LEFT = 44;
	int MARGIN_RIGHT = 45;

	int LINE_HEIGHT = 46;
	int LETTER_SPACING = 47;

	int HEADER_HEIGHT = 48;
	int FOOTER_HEIGHT = 49;
	int PAGE_ORIENTATION = 50;

	int BACKGROUND_IMAGE_URL = 51;
	int BACKGROUND_IMAGE_WIDTH = 52;
	int BACKGROUND_IMAGE_HEIGHT = 53;
	int BACKGROUND_IMAGE_LEFT = 54;
	int BACKGROUND_IMAGE_TOP = 55;
	int BACKGROUND_IMAGE_REPEAT = 56;

	int PAGE_BREAK_BEFORE = 57;
	int MASTER_PAGE = 58;

	int TEXT_OVERLINE_PROP = 59;

	// graphic properties
	int GRAPHIC_STROKE = 60;
	int GRAPHIC_FILL = 61;
	int GRAPHIC_FILL_COLOR = 62;
	int GRAPHIC_STROKE_WIDTH = 63;

	int MIN_HEIGHT = 64;
	int TEXT_INDENT = 65;

	String NULL = "NULL"; //$NON-NLS-1$

	Color HYPERLINK_COLOR = Color.blue;

	int TYPE_COUNT = 8;

	int TYPE_PARAGRAPH = 0;
	int TYPE_TEXT = 1;
	int TYPE_TABLE = 2;
	int TYPE_TABLE_COLUMN = 3;
	int TYPE_TABLE_ROW = 4;
	int TYPE_TABLE_CELL = 5;
	int TYPE_PAGE_LAYOUT = 6;
	int TYPE_DRAW = 7;

	String HIDDEN_STYLE_NAME = "Hidden"; //$NON-NLS-1$

}
